import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorService } from '../doctor.service';
import { UserService } from '../user.service'
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  userLogin: FormGroup;
  userObj: any;
  
  userTypes = ['user', 'doctor'];
  constructor(public formBuilderObj: FormBuilder, private userServiceObj: UserService,private doctorServiceObj:DoctorService, public routerObj: Router) { }
  ngOnInit(): void {
    this.userLogin = this.formBuilderObj.group({
      userType: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }
  loginFormSubmit() {
    console.log(this.userLogin.value)
    //if user type is user - goto user service
    console.log(this.userLogin.value.userType);
    
    if(this.userLogin.value.userType == "user")
    {
      this.userServiceObj.loginUser(this.userLogin.value).subscribe({
        next: (res:any) => {
          console.log("res is ", res);
          if (res.message == "login success")
          {
            console.log("User login success");
            this.userServiceObj.loginStatus =true;
            //local storage
            // localStorage.setItem("token",res.token)
            this.userServiceObj.getUsername().next(res.user)
            this.userServiceObj.getUserType().next(res.user.userType)
            console.log("Logged in")
            this.routerObj.navigateByUrl(`/userdashboard/account`)
            console.log("Dashboard On")
          }
          else{
            console.log("Login Failed")
            //this.routerObj.navigateByUrl("/home")
          }
        },
        error: (err) => {
          console.log("error is ", err)
        }
  
      })
    }
    else{
      this.doctorServiceObj.loginDoctor(this.userLogin.value).subscribe({
        next: (res:any) => {
          console.log("res is ", res);
          if (res.message == "login success")
          {
            console.log("login success");
            this.doctorServiceObj.loginStatus =true;
             //local storage
            //  localStorage.setItem("token",res.token)
            //  this.doctorServiceObj.getDoctorName().next(res.doctor)
            //  this.userServiceObj.getUserType().next(res.doctor.userType)
             this.routerObj.navigateByUrl(`doctordashboard`)
            
            
          }
          else{
            console.log("Login Failed")
            //this.routerObj.navigateByUrl("/home")
          }
        },
        error: (err) => {
          console.log("error is ", err)
        }
  
      })
    }
    
  }
  navigateToSignUp() {
    this.routerObj.navigateByUrl("/signup")
  }
  navigateToSignUpDoctor(){
    this.routerObj.navigateByUrl("/signupdoctor")
    console.log("navigate to signup doctor");
    
  }
}
