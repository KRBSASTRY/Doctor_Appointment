import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { SignupdoctorComponent } from './signupdoctor/signupdoctor.component';
import { DoctordashboardComponent } from './doctordashboard/doctordashboard.component';
import { UserdashboardComponent } from './userdashboard/userdashboard.component';
import { AboutusComponent } from './aboutus/aboutus.component';
import { PlansComponent } from './plans/plans.component';
import { NopageComponent } from './nopage/nopage.component';
import { HomeComponent } from './home/home.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserService } from './user.service';
import { DoctorService } from './doctor.service';
import { HttpClientModule } from '@angular/common/http';
import { UserModule } from './user/user.module';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    LoginComponent,
    SignupComponent,
    AboutusComponent,
    PlansComponent,
    NopageComponent,
    HomeComponent,
    SignupdoctorComponent,
    DoctordashboardComponent,
    UserdashboardComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    UserModule
  ],
  providers: [
    UserService,
    DoctorService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
