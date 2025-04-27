import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CreateWeekComponent } from "../create-week/create-week.component";
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-displayer',
  imports: [CreateWeekComponent,CommonModule],
  templateUrl: './displayer.component.html',
  styleUrl: './displayer.component.css'
})
export class DisplayerComponent {

   @Input() latestWeek!: string ;//latest week date
   @Output() reloadWeekList = new EventEmitter<void>(); // Event emitter to notify parent component

   cf:boolean = false;//create week flag

    createWeek(){
        this.cf = !this.cf;
    }

    reloadList(){
      console.log("reload week list from displayer")
      this.reloadWeekList.emit(); // Emit the event to notify parent component
      
        
    }

}
