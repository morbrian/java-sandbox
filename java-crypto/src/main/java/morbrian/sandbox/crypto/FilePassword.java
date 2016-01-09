package morbrian.sandbox.crypto;/*
* JBoss, the OpenSource J2EE webOS
*
* Distributable under LGPL license.
* See terms of license at gnu.org.
*/

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Read a password in opaque form to a file for use with the FilePassword
 * accessor in conjunction with the JaasSecurityDomain
 * {CLASS}org.jboss.security.plugins.FilePassword:password-file
 * format of the KeyStorePass attribute. The original opaque password file
 * can be created by running:
 * java org.jboss.security.plugins.FilePassword salt count password password-file
 * Running
 * java org.jboss.security.plugins.FilePassword
 * will generate a usage message.
 * <p>
 * Note that this is security by obscurity in that the password is not store
 * in plaintext, but it can be recovered by simply using the code from this
 * class.
 *
 * @author Scott.Stark@jboss.org
 * @version $Revison:$
 * @see #main(String[])
 */
public class FilePassword {
  private File passwordFile;

  public FilePassword(String file) {
    passwordFile = new File(file);
  }

  static char[] decode(RandomAccessFile passwordFile) throws Exception {
    byte[] salt = new byte[8];
    passwordFile.readFully(salt);
    int count = passwordFile.readInt();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int b;
    while ((b = passwordFile.read()) >= 0)
      baos.write(b);
    passwordFile.close();
    byte[] secret = baos.toByteArray();

    PBEParameterSpec cipherSpec = new PBEParameterSpec(salt, count);
    PBEKeySpec keySpec = new PBEKeySpec("78aac249a60a13d5e882927928043ebb".toCharArray());
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEwithMD5andDES");
    SecretKey cipherKey = factory.generateSecret(keySpec);
    Cipher cipher = Cipher.getInstance("PBEwithMD5andDES");
    cipher.init(Cipher.DECRYPT_MODE, cipherKey, cipherSpec);
    byte[] decode = cipher.doFinal(secret);
    return new String(decode, "UTF-8").toCharArray();
  }

  static void encode(RandomAccessFile passwordFile, byte[] salt, int count, byte[] secret)
      throws Exception {
    PBEParameterSpec cipherSpec = new PBEParameterSpec(salt, count);
    PBEKeySpec keySpec = new PBEKeySpec("78aac249a60a13d5e882927928043ebb".toCharArray());
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEwithMD5andDES");
    SecretKey cipherKey = factory.generateSecret(keySpec);
    Cipher cipher = Cipher.getInstance("PBEwithMD5andDES");
    cipher.init(Cipher.ENCRYPT_MODE, cipherKey, cipherSpec);
    byte[] encode = cipher.doFinal(secret);
    System.out.println("ENCODED: " + Base64.encode(encode));
    passwordFile.write(salt);
    passwordFile.writeInt(count);
    passwordFile.write(encode);
    passwordFile.close();
  }

  /**
   * Write a password in opaque form to a file for use with the FilePassword
   * accessor in conjunction with the JaasSecurityDomain
   * {CLASS}org.jboss.security.plugins.FilePassword:password-file
   * format of the KeyStorePass attribute.
   *
   * @param args
   */
  public static void main(String[] args) throws Exception {
    if (args.length != 4) {
      System.err.println(
          "Write a password in opaque form to a file for use with the FilePassword accessor"
              + "Usage: FilePassword salt count password password-file"
              + " salt : an 8 char sequence for PBEKeySpec"
              + " count : iteration count for PBEKeySpec"
              + " password : the clear text password to write"
              + " password-file : the path to the file to write the password to");
    }
    byte[] salt = args[0].substring(0, 8).getBytes();
    int count = Integer.parseInt(args[1]);
    byte[] passwordBytes = args[2].getBytes("UTF-8");
    RandomAccessFile passwordFile = new RandomAccessFile(args[3], "rws");
    encode(passwordFile, salt, count, passwordBytes);
    //char[] fromFile = decode(passwordFile);
    //System.out.println("OUT: " + String.valueOf(fromFile));


  }

  public char[] toCharArray() throws IOException {
    RandomAccessFile raf = new RandomAccessFile(passwordFile, "rws");
    try {
      char[] password = decode(raf);
      return password;
    } catch (Exception e) {
      Logger log = Logger.getLogger(FilePassword.class.getCanonicalName());
      log.log(Level.INFO, "Failed to decode password file: " + passwordFile, e);
      throw new IOException(e.getMessage());
    }
  }
}
