import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ApiService } from '../../services/api.service';



@Component({
  selector: 'app-create-week',
  imports: [CommonModule],
  templateUrl: './create-week.component.html',
  styleUrls: ['./create-week.component.css'],
  standalone: true
})
export class CreateWeekComponent {
  @Input() lastWeekEndDate: Date | null = null; // pareent will pass this will do this later

  apiService: ApiService;
  loader: boolean = false;


  selectedDate: string = '';

  constructor(ApiService: ApiService) {
    this.apiService = ApiService;

  }

  get allowedDate(): Date {
    if (this.lastWeekEndDate) {
      // If last week exists, next allowed is next Sunday
      const nextSunday = new Date(this.lastWeekEndDate);
      nextSunday.setDate(nextSunday.getDate() + 7);
      return nextSunday;
    } else {
      // No last week, allow current week Sunday
      const today = new Date();
      const sunday = new Date(today);
      sunday.setDate(today.getDate() - today.getDay()); // Go to this week's Sunday
      return sunday;
    }
  }

  onDateChange(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.selectedDate = value;
  }

  async createWeek() {

    
    if (!this.selectedDate) {
      alert('Please select a date!');
      return;
    }

    const selected = new Date(this.selectedDate);

    if (selected.getDay() !== 0) {
      alert('Please select a Sunday!');
      return;
    }

 

    this.loader = true; // Start loader

    try {
      const createWeetStatus = await this.apiService.postData('api/v1/create-week', { startDate: this.selectedDate });
      if (createWeetStatus) {
        alert('Week created successfully!');
        this.selectedDate = ''; // Reset the selected date
      } else {
        alert('Failed to create week!');
      }
    } catch (error) {
      console.error('Error creating week:', error);
      alert('An error occurred while creating the week. Please try again later.');
    } finally {
      this.loader = false; // Stop loader
    }




  }
}
