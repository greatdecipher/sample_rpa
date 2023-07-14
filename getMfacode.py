# this script must take in 1 argument which is the secret key of the 
# account whose MFA code you want to retrieve.
from config import creds
import pyotp


def mfa_code(code):
    totp = pyotp.TOTP(code)
    return totp.now()


if __name__=="__main__":
    print(mfa_code(creds['gen_key']))
