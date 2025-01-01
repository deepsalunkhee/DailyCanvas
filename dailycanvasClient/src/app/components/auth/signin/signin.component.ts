import { Component } from '@angular/core';
import { AUTH_CONFIG } from '../../../utils/auth-config';

@Component({
  selector: 'app-signin',
  imports: [],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css'
})
export class SigninComponent {
  backendBaseUrl = AUTH_CONFIG.backendBaseUrl;

  loginWithGoogle() {
    window.location.href = `${this.backendBaseUrl}${AUTH_CONFIG.googleLoginEndpoint}`;
  }

  loginWithGitHub() {
    window.location.href = `${this.backendBaseUrl}${AUTH_CONFIG.githubLoginEndpoint}`;
  }
}
