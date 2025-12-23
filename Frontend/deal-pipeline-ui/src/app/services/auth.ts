import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private baseUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  // 🔐 LOGIN API CALL
  login(data: any) {
  return this.http.post(
    'http://localhost:8080/api/auth/login',
    data,
    { responseType: 'text' } // 🔥 THIS IS THE FIX
  );
}

  saveAuthData(res: any) {
    localStorage.setItem('token', res.token);
    localStorage.setItem('role', res.role);
    localStorage.setItem('username', res.username);
  }

  // 💾 SAVE TOKEN
  saveToken(token: string) {
    localStorage.setItem('token', token);
    const decoded: any = jwtDecode(token);

    // 🔥 REAL DATA COMING FROM JWT
    localStorage.setItem('userId', decoded.userId);
    localStorage.setItem('role', decoded.role);
    localStorage.setItem('username', decoded.sub);
  }

  // getRole() {
  //   return localStorage.getItem('role');
  // }

  getUserId() {
    return localStorage.getItem('userId');
  }

  // 📦 GET TOKEN
  // getToken(): string | null {
  //   return localStorage.getItem('token');
  // }

  setSession(token: string, role: string) {
    localStorage.setItem('token', token);
    localStorage.setItem('role', role);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
  getUserRole(): 'ADMIN' | 'ANALYST' | null {
    const token = this.getToken();
    if (!token) return null;

    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.role || null; // role must be in JWT
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

  isAnalyst(): boolean {
    return this.getRole() === 'ANALYST';
  }

  // 🚪 LOGOUT
  logout() {
    localStorage.removeItem('token');
  }

  // ✅ CHECK LOGIN
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

}
