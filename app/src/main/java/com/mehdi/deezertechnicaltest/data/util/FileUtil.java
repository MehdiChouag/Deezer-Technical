package com.mehdi.deezertechnicaltest.data.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Util class related to {@link File} interaction.
 */
public final class FileUtil {

  private FileUtil() {
  }

  /**
   * Close and ignored exception raised.
   */
  public static void closeSilently(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException ignored) {
      }
    }
  }

  /**
   * Delete recursively all file of a folder.
   */
  public static void deleteFolder(File f) {
    if (f.isDirectory()) {
      for (File c : f.listFiles())
        deleteFolder(c);
    }
    f.delete();
  }

  /**
   * Returns a suitable MD5 hash using a key.
   */
  public static String hashKeyForDisk(String key) {
    String cacheKey;
    try {
      final MessageDigest mDigest = MessageDigest.getInstance("MD5");
      mDigest.update(key.getBytes());
      cacheKey = bytesToHexString(mDigest.digest());
    } catch (NoSuchAlgorithmException e) {
      cacheKey = String.valueOf(key.hashCode());
    }
    return cacheKey;
  }

  private static String bytesToHexString(byte[] bytes) {
    // http://stackoverflow.com/questions/332079
    StringBuilder sb = new StringBuilder();
    for (byte aByte : bytes) {
      String hex = Integer.toHexString(0xFF & aByte);
      if (hex.length() == 1) {
        sb.append('0');
      }
      sb.append(hex);
    }
    return sb.toString();
  }
}
