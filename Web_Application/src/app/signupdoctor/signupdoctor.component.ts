import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorService } from '../doctor.service';

@Component({
  selector: 'app-signupdoctor',
  templateUrl: './signupdoctor.component.html',
  styleUrls: ['./signupdoctor.component.css']
})
export class SignupdoctorComponent implements OnInit {
  doctorSignup: FormGroup;
  constructor(private signupFormBuilderObj: FormBuilder, private routerObj: Router, private doctorServiceObj: DoctorService) { }

  ngOnInit(): void {
    this.doctorSignup = this.signupFormBuilderObj.group({
      name: ['', Validators.required],
      username: ['', Validators.required],
      experience: ['', Validators.required],
      consultationFee: ['', Validators.required],
      city: ['', Validators.required],
      password: ['', Validators.required],
      phoneno: ['', Validators.required],
      specialization: ['', Validators.required]
    })

  }
  //on submitting the form
  signupSubmit() {

    console.log(this.doctorSignup.value)
    this.doctorServiceObj.addDoctorToDatabase(this.doctorSignup.value).subscribe({
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
    return this.doctorSignup.get("name")
  }
  get username() {
    return this.doctorSignup.get("username")
  }
  get password() {
    return this.doctorSignup.get("password")
  }
  get experience() {
    return this.doctorSignup.get("experience")
  }
  get phoneno() {
    return this.doctorSignup.get("phoneno")
  }
  get consultationFee() {
    return this.doctorSignup.get("consultationFee")
  }
  get specialization() {
    return this.doctorSignup.get("specialization")
  }
  get city(){
    return this.doctorSignup.get("city")
  }
}
