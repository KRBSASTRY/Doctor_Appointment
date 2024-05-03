import boto3
import mysql.connector
# from secret_keys import *
# dynamo_client  =  boto3.resource(service_name = 'dynamodb',region_name = 'us-east-2',
#               aws_access_key_id = access_key,
#               aws_secret_access_key = secret_access_key)
dynamo_client  =  boto3.resource(service_name = 'dynamodb',region_name = 'us-east-2')
appointment_history_table=dynamo_client.Table("Appointments_History")
patients_record_table=dynamo_client.Table("Patients_Records")

# Initialize MySQL connection
conn = mysql.connector.connect(
    host="database-1.cxg82a6ow0n3.us-east-2.rds.amazonaws.com",
    user="medicare",
    password="Medicare123",
    database="MediCare"
)
