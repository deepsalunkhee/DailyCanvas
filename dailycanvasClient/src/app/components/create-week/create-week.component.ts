import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-create-week',
  imports: [CommonModule],
  templateUrl: './create-week.component.html',
  styleUrls: ['./create-week.component.css'],
  standalone: true
})
export class CreateWeekComponent implements OnInit {
  // If not passed, the default value is undefined
  @Input() latestWeek: string | undefined = undefined; 
  @Output() closeModal = new EventEmitter<void>();
  @Output() reloadWeekList = new EventEmitter<void>(); 

  apiService: ApiService;
  loader: boolean = false;

  selectedDate: string = ''; // This will be set automatically

  constructor(ApiService: ApiService) {
    this.apiService = ApiService;
  }

  ngOnInit() {
    // Automatically set the selected date based on allowedDate logic
    const allowedDate = this.allowedDate;
    this.selectedDate = allowedDate.toISOString().split('T')[0]; // Set the date in YYYY-MM-DD format

    console.log('Latest week:', this.latestWeek);
    console.log('Selected date:', this.selectedDate); // Log the automatically selected date
  }

  // Get the next Sunday after the largest of the current or latest week
  get allowedDate(): Date {
    const today = new Date();
    const sunday = new Date(today);
    sunday.setDate(today.getDate() - today.getDay()); // Get current week's Sunday
    
    let latestSunday = sunday; // Default to current week's Sunday

    if (this.latestWeek) {
      // If latestWeek exists, calculate the next Sunday after the latest week
      const latestDate = new Date(this.latestWeek);
      latestSunday = new Date(latestDate);
      latestSunday.setDate(latestDate.getDate() - latestDate.getDay()); // Get the Sunday of the latest week
      // We need the largest Sunday (current or latest)
    const nextAllowedSunday = new Date(Math.max(sunday.getTime(), latestSunday.getTime()));
    nextAllowedSunday.setDate(nextAllowedSunday.getDate() + 7); // Add 7 days to get the next Sunday
    return nextAllowedSunday;
    }else{
      // We need the largest Sunday (current or latest)
    const nextAllowedSunday = new Date(Math.max(sunday.getTime(), latestSunday.getTime()));
    nextAllowedSunday.setDate(nextAllowedSunday.getDate() ); // Add 7 days to get the next Sunday
    return nextAllowedSunday;
    }

    

  }

  // Automatically create the week based on the selected date
  async createWeek() {
    

    this.loader = true; // Start loader

    try {
      const createWeekStatus = await this.apiService.postData('api/v1/create-week', { startDate: this.selectedDate });
      if (createWeekStatus) {
        alert('Week created successfully!');
        this.closeModal.emit(); // Emit event to close the modal
        this.reloadWeekList.emit(); // Reload the week list in the parent component
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
