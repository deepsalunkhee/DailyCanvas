import { Component } from '@angular/core';
import { AUTH_CONFIG } from '../../../utils/auth-config';
import { ApiService } from '../../../services/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signin',
  imports: [],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css'
})
export class SigninComponent {
  backendBaseUrl = AUTH_CONFIG.backendBaseUrl;

  //if current acces token is valid then redirect to home page


  constructor(private apiService: ApiService, private router: Router) {
    //wait for 3 seconds before checking the token status
    setTimeout(() => {
      this.checkTokenStatus();
    },1500);
  
    
      
    }

  async checkTokenStatus() {
    this.apiService.getTokenStatus('api/v1/tokenStatus').then(status => {
      if (status) {
       //this.router.navigate(['/']);
        window.open('/','_self');
      }
    });
  }


  loginWithGoogle() {
    window.location.href = `${this.backendBaseUrl}${AUTH_CONFIG.googleLoginEndpoint}`;
  }

  loginWithGitHub() {
    window.location.href = `${this.backendBaseUrl}${AUTH_CONFIG.githubLoginEndpoint}`;
  }
}
