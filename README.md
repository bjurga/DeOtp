# DeOtp
When you'r forced to use OTP and session is expiring quickly - DeOtp to the recue - highly insecure stuff!

# Story
You are working in a company that protects it's internal system with OTP. You are using in often, but not frequent enoigh to keep the quick expiring session alive. You are feeling like wasting your time on retyping codes from your smartphone.
DeOTP is for you. It alows you to generate the code on your PC and copy-paste it. Details below - keep reading...

# inSecurity note
By using this software you are litteraly raping your company policy about 2FA/MFA/OTP codes. I will not explain the threats in details. I assume you know what yor are doing.

# DeOTP is based on opensourced Google Authenticator app (link). Changes to the original app are:
- I extracted only the part responsible for getting the TOTP code.
- Android specific stuff is wiped.
- most of original app is not used.

# How to use:
1. For ease of use you will need additional soft which will run DeOTP underneeth and paste the result in appriopiate place. I'm using AHK (link). Quick ToDo list:
- download and intsall AHK. 
- make it autorun with the OS.
- check is it's working

2. DeOTP is keeping your secrets in %userhome%/.deotp/deotp.conf file.
- each entry has following format: deotp.secret.your_key=your_otp_secret_string
- when you run DeOTP with one argumemt it is treated as a request for TOTP code for a given your_key.
- use 2 arguments to save your_key and your_secret in the config. (Examles jere)
- otherwise it will show usage help

3. AHK tips:
- hotstrings
- windows apps )window searchimg)
- browser (image searching)
