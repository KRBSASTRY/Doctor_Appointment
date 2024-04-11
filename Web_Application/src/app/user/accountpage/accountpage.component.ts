import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorService } from 'src/app/doctor.service';
import { UserService } from 'src/app/user.service';

@Component({
  selector: 'app-accountpage',
  templateUrl: './accountpage.component.html',
  styleUrls: ['./accountpage.component.css']
})
export class AccountpageComponent implements OnInit {
  upcomingAppointmentsData: any[] = [];
  appointmentHistoryData: any[] = [];
  showUpcomingAppointments: boolean = true;

  constructor(private userService: UserService, private routerObj: Router) { }

  ngOnInit(): void {
    // this.getUpcomingAppointments();
  }

  getUpcomingAppointments() {
    this.userService.getUpcomingAppointments().subscribe((data: any) => {
      this.upcomingAppointmentsData = data;
    });
  }

  getAppointmentHistory() {
    this.userService.getAppointmentHistory().subscribe((data: any) => {
      this.appointmentHistoryData = data;
    });
  }

  toggleView(view: string) {
    if (view === 'upcoming') {
      this.showUpcomingAppointments = true;
      this.getUpcomingAppointments();
    } else {
      this.showUpcomingAppointments = false;
      this.getAppointmentHistory();
    }
  }

  bookAppointment() {
    this.routerObj.navigateByUrl('/userdashboard');
  }
}