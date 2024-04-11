import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http'
import { UserRoutingModule } from './user-routing.module';
import { UserComponent } from './user.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AccountpageComponent } from './accountpage/accountpage.component';
import { AppointmentDetailsComponent } from './appointment-details/appointment-details.component';



@NgModule({
  declarations: [
    UserComponent,
    AccountpageComponent,
    AppointmentDetailsComponent
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  exports:[
    AccountpageComponent,
    AppointmentDetailsComponent
  ]

})
export class UserModule { }
