import { Component } from '@angular/core';
import { CreateWeekComponent } from "../create-week/create-week.component";
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-displayer',
  imports: [CreateWeekComponent,CommonModule],
  templateUrl: './displayer.component.html',
  styleUrl: './displayer.component.css'
})
export class DisplayerComponent {

   cf:boolean = false;//create week flag

    createWeek(){
        this.cf = !this.cf;
    }

}
