from flask import Flask, request, jsonify
from flask_bcrypt import Bcrypt
from Web_Application.AWS_deployment.Key.db_access import *
from datetime import datetime
app = Flask(__name__)
bcrypt = Bcrypt()

def create_user(new_user):
    # Check if the username already exists in the database
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM patients WHERE username = %s", (new_user["username"],))
    user = cursor.fetchone()
    if user:
        cursor.close()
        return jsonify({"message": "Username already exists"}), 400
    # Insert the new user record into the patients table
    cursor.execute("INSERT INTO patients (username, password, email, city, phoneno, age, name) VALUES (%s, %s, %s, %s, %s, %s, %s)",
                   (new_user["username"], new_user["password"], new_user["email"], new_user["city"], new_user["phoneno"], new_user["age"], new_user["name"]))
    conn.commit()

    # Get the inserted user's ID
    new_user_id = cursor.lastrowid

    # Close the cursor
    cursor.close()

    # Return success message and inserted user record
    new_user["_id"] = new_user_id

    patient_record = {
        "PatientID":str(new_user_id),
        "username": new_user["username"],
        "bp": new_user.get("bp", ""),
        "diabetes": new_user.get("diabetes", ""),
        "height": new_user.get("height", ""),
        "weight": new_user.get("weight", ""),
        "age": new_user.get("age", "")
    }
    patients_record_table.put_item(Item=patient_record)
    return jsonify({"message": "User created", "payload": new_user}), 200


def user_login(login_user):
    # Retrieve login credentials from the request
    username = login_user.get("username")
    password = login_user.get("password")

    # Find the user record by username
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM patients WHERE username = %s", (username,))
    user = cursor.fetchone()

    # Check if the user is not found
    if user is None:
        cursor.close()
        return jsonify({"message": "User not found"}), 404

    # Check if the password is invalid
    if user["password"] != password:
        cursor.close()
        return jsonify({"message": "Invalid password"}), 401

    # Close the cursor
    cursor.close()
    move_expired_appointments(user["patient_id"])
    # If the credentials are valid, return success message and user's information
    return jsonify({"message": "login success", "user": user}), 200

def get_user_by_username(username):
    # Find the doctor record by username
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM patients WHERE username = %s", (username,))
    doctor = cursor.fetchone()

    # Check if the doctor is not found
    if doctor is None:
        cursor.close()
        return jsonify({"message": "User not found"}), 404

    # Close the cursor
    cursor.close()

    # If the doctor is found, return success message and doctor's information
    return jsonify({"message": "User found", "payload": doctor}), 200
def upcoming_appointments(data):
    try:
        cursor = conn.cursor(dictionary=True)
        current_date = datetime.now()
        cursor.execute("SELECT a.appointment_time as appointmentTime, d.name AS doctorName, d.specialization as doctorSpecialization FROM appointments a\
            INNER JOIN doctors d ON a.doctor_id = d.doctor_id\
            WHERE a.patient_id = %s AND a.appointment_time >= NOW()", (data["_value"]['patient_id'], ))
        appointments = cursor.fetchall()
        cursor.close()
        for appointment in appointments:
            appointment['appointmentDate'] = appointment['appointmentTime'].strftime('%Y-%m-%d')
            appointment['appointmentTime'] = appointment['appointmentTime'].strftime('%H:%M:%S')
        return jsonify(appointments), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

def move_expired_appointments(patient_id):
    current_date = datetime.now()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM appointments WHERE patient_id = %s", (patient_id,))
    appointments = cursor.fetchall()

    for appointment in appointments:
        if appointment["appointment_time"]==None:
            continue
        if appointment['appointment_time'] < current_date:
            # Move the appointment to the appointment history table
            appointment_data = {
                'PatientID': appointment['patient_id'],
                'AppointmentID': appointment['appointment_id'],
                'AppointmentDate': appointment['appointment_time'].strftime('%Y-%m-%d %H:%M:%S'),
                'DoctorID': appointment['doctor_id'],
                'Status':"Completed"
            }
            appointment_history_table.put_item(Item=appointment_data)
            
    cursor.close()

def appointments_history(data):
    cursor = conn.cursor(dictionary=True)
    doctor_info = {}
    response = appointment_history_table.query(
        KeyConditionExpression='PatientID = :pid',
        ExpressionAttributeValues={
            ':pid': data["_value"]['patient_id']
        }
    )
    appointments = response['Items']
    doctor_ids = [appointment['DoctorID'] for appointment in appointments]
    query = "SELECT doctor_id, name, specialization FROM doctors WHERE doctor_id IN (%s)" % ','.join(['%s'] * len(doctor_ids))
    cursor.execute(query, tuple(doctor_ids))
    rows = cursor.fetchall()
        
        # Populate doctor information dictionary
    for row in rows:
        doctor_info[row['doctor_id']] = {
            'name': row['name'],
            'specialization': row['specialization']
        }
    
    # Fetch all rows
    rows = cursor.fetchall()
    
    # Populate doctor information dictionary
    for row in rows:
        doctor_info[row['doctor_id']] = {
            'name': row['name'],
            'specialization': row['specialization']
        }
    for appointment in appointments:
        appointment['appointmentTime'] = appointment['AppointmentDate']
        try:
            appointment['AppointmentDate'] = datetime.strptime(appointment['AppointmentDate'], "%Y-%m-%d %H:%M:%S")

            appointment['appointmentTime'] = appointment['AppointmentDate'].strftime('%H:%M:%S')
            appointment['AppointmentDate'] = appointment['AppointmentDate'].strftime('%Y-%m-%d')
        except:
            pass
        doctor_id = appointment['DoctorID']
        if doctor_id in doctor_info:
            appointment['doctorName'] = doctor_info[doctor_id]['name']
            appointment['doctorSpecialization'] = doctor_info[doctor_id]['specialization']
        else:
            appointment['doctorName'] = 'Unknown'
            appointment['doctorSpecialization'] = 'Unknown'
        
    
    return appointments
def not_found(error):
    return jsonify({"message": f"Path {request.path} is not found"}), 404

def handle_error(error):
    return jsonify({"message": "Error!", "payload": str(error)}), 500
