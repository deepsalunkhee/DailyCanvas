import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { CreateWeekComponent } from "../create-week/create-week.component";
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-displayer',
  imports: [CreateWeekComponent,CommonModule],
  templateUrl: './displayer.component.html',
  styleUrl: './displayer.component.css'
})
export class DisplayerComponent implements OnInit,OnChanges {

   @Input() latestWeek!: string ;//latest week date
   @Input() weekData!: {}; // Data for the week to be displayed
   @Output() reloadWeekList = new EventEmitter<void>(); // Event emitter to notify parent component
   @Output() refreshWeekflag = new EventEmitter<void>(); // Event emitter to notify parent component

   Data: any = {};//week data

   cf:boolean = false;//create week flag


   ngOnInit() {
     
   }

    ngOnChanges(changes: SimpleChanges): void {
      if (changes['weekData'] && changes['weekData'].currentValue) {
        this.Data = changes['weekData'].currentValue;
        console.log("week data from parent", this.Data);
      }
    }

    createWeek(){
        this.cf = !this.cf;
    }

    refreshWeek(){
      this.Data={};
      console.log("refresh week list from displayer")
      this.refreshWeekflag.emit(); // Emit the event to notify parent component
    }

    reloadList(){
      console.log("reload week list from displayer")
      this.reloadWeekList.emit(); // Emit the event to notify parent component
      
        
    }

    formatWeekRange(startDateStr: string): string {
      const startDate = new Date(startDateStr);
    
      const monthNames = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
      ];
    
      // Format start date
      const startDay = startDate.getDate();
      const startMonth = monthNames[startDate.getMonth()];
      const startYear = startDate.getFullYear();
    
      // Calculate end date (start + 6 days)
      const endDate = new Date(startDate);
      endDate.setDate(startDate.getDate() + 6);
    
      const endDay = endDate.getDate();
      const endMonth = monthNames[endDate.getMonth()];
      const endYear = endDate.getFullYear();
    
      return `${startDay} ${startMonth} ${startYear} to ${endDay} ${endMonth} ${endYear}`;
    }

    getTodosWithEmpty(todos: any[]): any[] {
      const filledTodos = [...todos];
    
      while (filledTodos.length < 20) {
        filledTodos.push({
          title: '',
          textColor: 'black', // Default text color
          fontSize: 'S',      // Default font size
          scratchColor: 'none', // Default scratch color
          actionType: 'NONE', // Default action type
        }); // Add empty todos with default values
      }
    
      return filledTodos;
    }
    
    orderDays(days: any[]): any[] {
      // Check if days is null, undefined, or not an array
      if (!days || !Array.isArray(days)) {
        console.warn('Days is not an array:', days);
        return []; // Return empty array to avoid errors
      }
      
      const dayOrder = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
      
      // Make a copy of the array to avoid modifying the original
      return [...days].sort((a, b) => {
        return dayOrder.indexOf(a.day) - dayOrder.indexOf(b.day);
      });
    }
    
    // Ensure all todo lists have the same number of items
    getNormalizedTodos(todos: any[]): any[] {
      // Check if todos is null, undefined, or not an array
      if (!todos || !Array.isArray(todos)) {
        return Array(this.getMaxTodoCount()).fill({
          content: '',
          textColor: 'lightgray',
          fontSize: 'M',
          actionType: null,
          scratchColor: null
        });
      }
      
      // Find the maximum number of todos across all days
      const maxTodoCount = this.getMaxTodoCount();
      
      // If this day has fewer todos than the maximum, pad with empty todos
      if (todos.length < maxTodoCount) {
        const emptyTodos = Array(maxTodoCount - todos.length).fill({
          content: '',
          textColor: 'lightgray',
          fontSize: 'M',
          actionType: null,
          scratchColor: null
        });
        
        return [...todos, ...emptyTodos];
      }
      
      return todos;
    }
    
    // Helper method to get the maximum number of todos across all days
    getMaxTodoCount(): number {
      if (!this.Data || !this.Data.days || !Array.isArray(this.Data.days)) return 5; // Default to 5 if no data
      
      let maxCount = 0;
      for (const day of this.Data.days) {
        if (day.todos && Array.isArray(day.todos) && day.todos.length > maxCount) {
          maxCount = day.todos.length;
        }
      }
      
      return maxCount || 5; // Default to 5 if no todos exist
    }
    
    

}
