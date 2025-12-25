import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { jwtDecode } from 'jwt-decode';
import { ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private http: HttpClient
  ) {}

  private triggerShakeAnimation() {
    const loginCard = document.querySelector('.login-card');
    if (loginCard) {
      loginCard.classList.add('shake');
      setTimeout(() => {
        loginCard.classList.remove('shake');
      }, 600);
    }
  }

  login() {
    if (this.loading) return;

    this.error = '';
    
    // Input validation
    if (!this.username.trim()) {
      this.error = 'Username is required';
      this.triggerShakeAnimation();
      this.cdr.detectChanges();
      return;
    }
    
    if (!this.password.trim()) {
      this.error = 'Password is required';
      this.triggerShakeAnimation();
      this.cdr.detectChanges();
      return;
    }

    if (this.password.length < 3) {
      this.error = 'Password must be at least 3 characters';
      this.triggerShakeAnimation();
      this.cdr.detectChanges();
      return;
    }

    this.loading = true;

    const payload = {
      username: this.username,
      password: this.password,
    };

    this.authService.login(payload).subscribe({
      next: (token: string) => {
        console.log('Login success');
        console.log('JWT TOKEN 👉', token);

        // ✅ Decode JWT
        const decoded: any = jwtDecode(token);
        console.log('DECODED JWT 👉', decoded);
        
        // ✅ Store ONLY ONCE
        localStorage.setItem('token', token);
        localStorage.setItem('role', decoded.role);      // ADMIN / ANALYST
        localStorage.setItem('username', decoded.sub);   // admin / analyst
        console.log('Logged in as:', decoded.role);
        // ✅ Redirect
        this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
          this.router.navigate(['/deals']);
        });
        this.authService.saveToken(token);
        this.loading = false;
      },  
      error: (err) => {
        console.error('Login Failed:',err);
        this.loading = false;
        this.triggerShakeAnimation();
        
        if (err.status === 400) {
          this.error = 'Invalid username or password format';
        } else if (err.status === 401) {
          this.error = 'Invalid username or password';
        } else if (err.status === 429) {
          this.error = 'Too many login attempts. Please try again later.';
        } else if (err.status === 0 || !navigator.onLine) {
          this.error = 'Network error. Please check your connection.';
        } else {
          this.error = 'Login failed. Please try again.';
        }
        
        // Force change detection
        this.cdr.detectChanges();
      },
    });
  }
}
