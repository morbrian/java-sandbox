package morbrian.sandbox.crypto.app;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import morbrian.sandbox.crypto.CryptoUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by hikethru08 on 12/18/15.
 */
public class AppCliPasswordUtil {
  public static boolean NEVER_SYSTEM_EXIT = false;
  private static final int KEY_SIZE = 128;
  private static final String PARAM_PASSWORD = "-P";
  private static final String PARAM_FILE = "-F";
  private static final String PARAM_HELP = "-?";
  private static final String PARAM_DEBUG = "-D";
  private static final Set<String> SUPPORTED_PARAMS
      = new HashSet<>(Arrays.asList(PARAM_PASSWORD, PARAM_FILE, PARAM_HELP, PARAM_DEBUG));

  private String filename;
  private String password;
  private boolean debug = false;

  AppCliPasswordUtil(Map<String,String> parameters) {
    this.password = parameters.get(PARAM_PASSWORD);
    this.filename = parameters.get(PARAM_FILE);
    this.debug = parameters.containsKey(PARAM_DEBUG);
  }

  boolean isDebugEnabled() {
    return debug;
  }

  private String getPassword() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter Password To Be Encrypted: ");
    return scanner.next();
  }

  public void run() throws NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
      NoSuchPaddingException, IllegalBlockSizeException, IOException {
    if (password == null) {
      password = getPassword();
    }

    OutputStream output;
    if (filename == null) {
      output = System.out;
    } else {
      output = new PrintStream(new File(filename));
    }

    String aesKey = CryptoUtil.generateAesKeyString(KEY_SIZE);
    String encryptedPassword = CryptoUtil.encryptAes(password, aesKey, KEY_SIZE);

    Map<String,Object> sample = new HashMap<>();
    sample.put("value", encryptedPassword);
    sample.put("key", aesKey);
    sample.put("keySize", KEY_SIZE);
    sample.put("encrypted", Boolean.TRUE);
    Config config = ConfigFactory.parseMap(sample);

    output.write(config.root().render(ConfigRenderOptions.concise().setFormatted(true)).getBytes());
  }

  private static Map<String,String> processCommandLineParameters(String[] args) {
    Map<String,String> params = new HashMap<>();
    for (String arg : args) {
      if (arg.length() < 2 || !arg.startsWith("-")) {
        throw new IllegalArgumentException("Incorrect parameter format: " + arg);
      }
      String type = arg.substring(0, 2);
      if (!SUPPORTED_PARAMS.contains(type)) {
        throw new IllegalArgumentException("Parameter not supported; " + type);
      }
      String value = arg.substring(2, arg.length());
      params.put(type, value);
    }
    return params;
  }

  public static void main(String[] args) {

    Map<String,String> params = null;

    try {
      params = processCommandLineParameters(args);
    } catch(Exception exc) {
      printHelp();
      exit(1);
      return;
    }

    if (params.containsKey(PARAM_HELP)) {
      printHelp();
      exit(0);
      return;
    }

    AppCliPasswordUtil app = new AppCliPasswordUtil(params);
    try {
      app.run();
    } catch(IOException exc) {
      System.err.println("Failed to write to specified file: " + exc.getMessage());
      if (app.isDebugEnabled()) {
        exc.printStackTrace();
      }
      exit(1);
      return;
    } catch(Exception exc) {
      System.err.println("Failed to encrypt password: " + exc.getMessage());
      if (app.isDebugEnabled()) {
        exc.printStackTrace();
      }
      exit(1);
      return;
    }
  }

  private static void exit(int code) {
    if (!NEVER_SYSTEM_EXIT) {
      System.exit(code);
    }
  }

  private static void printHelp() {
    String message =
        "Program encrypts a password and writes it to a formatted configuration file.\n"
        + "Parameter Syntax:\n"
        + "\tAppcliPasswordUtil [-?] | [-P<password>] [-F<filename>]\n"
        + "\tDefault behavior prompts for password, writes to standard out.\n";

    System.out.println(message);
  }

}
