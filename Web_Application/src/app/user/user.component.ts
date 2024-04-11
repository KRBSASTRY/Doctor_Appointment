import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Router } from "@angular/router";
import { DoctorService } from '../doctor.service';
import { UserService } from '../user.service';


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {


  
  city=["Texas","Albany","MaryLand","Balmore"]
  specialization=["Dentist","Cardiologist","Dermatologist","Gynecologist","Neurologist","Psychiartist"]


//all doctors data
doctors: any[] = []
//value from city select
cityFromSelect: any = this.city[0];
//value from specialisation select
specFromSelect: any=this.specialization[0];
//doctors data based on book appointment button selection
selectedDoctor: any[] = [];
//appointment times,date data
appointmentDataObj: any[] = [];
//based on city selection doctors array
doctorsCity: any[] = [];
//doctors array based on city and then specialization selection
finalDoctors: any[] = [];
//comment for doctors results
comment: string = "";


accObj = {
  doctorname: '',
  consultationFee: 0,
  date: "01/02/2012",
  timmings: "5 am"

};

  constructor(public serviceObj: DoctorService,public userServiceObj: UserService, public fb: FormBuilder, public activatedRouteObj: ActivatedRoute, private routerObj: Router) { }

  ngOnInit(): void {
    this.getdoctor();
    
  }
  
  getdoctor() {
    this.serviceObj.getDoctorList().subscribe({
      next: (obj) => {
        this.doctors = obj.payload;
        console.log("doctors list after assigning is ", this.doctors);
      },
      error: (err) => { console.log(err) }
    })
  }
  
  getCity(city1) {
    this.doctorsCity.splice(0, this.doctorsCity.length)
    this.cityFromSelect = city1.target.value
    this.doctorsCity.push(this.doctors.filter(docOBj => docOBj.city == this.cityFromSelect))
    console.log(this.doctorsCity);
  }
  getDataAccSpec(spec) {
    this.finalDoctors.splice(0, this.finalDoctors.length)
    this.specFromSelect = spec.target.value
    this.finalDoctors.push(this.doctorsCity[0].filter(docObj => docObj.specialization == this.specFromSelect))
    if (this.finalDoctors[0].length==0) {
      this.comment = "NO DOCTORS FOUND";
      console.log(this.comment, "comment is");
    }
    else{
      this.comment=""
    }
    console.log(this.finalDoctors);
  }

 
  checkLogin(){
    console.log(this.userServiceObj.loginStatus);
    if(this.userServiceObj.loginStatus == true)
    {
      this.goToAccountPage()
    }
    else{
      this.routerObj.navigateByUrl("/login")
    }
  }

  goToModal(username) {
  
    this.appointmentDataObj.splice(0, this.appointmentDataObj.length)
    this.selectedDoctor.push(this.finalDoctors[0].find(doctorObj => doctorObj.username == username));

    this.serviceObj.getDoctorData().next(this.selectedDoctor)
    this.appointmentDataObj = this.serviceObj.getDoctorData().getValue()

    console.log("from user c", this.appointmentDataObj)
  }

  appointmentForm = this.fb.group({
    date: [''],
    timmings: ['']
  })
  onSubmit() {
    if (this.appointmentForm.valid) {
      const selectedDoctorId = this.selectedDoctor[0]?.doctor_id; // Assuming your doctor object has an 'id' property
      const selectedTime = this.appointmentForm.value.timmings;
      const date = this.appointmentForm.value.date;
      
      this.serviceObj.checkDoctorAvailability(selectedDoctorId, selectedTime, date).subscribe({
        next: (response) => {
          if (response.available) {
            this.proceedWithBooking();
          } else {
            alert("Doctor is not available in that slot. Please book another slot.");
          }
        },
        error: (err) => {
          console.error("Error checking doctor availability: ", err);
        }
      });
    } else {
      console.log("Form is not valid");
    }
  }
  
  proceedWithBooking() {
    // Assuming the necessary details are set from the form and selected doctor
    if (this.userServiceObj.loginStatus === true) {
      const appointmentDetails = {
        patient_id:this.userServiceObj.userBehaviourSubject.value.patient_id,
        doctor_id: this.selectedDoctor[0]?.doctor_id, // Adjust according to your doctor object structure
        date: this.appointmentForm.value.date,
        appointment_time: this.appointmentForm.value.timmings,
        consultationFee: this.selectedDoctor[0]?.consultationFee
      };
  
      // Now call the bookAppointment method with the gathered details
      this.serviceObj.bookAppointment(appointmentDetails).subscribe({
        next: (response) => {
          // Handle the successful booking response
          console.log('Appointment booked successfully', response);
          this.accObj.timmings = this.appointmentForm.value.timmings
          this.accObj.date = this.appointmentForm.value.date
          for (let v of this.appointmentDataObj) {
            this.accObj.doctorname = v.name
            this.accObj.consultationFee = v.consultationFee
          }
          // For example, you might want to redirect the user to their dashboard
          this.routerObj.navigateByUrl('/userdashboard/account');
          // Or display a success message/alert
          alert('Appointment booked successfully!');
        },
        error: (err) => {
          // Handle any errors encountered during the booking
          console.error('Error booking the appointment:', err);
          // Show an error message to the user
          alert('Failed to book the appointment. Please try again.');
        }
      });
    } else {
      // If the user is not logged in, redirect to the login page
      this.routerObj.navigateByUrl("/login");
    }
  }
  
  
  
  // onSubmit() {
  //   console.log(typeof (this.appointmentForm.value.timmings))
  //   this.accObj.timmings = this.appointmentForm.value.timmings
  //   this.accObj.date = this.appointmentForm.value.date
  //   for (let v of this.appointmentDataObj) {
  //     this.accObj.doctorname = v.name
  //     this.accObj.consultationFee = v.consultationFee
  //   }
  //   console.log("obj is", this.accObj);
  // }


  goToAccountPage() {
    this.serviceObj.getAccountPageDetails().next(this.accObj)
    this.routerObj.navigateByUrl('/userdashboard/account')
  }


}