package pl.bjur.deotp.otp;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class OtpProvider {

    private static final int PIN_LENGTH = 6; // HOTP or TOTP
    public static final int DEFAULT_INTERVAL = 30;
    private final String otpSecret;

    public String getCode() throws Exception {
        long otpState = 0;
        // For time-based OTP, the state is derived from clock.
        otpState = mTotpCounter.getValueAtTime(currentUnixTime());

        String result = computePin(otpSecret, otpState);
        return result;
    }

    private OtpProvider(String secret) {
        this.otpSecret = secret;
    }

    public static OtpProvider of(String secret) {
        return new OtpProvider(secret);
    }

    /**
     * Computes the one-time PIN given the secret key.
     *
     * @param secret   the secret key
     * @param otpState current token state (counter or time-interval)
     * @return the PIN
     */
    private String computePin(String secret, long otpState)
            throws Exception {
        if (secret == null || secret.length() == 0) {
            throw new Exception("Null or empty secret");
        }
        //System.out.println("OTPState: "+otpState);
        try {
            PasscodeGenerator.Signer signer = getSigningOracle(secret);
            PasscodeGenerator pcg = new PasscodeGenerator(signer, PIN_LENGTH);

            return pcg.generateResponseCode(otpState);
        } catch (GeneralSecurityException e) {
            throw new Exception("Crypto failure", e);
        }
    }

    private static PasscodeGenerator.Signer getSigningOracle(String secret) {
        try {
            byte[] keyBytes = decodeKey(secret);
            final Mac mac = Mac.getInstance("HMACSHA1");
            mac.init(new SecretKeySpec(keyBytes, ""));

            // Create a signer object out of the standard Java MAC implementation.
            return new PasscodeGenerator.Signer() {
                //@Override
                public byte[] sign(byte[] data) {
                    return mac.doFinal(data);
                }
            };
        } catch (Base32String.DecodingException
                | NoSuchAlgorithmException
                | InvalidKeyException
                | IllegalArgumentException error) {
            System.out.println(error.getMessage());
        }

        return null;
    }

    private static byte[] decodeKey(String secret) throws Base32String.DecodingException {
        return Base32String.decode(secret);
    }

    String getSecret() {
        return otpSecret;
    }

    /**
     * Counter for time-based OTPs (TOTP).
     */
    private final TotpCounter mTotpCounter = new TotpCounter(DEFAULT_INTERVAL);
    ;

    private long currentUnixTime() {
        //long l = Calendar.getInstance().get(Calendar.MILLISECOND);
        long l = System.currentTimeMillis() / 1000L;
        //System.out.println("currentUnixTime: " + l);
        return l;
    }
}
