import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { jwtDecode } from 'jwt-decode';

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
    private router: Router
  ) {}

  login() {
    if (this.loading) return;

    this.error = '';
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

        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        if (err.status === 400 || err.status === 401) {
          this.error = 'Invalid username or password';
        } else {
          this.error = 'Something went wrong. Try again.';
        }
        this.loading = false;
      },
    });
  }
}
