import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';

// import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  userBehaviourSubject=new BehaviorSubject(null)
  userTypeBehaviourSubject = new BehaviorSubject(null)
 
  loginStatus:boolean= false;
  constructor(public httpClientObj: HttpClient) {

  }
  addUserToDatabase(userObj: any) {
    console.log(userObj);
    return this.httpClientObj.post('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/patient_signup', userObj);
  }

  loginUser(userObj: any): Observable<any> {
   
    return this.httpClientObj.post('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/user_login', userObj);

  }

  getUsername(){
    return this.userBehaviourSubject;
  }
  getUserType(){
    return this.userTypeBehaviourSubject;
  }
  userLogout(){
    localStorage.removeItem("token")
   
    this.userBehaviourSubject.next(null)
  }

  getUpcomingAppointments(): Observable<any> {
    return this.httpClientObj.post('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/upcoming_patient_appointments',this.userBehaviourSubject);
  }

  getAppointmentHistory(): Observable<any> {
    return this.httpClientObj.post('http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/patient_appointments_history', this.userBehaviourSubject);
  }
}
