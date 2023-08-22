# main_script.py
import sys
import pprint
sys.path.append(r"C:\Users\RIGNA12\Desktop\Coding Files\progression_loader_automation\Python\contract_ratification\manage_grades")
from credential.encrypted_config_manager import EncryptedConfigHandler

def creds(required_creds):
    # Create an instance of the EncryptedConfigManager class
    file_path = r"C:\Users\RIGNA12\Desktop\Coding Files\progression_loader_automation\Python\contract_ratification\manage_grades\credential\encrypted_config.dat"  # Assuming you have already stored the encrypted data in this file
    config_manager = EncryptedConfigHandler(file_path)

    # Retrieve the decrypted config data (assuming it's a JSON object)
    decrypted_config = config_manager.get_decrypted_config()

    # Now you can access the decrypted config data as a Python dictionary
    return decrypted_config[required_creds]


if __name__ == "__main__":
    print(creds('otp_secret'))