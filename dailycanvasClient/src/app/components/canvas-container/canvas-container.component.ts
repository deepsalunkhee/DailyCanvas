import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';

@Component({
  selector: 'app-canvas-container',
  imports: [HeaderComponent, FooterComponent],
  templateUrl: './canvas-container.component.html',
  styleUrl: './canvas-container.component.css'
})
export class CanvasContainerComponent {

  
}
