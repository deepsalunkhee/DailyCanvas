import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { EditorComponent } from "../editor/editor.component";
import { DisplayerComponent } from "../displayer/displayer.component";

@Component({
  selector: 'app-canvas-container',
  imports: [HeaderComponent, FooterComponent, EditorComponent, DisplayerComponent],
  templateUrl: './canvas-container.component.html',
  styleUrl: './canvas-container.component.css'
})
export class CanvasContainerComponent {

  
}
