import { Component, OnInit } from '@angular/core';
import { DoctorService } from 'src/app/doctor.service';

@Component({
  selector: 'app-appointment-details',
  templateUrl: './appointment-details.component.html',
  styleUrls: ['./appointment-details.component.css']
})
export class AppointmentDetailsComponent implements OnInit {

  appointmentDetails: any[] = []

  constructor(public serviceObj: DoctorService) { }

  ngOnInit(): void {
    this.appointmentDetails=(this.serviceObj.getAppointmentDetails().getValue())
    console.log(this.appointmentDetails);

  }

}