import { Component, OnInit } from '@angular/core';
import { DoctorService } from '../doctor.service';
import { UserService } from '../user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public userServiceObj:UserService , public doctorServiceObj:DoctorService, private router: Router) { }

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
  redirectToDashboard() {
    if (this.userServiceObj.loginStatus == true) {
        const userType = this.userServiceObj.userTypeBehaviourSubject.getValue();
        console.log("Type",userType);
        if (userType == 'user') {
            this.router.navigate(['/app-accountpage']);
        } else if (userType == 'doctor') {
            this.router.navigate(['/doctordashboard']);
        }
    } else {
        this.router.navigate(['/login']);
    }
}
}
