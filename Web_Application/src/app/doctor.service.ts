import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

// import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {
  loginStatus:boolean= false;
  doctorData: any; 
  constructor(private httpClientObj: HttpClient) { }
  checkDoctorAvailability(doctor_id: string, slot: string, date : string): Observable<any> {
    return this.httpClientObj.post<any>('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/check_doctor_availability', { doctor_id, slot, date });
  }

  // Function to book appointment
  bookAppointment(appointmentDetails: any): Observable<any> {
    return this.httpClientObj.post<any>('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/book_appointment', appointmentDetails);
  }
  loginDoctor(userObj: any): Observable<any> {
    // return this.httpClientObj.post('http://ec2-3-144-21-20.us-east-2.compute.amazonaws.com'+"/doctor_login", userObj);
    return this.httpClientObj.post('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com' + "/doctor_login", userObj)
    .pipe(
      tap((res: any) => {
        if (res.message === "login success") {
          this.setLoggedInDoctor(res.doctor); // Call setLoggedInDoctor with doctor data
        }
      })
    );
  }

  setLoggedInDoctor(doctorData: any) {
    this.doctorData = doctorData; // Assuming doctorData contains doctor's ID
    this.loginStatus = true;
  }
  addDoctorToDatabase(userObj: any): Observable<any> {
    return this.httpClientObj.post('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com'+"/doctor_signup", userObj);
  }
  getLoggedInDoctor(){
    return this.doctorData;
  }

  //get doctor values based on book appointment button selection
  doctorBehaviourSubject = new BehaviorSubject(null);
  logoutdoctorBehaviourSubject = new BehaviorSubject(null);
  getDoctorData() {
    return this.doctorBehaviourSubject
  }
  //get appointment details after successfull payment
  appointmentBehaviourSubject = new BehaviorSubject(null);
  getAppointmentDetails() {
    return this.appointmentBehaviourSubject
  }
  //getAccountPageDetails
  accountBehaviourSubject = new BehaviorSubject(null);
  getAccountPageDetails() {
    return this.accountBehaviourSubject
  }
  // getDoctorList
  getDoctorList(): Observable<any> {
    return this.httpClientObj.get<any>('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/get_doctors')
  }
  getDoctorName(){
    return this.logoutdoctorBehaviourSubject;
  }
  doctorLogout(){
    localStorage.removeItem("token")
    this.logoutdoctorBehaviourSubject.next(null)
  }
  reviewAppointment(appointmentId: string, action: string): Observable<any> {
    return this.httpClientObj.post<any>('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/appointment_review', { appointmentId, action });
  }
  getpendingDoctorSchedule():Observable<any>{
    return this.httpClientObj.post<any>('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/doctor_awaiting_schedule', {"doctorId":this.doctorData.doctor_id});

  }
  getDoctorSchedule(): Observable<any> {
    console.log(this.doctorData);
    return this.httpClientObj.post<any>('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/doctor_schedule', {"doctorId":this.doctorData.doctor_id});
  }

  // Function to get doctor appointments
  getDoctorAppointments(): Observable<any> {
    return this.httpClientObj.get<any>('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/doctor_appointments');
  }
}
