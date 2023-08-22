# main_script.py
from encrypted_config_manager import EncryptedConfigHandler

def main(required_creds):
    # Create an instance of the EncryptedConfigManager class
    file_path = 'encrypted_config.dat'  # Assuming you have already stored the encrypted data in this file
    config_manager = EncryptedConfigHandler(file_path)

    # Retrieve the decrypted config data (assuming it's a JSON object)
    decrypted_config = config_manager.get_decrypted_config()

    # Now you can access the decrypted config data as a Python dictionary
    return decrypted_config[required_creds]


if __name__ == "__main__":
    print(main('otp_secret'))
