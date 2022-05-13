import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AddProfileComponent } from './components/add-profile/add-profile.component';
import { ProfileListComponent } from './components/profile-list/profile-list.component';
import { ProfileService } from './shared/profile.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import  {FilterPipe} from './shared/filter.pipe';

@NgModule({
  declarations: [
    AppComponent,
    AddProfileComponent,
    ProfileListComponent,
    FilterPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [ProfileService],
  bootstrap: [AppComponent]
})
export class AppModule { }
