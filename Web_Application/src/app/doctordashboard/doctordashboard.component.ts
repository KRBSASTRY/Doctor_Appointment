import { Component, OnInit } from '@angular/core';
import { DoctorService } from '../doctor.service';

@Component({
  selector: 'app-doctordashboard',
  templateUrl: './doctordashboard.component.html',
  styleUrls: ['./doctordashboard.component.css']
})
export class DoctordashboardComponent implements OnInit {
  schedule: any[];
  doctor:any[];
  pendingAppointments:any[];
  showMySchedule: boolean =false;
  showreview:boolean=false;
  constructor(private doctorServiceObj:DoctorService) { }

  ngOnInit(): void {
    this.doctor = this.doctorServiceObj.getLoggedInDoctor(); 
   
  }
  toggleSchedule(view: string) {
    if (view === 'schedule') {
      this.showMySchedule = true;
      this.showreview = false;
      this.getMySchedule();
    } else {
      this.showMySchedule = false;
      this.showreview = true;
      this.reviewAppointments();
    }
  }

  getMySchedule() {
    this.doctorServiceObj.getDoctorSchedule().subscribe(
      response => {
        console.log('My Schedule:', response);
        this.schedule = response; // Assign fetched schedule to property
      },
      error => {
        console.error('Error fetching schedule:', error);
      }
    );
  }
  reviewAppointments() {
    this.doctorServiceObj.getpendingDoctorSchedule().subscribe(response => {
      // Assuming response contains data for UI with PatientName, Appointment Date and time, health records
      // Also, give user options for approve and deny
      this.pendingAppointments = response;
      console.log('Review Appointments:', response);
    }, error => {
      console.error('Error fetching appointments:', error);
    });
  }

  approveAppointment(appointmentId: string) {
    this.doctorServiceObj.reviewAppointment(appointmentId,"confirmed").subscribe(response => {
      console.log('Appointment approved successfully');
    }, error => {
      console.error('Error approving appointment:', error);
    });
  }

  denyAppointment(appointmentId: string) {
    this.doctorServiceObj.reviewAppointment(appointmentId,"canceled").subscribe(response => {
      console.log('Appointment denied successfully');
    }, error => {
      console.error('Error denying appointment:', error);
    });
  }

}
