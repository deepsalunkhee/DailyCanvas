import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { EditorComponent } from "../editor/editor.component";
import { DisplayerComponent } from "../displayer/displayer.component";
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-canvas-container',
  imports: [HeaderComponent, FooterComponent, EditorComponent, DisplayerComponent],
  templateUrl: './canvas-container.component.html',
  styleUrl: './canvas-container.component.css'
})
export class CanvasContainerComponent implements OnInit {

  apiService: ApiService;

  //get the date of sunday of current week
  currentDate: string;
  latestWeek: string;




  //user data
  weekList: any[] = [];


  constructor(apiService: ApiService) {
    this.apiService = apiService;

    const today = new Date();
    const day = today.getDay(); // 0 (Sun) to 6 (Sat)
    const diff = today.getDate() - day+1;
    today.setDate(diff);
    today.setHours(0, 0, 0, 0);
    this.currentDate = today.toISOString().split('T')[0];

    this.latestWeek = "";

    console.log("Sunday of this week:", this.currentDate);

  }

  async ngOnInit() {
    await this.loadWeekList();
    if (this.weekList.length > 0) {
      this.latestWeek = this.weekList[0].startDate;
      this.onStartWeekLoad(this.currentDate, this.weekList[0].startDate);
    } else {
      alert("you should create a week first 😊")
    }
  }


  async loadWeekList() {

    try {
      const data = await this.apiService.getData("api/v1/get-week-list");
      this.weekList = data.data;

      //sort the week list by start date in descending order
      this.weekList.sort((a, b) => {
        return new Date(b.startDate).getTime() - new Date(a.startDate).getTime();
      });

      this.latestWeek = this.weekList[0].startDate;

      console.log("weekList", this.weekList);
    } catch (error) {
      console.error("Error fetching data:", error);
      return;
    }

  }

  async onStartWeekLoad(currWeek: string, LatestWeek: string) {

    console.log("currWeek", currWeek);
    console.log("LatestWeek", LatestWeek);

    let id: string | undefined;
    if (new Date(currWeek) <= new Date(LatestWeek)) {

      for (let i = 0; i < this.weekList.length; i++) {
        if (this.weekList[i].startDate == currWeek) {
          id = this.weekList[i].weekId;
          break;
        }
      }

      if (id) {
        console.log("loading the curr week ", currWeek);
        this.loadWeek(id);
      } else {
        console.error("No matching week found for the current week.");
      }

    } else {
      id = this.weekList[0].weekId;
      console.log("loading latest week ", id);

      if (id)
        this.loadWeek(id);
    }

  }

  async loadWeek(WeekId: string) {
    try {
      const data = await this.apiService.getData("api/v1/get-a-week?id=" + WeekId);
      console.log("week", data.data);
    } catch (error) {
      console.error("Error fetching data:", error);
      return;
    }
  }






}
