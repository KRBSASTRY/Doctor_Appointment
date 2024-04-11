import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { UserService } from '../user.service';
@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  userSignup: FormGroup;
  userTypes = ['USER', 'DOCTOR'];
  constructor(private signupFormBuilderObj: FormBuilder, private routerObj: Router, private userServiceObj: UserService) { }

  ngOnInit(): void {
    this.userSignup = this.signupFormBuilderObj.group({
      name: ['', Validators.required],
      username: ['', Validators.required],
      age: ['', Validators.required],
      email: ['', Validators.required],
      city: ['', Validators.required],
      password: ['', Validators.required],
      phoneno: ['', Validators.required],
      bp: ['', Validators.required],
      diabetes: ['', Validators.required],
      height: ['', Validators.required],
      weight: ['', Validators.required]
    })


  }
  //on submitting the form
  signupSubmit() {

    console.log(this.userSignup.value)
    this.userServiceObj.addUserToDatabase(this.userSignup.value).subscribe({
      next: (res) => {
        console.log("res is", res);

        this.routerObj.navigateByUrl("/login")


      },
      error: (err) => {
        console.log("something went wrong ", err)
      }
    })

  }
  get name() {
    return this.userSignup.get("name")
  }
  get username() {
    return this.userSignup.get("username")
  }
  get password() {
    return this.userSignup.get("password")
  }
  get age() {
    return this.userSignup.get("age")
  }
  get phoneno() {
    return this.userSignup.get("phoneno")
  }
  get email() {
    return this.userSignup.get("email")
  }
  get city() {
    return this.userSignup.get("city")
  }

  get bp() { return this.userSignup.get("bp"); }
get diabetes() { return this.userSignup.get("diabetes"); }
get height() { return this.userSignup.get("height"); }
get weight() { return this.userSignup.get("weight"); }

}
