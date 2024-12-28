import { Routes } from '@angular/router';
import { CanvasContainerComponent } from './components/canvas-container/canvas-container.component';
import { SigninComponent } from './components/auth/signin/signin.component';
import { SignupComponent } from './components/auth/signup/signup.component';

export const routes: Routes = [
    {
        path:"",
        component:CanvasContainerComponent
    },
    {
        path:"signin",
        component:SigninComponent
    },
    {
        path:"signup",
        component:SignupComponent
    }
];
