import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AdminComponent } from './admin/admin.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { AppRoutingModule } from './/app-routing.module';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { LoadFilesComponent } from './load-files/load-files.component';
import { InfoDetailsComponent } from './info-details/info-details.component';
import {UploadFileService} from "./upload-file.service";
import { LoadFilesCitiesComponent } from './load-files-cities/load-files-cities.component';
import { ViewDetailsComponent } from './view-details/view-details.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    AdminComponent,
    ProductDetailComponent,
    LoadFilesComponent,
    InfoDetailsComponent,
    LoadFilesCitiesComponent,
    ViewDetailsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [
    UploadFileService
  ],
  bootstrap: [AppComponent,
  ]
})
export class AppModule { }
