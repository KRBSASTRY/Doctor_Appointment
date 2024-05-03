from flask import request, jsonify, Flask, send_file, render_template, send_from_directory
import Backend.patient_operations as patient
import Backend.doctors_operations as doctor
from Backend.login import *
from flask_cors import CORS
app = Flask(__name__, static_folder='Backend/static', template_folder="Backend/templates")
CORS(app) 

@app.route('/')
def home():
    return render_template("index.html")
@app.route('/test')
def test():
    return "<h1>'test'<h1>"

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

@app.route('/check_doctor_availability', methods=['POST'])
def check_doctor_availability():
    data = request.json
    return doctor.check_slot_availability(data)

@app.route('/update_appointment_status',methods=["POST"])
def update_appointment_status():
    data=request.json
    return doctor.update_appointment_status(data)

@app.route('/upcoming_patient_appointments',methods=["POST"])
def upcoming_patient_appointments():
    data=request.json
    return patient.upcoming_appointments(data)

@app.route('/patient_appointments_history',methods=["POST"])
def patient_appointments_history():
    data=request.json
    return patient.appointments_history(data)

@app.route('/doctor_schedule', methods=['POST'])
def doctor_schedule_route():
    data = request.json
    doctor_id = data.get('doctorId')
    if not doctor_id:
        return jsonify({"error": "Doctor ID is required"}), 400
    
    schedule = doctor.fetch_doctor_schedule(doctor_id)
    
    return jsonify(schedule), 200

@app.route('/doctor_awaiting_schedule', methods=['POST'])
def doctor_schedule():
    data = request.json
    doctor_id = data.get('doctorId')
    # doctor_id = data.get('doctor_id')
    if not doctor_id:
        return jsonify({"error": "Doctor ID is required"}), 400
    
    schedule = doctor.fetch_awaiting_doctor_schedule(doctor_id)
    
    return jsonify(schedule), 200

@app.route('/appointment_review', methods=['POST'])
def appointment_review_route():
    data = request.json
    appointment_id = data.get('appointmentId')
    action = data.get('action')  # 'approve' or 'deny'
    
    if not appointment_id or not action:
        return jsonify({"error": "Appointment ID and action are required"}), 400
    
    status_code, message = doctor.review_appointment_status(appointment_id, action)
    
    return jsonify({"message": message}), status_code

if __name__ == '__main__':
    app.run()
