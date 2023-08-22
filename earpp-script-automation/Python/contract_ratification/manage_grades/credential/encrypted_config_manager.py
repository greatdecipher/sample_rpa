import win32cred
from cryptography.fernet import Fernet
import json

class EncryptedConfigHandler:
    def __init__(self, file_path):
        self.file_path = file_path

    def _read_encrypted_data(self):
        try:
            with open(self.file_path, 'rb') as f:
                return f.read()
        except FileNotFoundError:
            raise FileNotFoundError(f"The file {self.file_path} does not exist.")
        
    def _write_encrypted_data(self, encrypted_data):
        with open(self.file_path, 'wb') as f:
            f.write(encrypted_data)        

    def get_fernet_key(self):
        encrypted_data = self._read_encrypted_data()
        credential = win32cred.CredRead('Contract Ratification', win32cred.CRED_TYPE_GENERIC)
        fernet_key_str = credential['CredentialBlob']
        #fernet_key = fernet_key_str.encode('utf-16-le')
        return fernet_key_str

    def get_encrypted_key(self):
        fernet_key = self.get_fernet_key()
        cipher = Fernet(fernet_key)

        encrypted_data = self._read_encrypted_data()
        decrypted_data = cipher.decrypt(encrypted_data)
        return decrypted_data

    def get_decrypted_config(self):
        encrypted_key = self.get_encrypted_key()
        config_data = json.loads(encrypted_key)
        return config_data

    def encrypt_and_save_config(self, config_data):
        fernet_key = self.get_fernet_key()
        cipher = Fernet(fernet_key)

        # Serialize the JSON data to bytes
        json_data = json.dumps(config_data).encode()

        # Encrypt the JSON data
        encrypted_data = cipher.encrypt(json_data)

        # Save the encrypted data to the file
        self._write_encrypted_data(encrypted_data)