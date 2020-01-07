# DeOtp &nbsp; &nbsp; <a href="https://github.com/bjurga/DeOtp/actions"><img alt="GitHub Actions status" src="https://github.com/bjurga/DeOtp/workflows/Test%26Build/badge.svg"></a> &nbsp; &nbsp; 
When you'r forced to use OTP and session is expiring quickly - DeOtp to the rescue - highly insecure stuff!
In other words: DeOtp is a command line OTP code generator.

# Story
You are working in a company that protects it's internal system with OTP. You are using in often, but not frequent enough to keep the quick expiring session alive. You are feeling like wasting your time on retyping codes from your smartphone.
DeOTP is for you. It allows you to generate the code on your PC and copy-paste it. Details below - keep reading...

# inSecurity note
By using this software you are literally raping your company policy about 2FA/MFA/OTP codes. I will not explain the threats in details. I assume you know what yor are doing.

# DeOTP code is based on [opensourced Google Authenticator app](https://github.com/google/google-authenticator). 
Changes to the original app are:
- I extracted only the part responsible for getting the TOTP code.
- Android specific stuff is wiped.
- most of original app is not used.

# How to use:
1. For ease of use you will need additional soft which will run DeOTP underneath and paste the result in appropriate place. I'm using [AHK](https://www.autohotkey.com/). Quick ToDo list:
- download and install AHK. 
- make it autorun with the OS.
- check is it's working

2. DeOTP is keeping your secrets in %userhome%/.deotp/deotp.conf file. //TODO: commands examples
- run "gradle go". it will generate required files into "build" directory.
- each entry has following format: deotp.secret.your_key=your_otp_secret_string
- when you run DeOTP with one argument it is treated as a request for TOTP code for a given your_key.
- use 2 arguments to save your_key and your_secret in the config.
- otherwise it will show usage help

3. AHK tips:
- hotstrings
- //TODO: windows apps (window searching + auto-paste)
- //TODO: browser (image searching + auto-paste)
