import sys
import win32cred
from cryptography.fernet import Fernet

def store_fernet_key(key=None):
    if key is None:
        #Generate a Fernet Key if not provided
        fernet_key = Fernet.generate_key()
        print(f"GENARETED KEY:  {fernet_key}")
    else:
        fernet_key = key.encode('utf-16-le')

    # Convert the fernet_key to a UTF-16 little-endian string
    fernet_key_str = fernet_key.decode('utf-16-le')

    # Store the fernet_key in Windows Vault
    credential = {
        'TargetName': 'Contract Ratification',
        'Type': win32cred.CRED_TYPE_GENERIC,
        'CredentialBlob': fernet_key_str,
        'Persist': win32cred.CRED_PERSIST_LOCAL_MACHINE,  
    }
    win32cred.CredWrite(credential)

if __name__ == "__main__":
    if len(sys.argv) > 1:
        provided_key = sys.argv[1]
        store_fernet_key(provided_key)
        print("Fernet key stored successfully.")
    else:
        store_fernet_key()
        print("Generated and stored a new Fernet key.")