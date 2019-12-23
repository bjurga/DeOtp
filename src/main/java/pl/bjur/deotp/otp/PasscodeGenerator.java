package pl.bjur.deotp.otp;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;

/**
 * An implementation of the HOTP generator specified by RFC 4226.
 *
 * <p>Generates short passcodes that may be used in challenge-response protocols or as timeout
 * passcodes that are only valid for a short period.
 *
 * <p>The default passcode is a 6-digit decimal code. The maximum passcode length is 9 digits.
 */
public class PasscodeGenerator {
    /**
     * Maximum passcode length, in digits. Must be kept in sync with
     * {@link #DIGITS_POWER}.
     */
    private static final int MAX_PASSCODE_LENGTH = 9;

    /**
     * Powers of 10 to shorten the pin to the desired number of digits. This
     * prevents invalid OTP generation when Math.pow() is implemented incorrectly
     * (e.g. when 10^6 != 1000000), and matches the reference implementation in
     * RFC 6238. Must be kept in sync with {@link #MAX_PASSCODE_LENGTH}.
     */
    private static final int[] DIGITS_POWER =
            {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

    private final Signer signer;
    private final int codeLength;

    /**
     * Using an interface to allow us to inject different signature
     * implementations.
     */
    interface Signer {
        /**
         * @param data Preimage to sign, represented as sequence of arbitrary bytes
         * @return Signature as sequence of bytes.
         * @throws GeneralSecurityException
         */
        byte[] sign(byte[] data) throws GeneralSecurityException;
    }

    public PasscodeGenerator(Signer signer, int passCodeLength) {
        if ((passCodeLength < 0) || (passCodeLength > MAX_PASSCODE_LENGTH)) {
            throw new IllegalArgumentException(
                    "PassCodeLength must be between 1 and " + MAX_PASSCODE_LENGTH + " digits.");
        }
        this.signer = signer;
        this.codeLength = passCodeLength;
    }

    private String padOutput(int value) {
        String result = Integer.toString(value);
        for (int i = result.length(); i < codeLength; i++) {
            result = "0" + result;
        }
        return result;
    }

    /**
     * @param state 8-byte integer value representing internal OTP state.
     * @return A decimal response code
     * @throws GeneralSecurityException If a JCE exception occur
     */
    public String generateResponseCode(long state) throws GeneralSecurityException {
        byte[] value = ByteBuffer.allocate(8).putLong(state).array();
        return generateResponseCode(value);
    }

    /**
     * @param challenge An arbitrary byte array used as a challenge
     * @return A decimal response code
     * @throws GeneralSecurityException If a JCE exception occur
     */
    public String generateResponseCode(byte[] challenge)
            throws GeneralSecurityException {
        byte[] hash = signer.sign(challenge);

        // Dynamically truncate the hash
        // OffsetBits are the low order bits of the last byte of the hash
        int offset = hash[hash.length - 1] & 0xF;
        // Grab a positive integer value starting at the given offset.
        int truncatedHash = hashToInt(hash, offset) & 0x7FFFFFFF;
        int pinValue = truncatedHash % DIGITS_POWER[codeLength];
        return padOutput(pinValue);
    }

    /**
     * Grabs a positive integer value from the input array starting at
     * the given offset.
     *
     * @param bytes the array of bytes
     * @param start the index into the array to start grabbing bytes
     * @return the integer constructed from the four bytes in the array
     */
    private int hashToInt(byte[] bytes, int start) {
        DataInput input = new DataInputStream(
                new ByteArrayInputStream(bytes, start, bytes.length - start));
        int val;
        try {
            val = input.readInt();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return val;
    }
}
