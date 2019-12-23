package pl.bjur.deotp.otp;


import com.google.common.collect.Maps;

import java.util.Locale;
import java.util.Map;

public class Base32String {
    private static final String SEPARATOR = "-";
    private static final char[] DIGITS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray();
    private static final int MASK = DIGITS.length - 1;
    private static final int SHIFT = Integer.numberOfTrailingZeros(DIGITS.length);
    private static final Map<Character, Integer> CHAR_MAP =
            Maps.newHashMapWithExpectedSize(DIGITS.length);

    static {
        for (int i = 0; i < DIGITS.length; i++) {
            CHAR_MAP.put(DIGITS[i], i);
        }
    }

    public static byte[] decode(String encoded) throws DecodingException {
        // Remove whitespace and separators
        encoded = encoded.trim().replaceAll(SEPARATOR, "").replaceAll(" ", "");

        // Remove padding. Note: the padding is used as hint to determine how many
        // bits to decode from the last incomplete chunk (which is commented out
        // below, so this may have been wrong to start with).
        encoded = encoded.replaceFirst("[=]*$", "");

        // Canonicalize to all upper case
        encoded = encoded.toUpperCase(Locale.US);
        if (encoded.length() == 0) {
            return new byte[0];
        }
        int encodedLength = encoded.length();
        int outLength = encodedLength * SHIFT / 8;
        byte[] result = new byte[outLength];
        int buffer = 0;
        int next = 0;
        int bitsLeft = 0;
        for (char c : encoded.toCharArray()) {
            if (!CHAR_MAP.containsKey(c)) {
                throw new DecodingException("Illegal character: " + c);
            }
            buffer <<= SHIFT;
            buffer |= CHAR_MAP.get(c) & MASK;
            bitsLeft += SHIFT;
            if (bitsLeft >= 8) {
                result[next++] = (byte) (buffer >> (bitsLeft - 8));
                bitsLeft -= 8;
            }
        }
        // We'll ignore leftover bits for now.
        //
        // if (next != outLength || bitsLeft >= SHIFT) {
        //  throw new DecodingException("Bits left: " + bitsLeft);
        // }
        return result;
    }

    /**
     * Exception thrown when decoding fails
     */
    public static class DecodingException extends Exception {
        public DecodingException(String message) {
            super(message);
        }
    }
}