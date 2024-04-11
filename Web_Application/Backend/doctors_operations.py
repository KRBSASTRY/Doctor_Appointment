import boto3
from datetime import datetime
from flask import Flask, request, jsonify
from flask_bcrypt import Bcrypt
import mysql.connector
from Web_Application.AWS_deployment.Key.db_access import *
app = Flask(__name__)
bcrypt = Bcrypt()

def create_doctor(new_doctor):
    # Extract fields from the new_doctor dictionary
    username = new_doctor.get("username")
    name = new_doctor.get("name")
    specialty = new_doctor.get("specialization")
    # Check if the username already exists in the database
    cur = conn.cursor()
    cur.execute("SELECT * FROM doctors WHERE username = %s", (username,))
    doctor = cur.fetchone()
    if doctor:
        cur.close()
        return jsonify({"message": "Username already exists"}), 400
    # Insert the new doctor record into the doctors table
    cur.execute("INSERT INTO doctors (username,password, consultationFee,city, phoneno, experience, name, specialization) VALUES (%s, %s, %s,%s, %s, %s,%s, %s)",
                   (username,new_doctor.get("password"),new_doctor.get("consultationFee"),new_doctor.get("city"), new_doctor.get("phoneno"),new_doctor.get("experience"), name, specialty))
    conn.commit()
    # Get the inserted doctor's ID
    new_doctor_id = cur.lastrowid
    cur.close()
    new_doctor["_id"] = new_doctor_id
    return jsonify({"message": "Doctor created", "payload": new_doctor}), 200


def doctor_login(login_doctor):
    # Retrieve login credentials from the request
    username = login_doctor.get("username")
    password = login_doctor.get("password")
    # Find the doctor record by username
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM doctors WHERE username = %s", (username,))
    doctor = cursor.fetchone()
    # Check if the doctor is not found
    if doctor is None:
        cursor.close()
        return jsonify({"message": "Doctor not found"}), 404
    # Check if the password is invalid
    if doctor["password"] != password:
        cursor.close()
        return jsonify({"message": "Invalid password"}), 401
    # Close the cursor
    cursor.close()
    # If the credentials are valid, return success message and doctor's information
    return jsonify({"message": "login success", "doctor": doctor}), 200

def get_doctors():
    # Execute a SELECT query to retrieve all doctors from the database
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM doctors")
    doctors = cursor.fetchall()
    # Close the cursor
    cursor.close()
    # Return JSON response containing doctors data
    return jsonify({"message": "Doctors data", "payload": doctors}), 200

def get_doctor_by_username(username):
    # Find the doctor record by username
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM doctors WHERE username = %s", (username,))
    doctor = cursor.fetchone()
    # Check if the doctor is not found
    if doctor is None:
        cursor.close()
        return jsonify({"message": "Doctor not found"}), 404
    # Close the cursor
    cursor.close()
    # If the doctor is found, return success message and doctor's information
    return jsonify({"message": "Doctor found", "payload": doctor}), 200

# Function to check slot availability
def check_slot_availability(data):
    doctor_id = data.get('doctor_id')
    date = data.get('date')
    slot_time = data.get('slot')
    date_time_str = f"{date} {slot_time}"
    slot_time = datetime.strptime(date_time_str, '%Y-%m-%d %I %p')
    try:
        con = conn.cursor()
        # Query to check slot availability
        query = """
            SELECT * FROM doctor_availability 
            WHERE doctor_id = %s AND date = %s AND slot_time = %s
        """
        con.execute(query, (doctor_id, date, slot_time))
        result = con.fetchone()
        if result and result[4] != 'available':
            return jsonify({'available': False})
        else:
            return jsonify({'available': True})
    except mysql.connector.Error as e:
        print("MySQL Error:", e)
        return jsonify({'error': str(e)}), 500

    finally:
        if 'cursor' in locals():
            con.close()
        if 'conn' in locals():
            conn.close()

# Function to update appointment status to completed
def update_appointment_status(data):
    appointment_id=data.get('appointment_id')
    try:
        # Connect to MySQL database
        con = conn.cursor()
        query = """
            UPDATE doctor_availability 
            SET status = 'completed' 
            WHERE availability_id = %s
        """
        con.execute(query, (appointment_id,))
        conn.commit()

        return jsonify({'message': 'Appointment status updated successfully'})

    except mysql.connector.Error as e:
        print("MySQL Error:", e)
        return jsonify({'error': str(e)}), 500

    finally:
        if 'cursor' in locals():
            con.close()
        if 'conn' in locals():
            conn.close()


def book_appointment(payload):
    try:
        # Connect to MySQL database
        cursor = conn.cursor()
        # Extract data from payload
        patient_id = payload.get('patient_id')
        doctor_id = payload.get('doctor_id')
        date=payload.get("date")
        appointment_time = payload.get('appointment_time')
        date_time_str = f"{date} {appointment_time}"
        appointment_time = datetime.strptime(date_time_str, '%Y-%m-%d %I %p')

        # Insert appointment data into appointments table
        query = """
            INSERT INTO appointments (patient_id, doctor_id, appointment_time)
            VALUES (%s, %s, %s)
        """
        cursor.execute(query, (patient_id, doctor_id, appointment_time))
        conn.commit()
        appointment_id = cursor.lastrowid

        update_availability_query = """
            INSERT INTO doctor_availability (doctor_id, date, slot_time, status)
            VALUES (%s, %s, %s, 'booked')
            ON DUPLICATE KEY UPDATE status = 'booked';
        """
        cursor.execute(update_availability_query, (doctor_id, date, appointment_time))
        conn.commit()
        appointment_time = appointment_time.isoformat()
        appointment_history_table.put_item(
            Item={
                'PatientID': patient_id,
                'AppointmentID': appointment_id, # Using current timestamp as a unique identifier
                'DoctorID': doctor_id,
                'AppointmentDate': appointment_time,
                'Status': 'booked'
            }
        )
        return True
    # except mysql.connector.Error as e:
    #     print("MySQL Error:", e)
    #     return False
    except Exception as e:
        print(e)

    # finally:
    #     if 'cursor' in locals():
    #         cursor.close()
    #     if 'conn' in locals():
    #         conn.close()

def not_found(error):
    return jsonify({"message": f"Path {request.path} is not found"}), 404

def handle_error(error):
    return jsonify({"message": "Error!", "payload": str(error)}), 500
