import os
import json
from encrypted_config_manager import EncryptedConfigHandler

# Assuming you have already stored the encrypted data in 'encrypted_config.dat'
file_path = 'encrypted_config.dat'

if not os.path.exists(file_path):
    config_data = {
        "email": "erey118@safeway.com",
        "username": "EREY118",
        "password": "Qkgu_8HCna",
        "peoplesoft_password": "aw6_MPKA7K",
        "otp_secret": "62hnq7fcnbhs5bfr"
    }

    with open(file_path, 'w') as file:
        json.dump(config_data, file, indent=4)

    print("File created successfully.")
else:
    print("File already exists.")

# Create an instance of the EncryptedConfigHandler class
config_handler = EncryptedConfigHandler(file_path)

# Encrypt and save the configuration data
config_handler.encrypt_and_save_config(config_data)



## readme ###
# 1. python store_credentials.py
# 2. python encrypt_file.py
        # create empty file
# 2. python democonfig.py