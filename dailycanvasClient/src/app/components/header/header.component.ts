import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service'; 

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  name: string | null = null;
  time: string = new Date().toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
  date: string = new Date().toLocaleDateString('en-GB', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  });
  day: string = new Date().toLocaleDateString('en-US', { weekday: 'long' });

  updateClock(): void {
    setInterval(()=>{
    this.time = new Date().toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
    }, 1000);
  }

  constructor(private router: Router, private apiService: ApiService) {
    // Token handling logic
    const token = new URLSearchParams(window.location.search).get('token');
    const provider = new URLSearchParams(window.location.search).get('provider');
    const refreshToken = new URLSearchParams(window.location.search).get('refreshToken');

    if (token && provider) {
      localStorage.setItem('token', token);
      localStorage.setItem('provider', provider);

      if(refreshToken) {
      localStorage.setItem('refreshToken',refreshToken);
      }

      console.log('Token:', token);
      console.log('Provider:', provider);
      console.log('Refresh Token:', refreshToken);

      // Remove the token from the URL
      this.router.navigate([], {
        queryParams: {
          token: null,
          provider: null,
          refreshToken: null
        },
        replaceUrl: true
      });

      //after waithing for 2 second relode the window;
      setTimeout(() => {
        window.open('/','_self');
      }, 1000);

    } else if (!localStorage.getItem('token')) {
      this.router.navigate(['/signin']);
    }
  }

  ngOnInit(): void {
    this.fetchEmail();
    this.updateClock();
  }

  

  fetchEmail(): void {
    this.apiService
      .getData('api/v1/user-email') 
      .then((response) => {
        this.name = response.data.name;
        console.log('User info:', response.data);

        //making sure we always have refresh token
        if(response.data.refreshToken) {
          localStorage.setItem('refreshToken',response.data.refreshToken);
        }

        //update the access token as it may have changed (I need to do it pretty much every time)

        if(response.data.accessToken) {
          localStorage.setItem('token',response.data.accessToken);
        }
      })
      .catch((error) => {
        console.error('Error fetching user info', error);
        this.router.navigate(['/signin']); // Redirect if token is invalid
      });
  }

  testcall():void{
    this.apiService
      .getData('/')
      .then((response) => {
        console.log(response);
      })
      .catch((error) => {
        console.error('Error fetching user info', error);
        this.router.navigate(['/signin']); // Redirect
      });

  }

  Logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('provider');
    this.router.navigate(['/signin']);
  }
}
