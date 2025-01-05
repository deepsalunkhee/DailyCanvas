import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service'; // Update path based on your folder structure

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  name: string | null = null;

  constructor(private router: Router, private apiService: ApiService) {
    // Token handling logic
    const token = new URLSearchParams(window.location.search).get('token');
    const provider = new URLSearchParams(window.location.search).get('provider');

    if (token && provider) {
      localStorage.setItem('token', token);
      localStorage.setItem('provider', provider);

      console.log('Token:', token);
      console.log('Provider:', provider);

      // Remove the token from the URL
      this.router.navigate([], {
        queryParams: {
          token: null,
          provider: null
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
  }

  

  fetchEmail(): void {
    this.apiService
      .getData('api/v1/user-email') 
      .then((response) => {
        this.name = response.data.name;
        console.log('User info:', response.data);
      })
      .catch((error) => {
        console.error('Error fetching user info', error);
        this.router.navigate(['/signin']); // Redirect if token is invalid
      });
  }

  testcall():void{
    this.apiService
      .getData('api/v1/')
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
