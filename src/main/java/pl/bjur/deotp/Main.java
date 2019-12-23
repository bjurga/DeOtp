package pl.bjur.deotp;

import pl.bjur.deotp.otp.OtpProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {

    private static final String CONFIG_FILE_PATH = System.getProperty("user.home") + "\\.deotp\\";
    private static final String CONFIG_FILE_NAME = "secrets.conf";
    private static final String CONFIG_FILE_FULL_PATH = CONFIG_FILE_PATH + CONFIG_FILE_NAME;

    private static final String SECRETS_CONFIG_PREFIX = "deotp.secret.";

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            OtpProvider otpProvider = OtpProvider.of(getSecretsMap().get(args[0]));
            System.out.println(otpProvider.getCode());
        } else if (args.length == 2) {
            //TODO: save entry in config if not exists
            ;
        } else {
            System.out.println();
            System.out.println("Usage:");
            System.out.println("deotp your_key -> returns current TOTP code for secret saved as deotp.secret.your_key");
            System.out.println("deotp your_key your_secret -> saves an entry into the config file deotp.secret.your_key=your_secret");
            System.out.println();
            System.out.println("The config file used is " + CONFIG_FILE_FULL_PATH);
            System.out.println();
            System.out.println("Currently only windows is supported.");
            System.out.println();
            System.out.println("Warning: This soft is not a virus but you probably shouldn't use it.");
            System.out.println();
        }

        //TODO: generate AHK snippets with keys from the map
        //TODO: use #include way
        //TODO: generate bat that is called from AHK
        //TODO: make it accept params

        //TODO: if map empty:
        //TODO: save example file (https://www.mkyong.com/java/java-properties-file-examples/)
        //TODO: display help
    }

    private static Map<String, String> getSecretsMap() {
        Map<String, String> secretMap = new HashMap<>();
        Properties properties = new Properties();

        String configFilePath = CONFIG_FILE_FULL_PATH;
        //String configFilePath = "C:\\Users\\bajurg\\secrets.conf";
        try (FileInputStream configFile = new FileInputStream(configFilePath)) {
            properties.load(configFile);
            for (Map.Entry e : properties.entrySet()) {
                if (e.getKey().toString().toLowerCase().startsWith(SECRETS_CONFIG_PREFIX)) {
                    //extract last part from key
                    secretMap.put(
                            e.getKey().toString().substring(SECRETS_CONFIG_PREFIX.length()),
                            e.getValue().toString());
                }
            }
        } catch (IOException e) {
            //TODO: suppress or hide
            e.printStackTrace();
        }
        return secretMap;
    }


}
