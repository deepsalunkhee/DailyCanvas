import { Injectable } from '@angular/core';
import axios from 'axios';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'https://dailycanvasserver.onrender.com'; 
  private token = localStorage.getItem('token');
  private refreshToken = localStorage.getItem('refreshToken');

  constructor() {}

  private getAuthHeader() {
    return { Authorization: `Bearer ${this.token}`,
      RefreshToken: this.refreshToken
     };
  }

   async getTokenStatus(endpoint: string) {
    return axios.get(`${this.baseUrl}/${endpoint}`, {
      headers: this.getAuthHeader(),
    }).then(response => {
      if (response.status === 200) {
        return true;
      } else {
        return false;
      }
    }).catch(error => {
      console.error(error);
      return false;
    });
  }

  getData(endpoint: string) {
    const data= axios.get(`${this.baseUrl}/${endpoint}`, {
      headers: this.getAuthHeader(),
      withCredentials: true,
    });

    //console.log("Obtained:",data);
    return data;
  }

  postData(endpoint: string, data: any) {
    const sent= axios.post(`${this.baseUrl}/${endpoint}`, data, {
      headers: this.getAuthHeader(),
      withCredentials: true,
    });

    //console.log("Obtained after send:",sent);
    return sent;
  }
}
