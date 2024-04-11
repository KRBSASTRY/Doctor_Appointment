CREATE TABLE doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    consultationFee INT NOT NULL,
    city VARCHAR(255) NOT NULL,
    phoneno INT NOT NULL,
    experience INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255) NOT NULL,
    status BIT DEFAULT 1
);

CREATE TABLE patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    phoneno INT NOT NULL,
    age INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    status BIT DEFAULT 1
);

CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    doctor_id INT,
    appointment_time DATETIME NOT NULL,
    status ENUM('confirmed', 'pending', 'canceled') DEFAULT 'pending',
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);



CREATE TABLE doctor_availability (
    availability_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT,
    date DATE,
    slot_time DATETIME,
    status ENUM('available', 'booked') DEFAULT 'available',
    UNIQUE KEY (doctor_id, date, slot_time),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);
