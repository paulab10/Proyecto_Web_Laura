import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardComponent} from './dashboard/dashboard.component';
import {LoadFilesComponent} from "./load-files/load-files.component";
import {InfoDetailsComponent} from "./info-details/info-details.component";
import {LoadFilesCitiesComponent} from "./load-files-cities/load-files-cities.component";
import {ViewDetailsComponent} from "./view-details/view-details.component";

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'load-files', component: LoadFilesComponent},
  { path: 'info-details', component: InfoDetailsComponent},
  { path: 'load-files-cities', component: LoadFilesCitiesComponent},
  { path: 'view-details', component: ViewDetailsComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [
    RouterModule
  ]

})
export class AppRoutingModule { }
