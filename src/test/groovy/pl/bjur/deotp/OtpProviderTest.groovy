package pl.bjur.deotp

import pl.bjur.deotp.otp.OtpProvider
import spock.lang.Specification

import java.util.concurrent.TimeUnit

class OtpProviderTest extends Specification {

    private static final String A_SECRET = "ABCDEFGHIJK";

    def "getCode returns a 6-digit code"() {
        given:
        OtpProvider otpProvider = OtpProvider.of(A_SECRET);

        when:
        String result = otpProvider.getCode();

        then:
        result.length() == 6
    }

    def "generated codes are different after 31seconds"() {
        given:
        OtpProvider otpProvider = OtpProvider.of(A_SECRET);

        when:
        String result = otpProvider.getCode();
        TimeUnit.SECONDS.sleep(31) //YESSS!! My test are fu...ing fast!! //TODO: refactor to be able to inject time
        String result2 = otpProvider.getCode();

        then:
        result != result2
    }

    def "generated codes are different for different secrets"() {
        given:
        OtpProvider otpProvider = OtpProvider.of(A_SECRET);
        OtpProvider otpProvider2 = OtpProvider.of(A_SECRET + "a");

        when:
        String result = otpProvider.getCode();
        String result2 = otpProvider2.getCode();

        then:
        result != result2
    }
}
