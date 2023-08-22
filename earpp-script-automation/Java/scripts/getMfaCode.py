# This script must take in 1 argument which is the secret key of the
# account whose MFA code you want to retrieve.

import sys
import pyotp

totp = pyotp.TOTP(sys.argv[1])
print(totp.now())
