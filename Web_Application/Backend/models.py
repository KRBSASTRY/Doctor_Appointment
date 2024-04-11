
from pymongo import MongoClient

# Connect to MongoDB
client = MongoClient('mongodb://localhost:27017/')  # Update the MongoDB URI as necessary
db = client['doctor_appointment']  # Replace 'your_database_name' with your actual database name

# Define Doctor Collection Schema
doctor_collection = db['doctorcollection']  # Collection name 'doctorcollection'

# Define Doctor Model Schema
doctor_schema = {
    'username': {'type': 'string', 'required': True},
    'password': {'type': 'string', 'required': True},
    'consultationFee': {'type': 'number', 'required': True},
    'city': {'type': 'string', 'required': True},
    'phoneno': {'type': 'number', 'required': True},
    'experience': {'type': 'number', 'required': True},
    'name': {'type': 'string', 'required': True},
    'specialization': {'type': 'string', 'required': True},
    'status': {'type': 'boolean', 'default': True}
}

# Define User Collection Schema
user = db['usercollection']  

# Define Doctor Model Schema
user_schema = {
    'username': {'type': 'string', 'required': True},
    'password': {'type': 'string', 'required': True},
    'email': {'type': 'string', 'required': True},
    'city': {'type': 'string', 'required': True},
    'phoneno': {'type': 'number', 'required': True},
    'age': {'type': 'string', 'required': True},
    'name': {'type': 'string', 'required': True},
    'status': {'type': 'boolean', 'default': True}
}

