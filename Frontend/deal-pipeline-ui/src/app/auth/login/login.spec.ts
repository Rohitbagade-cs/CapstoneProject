import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';

import { LoginComponent } from './login';
import { AuthService } from '../services/auth';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockAuthService: jasmine.SpyObj<AuthService>;
  let mockRouter: jasmine.SpyObj<Router>;
  let mockCdr: jasmine.SpyObj<ChangeDetectorRef>;

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['login', 'saveToken']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl', 'navigate']);
    const cdrSpy = jasmine.createSpyObj('ChangeDetectorRef', ['detectChanges']);

    await TestBed.configureTestingModule({
      imports: [LoginComponent, FormsModule, CommonModule, HttpClientTestingModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ChangeDetectorRef, useValue: cdrSpy },
        HttpClientTestingModule
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    mockAuthService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    mockRouter = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    mockCdr = TestBed.inject(ChangeDetectorRef) as jasmine.SpyObj<ChangeDetectorRef>;
    
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty form', () => {
    expect(component.username).toBe('');
    expect(component.password).toBe('');
    expect(component.error).toBe('');
    expect(component.loading).toBe(false);
  });

  describe('Input Validation', () => {
    it('should show error when username is empty', () => {
      component.username = '';
      component.password = 'password123';
      
      component.login();
      
      expect(component.error).toBe('Username is required');
      expect(component.loading).toBe(false);
      expect(mockCdr.detectChanges).toHaveBeenCalled();
    });

    it('should show error when password is empty', () => {
      component.username = 'admin';
      component.password = '';
      
      component.login();
      
      expect(component.error).toBe('Password is required');
      expect(component.loading).toBe(false);
      expect(mockCdr.detectChanges).toHaveBeenCalled();
    });

    it('should show error when password is too short', () => {
      component.username = 'admin';
      component.password = 'ab';
      
      component.login();
      
      expect(component.error).toBe('Password must be at least 3 characters');
      expect(component.loading).toBe(false);
      expect(mockCdr.detectChanges).toHaveBeenCalled();
    });

    it('should not show error for valid inputs', () => {
      component.username = 'admin';
      component.password = 'password123';
      
      mockAuthService.login.and.returnValue({ subscribe: () => {} });
      
      component.login();
      
      expect(component.error).toBe('');
      expect(component.loading).toBe(true);
    });
  });

  describe('Login Success', () => {
    it('should handle successful login', fakeAsync(() => {
      const mockToken = 'mock.jwt.token';
      const mockDecoded = { sub: 'admin', role: 'ADMIN', userId: 1 };
      
      component.username = 'admin';
      component.password = 'password123';
      
      mockAuthService.login.and.returnValue({
        subscribe: (callbacks: any) => {
          callbacks.next(mockToken);
        }
      });
      
      // Mock jwtDecode and localStorage
      spyOn(localStorage, 'setItem');
      spyOn(window, 'atob').and.returnValue(JSON.stringify(mockDecoded));
      
      component.login();
      tick();
      
      expect(mockAuthService.saveToken).toHaveBeenCalledWith(mockToken);
      expect(localStorage.setItem).toHaveBeenCalledWith('token', mockToken);
      expect(component.loading).toBe(false);
    }));
  });

  describe('Login Errors', () => {
    it('should handle 400 error', fakeAsync(() => {
      component.username = 'admin';
      component.password = 'password123';
      
      mockAuthService.login.and.returnValue({
        subscribe: (callbacks: any) => {
          callbacks.error({ status: 400 });
        }
      });
      
      component.login();
      tick();
      
      expect(component.error).toBe('Invalid username or password format');
      expect(component.loading).toBe(false);
      expect(mockCdr.detectChanges).toHaveBeenCalled();
    }));

    it('should handle 401 error', fakeAsync(() => {
      component.username = 'admin';
      component.password = 'wrongpassword';
      
      mockAuthService.login.and.returnValue({
        subscribe: (callbacks: any) => {
          callbacks.error({ status: 401 });
        }
      });
      
      component.login();
      tick();
      
      expect(component.error).toBe('Invalid username or password');
      expect(component.loading).toBe(false);
      expect(mockCdr.detectChanges).toHaveBeenCalled();
    }));

    it('should handle network error', fakeAsync(() => {
      component.username = 'admin';
      component.password = 'password123';
      
      mockAuthService.login.and.returnValue({
        subscribe: (callbacks: any) => {
          callbacks.error({ status: 0 });
        }
      });
      
      component.login();
      tick();
      
      expect(component.error).toBe('Network error. Please check your connection.');
      expect(component.loading).toBe(false);
      expect(mockCdr.detectChanges).toHaveBeenCalled();
    }));
  });

  describe('Shake Animation', () => {
    it('should trigger shake animation on error', () => {
      const mockElement = jasmine.createSpyObj('HTMLElement', ['classList', 'remove']);
      spyOn(document, 'querySelector').and.returnValue(mockElement);
      spyOn(window, 'setTimeout').and.callFake((fn) => fn());
      
      component.triggerShakeAnimation();
      
      expect(document.querySelector).toHaveBeenCalledWith('.login-card');
      expect(mockElement.classList.add).toHaveBeenCalledWith('shake');
      expect(mockElement.classList.remove).toHaveBeenCalledWith('shake');
    });
  });

  describe('Loading State', () => {
    it('should not submit if already loading', () => {
      component.loading = true;
      component.username = 'admin';
      component.password = 'password123';
      
      mockAuthService.login.and.returnValue({ subscribe: () => {} });
      
      component.login();
      
      expect(mockAuthService.login).not.toHaveBeenCalled();
    });
  });
});
