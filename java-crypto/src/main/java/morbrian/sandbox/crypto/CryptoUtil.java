package morbrian.sandbox.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

public class CryptoUtil {

  public static final String UTF8 = "UTF-8";
  public static final String ALGORITHM_AES = "AES";
  public static final String TRANSFORMATION_AES = "AES/CBC/PKCS5Padding";
  public static final int INIT_VECTOR_SIZE_AES_CBC = 16;
  public static final int MIN_TEXT_LENGTH = 8;

  /**
   * Encrypts the given bytes using AES and the given key.
   */
  public static byte[] encryptAes(byte[] bytes, SecretKey key)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException {
    Cipher cipher = aesCipher(Cipher.ENCRYPT_MODE, key);
    byte[] cipherText = cipher.doFinal(bytes);
    byte[] initializationVector = cipher.getIV();
    ByteBuffer bb = ByteBuffer.allocate(cipherText.length + initializationVector.length);
    return bb.put(initializationVector).put(cipherText).array();
  }

  /**
   * Encrypts the given string using AES and the given key.
   * @param plainText The plaintext to encrypt.
   * @param key The key to use for encrypting.
   * @param keySize The size of the provided `key`.
   */
  public static String encryptAes(String plainText, String key, int keySize)
      throws UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeyException,
      BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
    return DatatypeConverter.printHexBinary(encryptAes(plainText.getBytes(UTF8),
        new SecretKeySpec(DatatypeConverter.parseHexBinary(key), ALGORITHM_AES)));
  }

  /**
   * Generates an AES key of the given size.
   */
  public static SecretKey generateAesKey(int size) throws NoSuchAlgorithmException {
    KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM_AES);
    keyGen.init(size);
    return keyGen.generateKey();
  }

  /**
   * Generates an AES key of the given size and returns it as a hexadecimal string.
   */
 public static String generateAesKeyString(int size) throws NoSuchAlgorithmException {
    return DatatypeConverter.printHexBinary(generateAesKey(size).getEncoded());
  }

  /**
   * Decrypts the given bytes using AES and the given key. The first 16 bytes must be the initialization vector.
   */
  public static byte[] decryptAes(byte[] bytes, SecretKey key)
      throws BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException,
      NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
    byte[] initializationVector = getAesInitializationVector(bytes);
    byte[] cipherText = getAesCipherText(bytes);
    Cipher cipher = aesCipher(Cipher.DECRYPT_MODE, key, new IvParameterSpec(initializationVector));
    return cipher.doFinal(cipherText);
  }

  /**
   * Decrypts the given string using AES and the given key.
   * @param encrypted A concatenation of the initialization vector and the cipher text to be decrypted.
   *                  The first 16 bytes must be the initialization vector.
   * @param key The key to use for decrypting.
   * @param keySize (_optional_) The size of the provided `key`. If omitted, it's assumed to be 256 bits.
   */
  public static String decryptAes(String encrypted, String key, int keySize)
      throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException,
      IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException,
      UnsupportedEncodingException {
    byte[] bytes = decryptAes(hex2bytes(encrypted), new SecretKeySpec(DatatypeConverter.parseHexBinary(key), ALGORITHM_AES));
    return new String(bytes, UTF8);
  }

  /**
   * Returns the initialization vector portion of the given byte array (the first 16 bytes).
   */
  private static byte[] getAesInitializationVector(byte[] bytes) {
    assert(bytes.length > INIT_VECTOR_SIZE_AES_CBC);
    return Arrays.copyOf(bytes, INIT_VECTOR_SIZE_AES_CBC);
  }

  /**
   * Returns the cipher text portion of the given byte array (everything after the first 16 bytes).
   */
  private static byte[] getAesCipherText(byte[] bytes) {
    assert(bytes.length >= INIT_VECTOR_SIZE_AES_CBC + MIN_TEXT_LENGTH);
    return Arrays.copyOfRange(bytes, INIT_VECTOR_SIZE_AES_CBC, bytes.length);
  }

  /*  Returns a byte array representation of the given hex string.
 */
  private static byte[] hex2bytes(String hex) {
    int len = hex.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
          + Character.digit(hex.charAt(i+1), 16));
    }
    return data;
  }

  /**
   * Creates an AES `Cipher` initialized with the given mode (such as `Cipher.ENCRYPT_MODE`) and key.
   */
  private static Cipher aesCipher(int opMode, SecretKey key)
      throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
    Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES);
    cipher.init(opMode, key);
    return cipher;
  }

  /**
   * Creates an AES `Cipher` initialized with the given mode (such as `Cipher.ENCRYPT_MODE`), key, and
   * parameter spec (such as the initialization vector).
   */
  private static Cipher aesCipher(int opMode, SecretKey key, AlgorithmParameterSpec spec)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
      InvalidKeyException {
    Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES);
    cipher.init(opMode, key, spec);
    return cipher;
  }

}
