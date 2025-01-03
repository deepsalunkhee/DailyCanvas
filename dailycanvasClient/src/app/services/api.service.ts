import { Injectable } from '@angular/core';
import axios from 'axios';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080'; // Replace with your backend URL
  private token = localStorage.getItem('token');

  constructor() {}

  private getAuthHeader() {
    return { Authorization: `Bearer ${this.token}` };
  }

  getData(endpoint: string) {
    const data= axios.get(`${this.baseUrl}/${endpoint}`, {
      headers: this.getAuthHeader(),
    });

    //console.log("Obtained:",data);
    return data;
  }

  postData(endpoint: string, data: any) {
    const sent= axios.post(`${this.baseUrl}/${endpoint}`, data, {
      headers: this.getAuthHeader(),
    });

    //console.log("Obtained after send:",sent);
    return sent;
  }
}
