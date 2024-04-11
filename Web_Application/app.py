from flask import request, jsonify, Flask, send_file, render_template
# from app_routes import app
# from app.patient_operations import * 
import Backend.patient_operations as patient
import Backend.doctors_operations as doctor
# from app.doctors_operations import * 
from Backend.login import *
from flask_cors import CORS

# Define API endpoints
# app=Flask(__name__)
app = Flask(__name__, static_folder='Backend/static', template_folder="Backend/templates")
CORS(app) 

@app.route('/')
def home():
    return render_template("index.html")

# Login
@app.route('/user_login', methods=['POST'])
def userLogin():
    # Implement login logic here
    login_user=request.json
    return patient.user_login(login_user)
    pass

@app.route('/doctor_login', methods=['POST'])
def doctorLogin():
    login_doctor = request.json
    return doctor.doctor_login(login_doctor)

@app.route('/doctor_signup', methods=['POST'])
def doctor_signup():
    new_doctor= request.json
    return doctor.create_doctor(new_doctor)
    # pass

@app.route('/patient_signup', methods=['POST'])
def patient_signup():
    new_user= request.json
    return patient.create_user(new_user)

@app.route('/get_doctors', methods=['GET'])
def getDoctors():
    return doctor.get_doctors()
    # pass


# Patient Operations
@app.route('/user_medical_records', methods=['GET'])
def get_medical_records():
    # Implement logic to fetch medical records from MongoDB
    pass

@app.route('/book_appointment', methods=['POST'])
def book_appointment():
    payload = request.json  
    # Call function to book appointment
    if doctor.book_appointment(payload):
        return jsonify({'message': 'Appointment booked successfully'}), 200
    else:
        return jsonify({'message': 'Failed to book appointment'}), 500

# Doctor Operations
@app.route('/doctor_appointments', methods=['GET'])
def get_appointments():
    # Implement logic to fetch appointments for the doctor
    pass

@app.route('/check_doctor_availability', methods=['POST'])
def check_doctor_availability():
    data = request.json
    return doctor.check_slot_availability(data)

@app.route('/update_appointment_status',methods=["POST"])
def update_appointment_status():
    data=request.json
    return doctor.update_appointment_status(data)


@app.route('/doctor_respond_appointment', methods=['POST'])
def respond_appointment():
    # Implement logic for doctor to respond to appointment requests
    pass

@app.route('/upcoming_patient_appointments',methods=["POST"])
def upcoming_patient_appointments():
    data=request.json
    return patient.upcoming_appointments(data)

@app.route('/patient_appointments_history',methods=["POST"])
def patient_appointments_history():
    data=request.json
    return patient.appointments_history(data)

if __name__ == '__main__':
    app.run()
