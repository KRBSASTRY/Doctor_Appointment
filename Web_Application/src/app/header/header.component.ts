import { Component, OnInit } from '@angular/core';
import { DoctorService } from '../doctor.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public userServiceObj:UserService , public doctorServiceObj:DoctorService) { }

  ngOnInit(): void {
  }

  logout(){
    //user ? userservice logout sholud be called
    console.log(this.userServiceObj.getUserType);
   


    if( this.userServiceObj.userTypeBehaviourSubject.getValue() == 'user')
    {
      this.userServiceObj.userLogout()
      this.userServiceObj.loginStatus = false;
    }
    else{
      
      this.doctorServiceObj.doctorLogout();
      this.userServiceObj.loginStatus = false;
    }
    //doctorservice obj should be called
  }
}
