package com.mehdi.deezertechnicaltest.data.cache.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.mehdi.deezertechnicaltest.BuildConfig;
import com.mehdi.deezertechnicaltest.data.util.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class in charge to persist bitmap in cache.
 */
class DiskImageCache {

  private static final String TAG = DiskImageCache.class.getSimpleName();

  private static final String DISK_CACHE_FOLDER = "img";

  private static final Bitmap.CompressFormat DISK_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
  private static final int DISK_COMPRESS_QUALITY = 85;

  private final File cacheDir;

  DiskImageCache(Context context) {
    this.cacheDir = new File(context.getCacheDir(), DISK_CACHE_FOLDER);
  }

  /**
   * Initialize disk cache by creating cache directory if necessary.
   */
  @WorkerThread
  synchronized void initDiskCache() {
    if (!cacheDir.exists()) {
      cacheDir.mkdir();
    }
  }

  /**
   * Save bitmap in disk under cache directory.
   */
  @WorkerThread
  synchronized void put(String key, Bitmap bitmap) {
    File save = new File(cacheDir, FileUtil.hashKeyForDisk(key));

    try {
      FileOutputStream fos = new FileOutputStream(save);
      bitmap.compress(DISK_COMPRESS_FORMAT, DISK_COMPRESS_QUALITY, fos);
      fos.flush();
      fos.close();
    } catch (IOException e) {
      if (BuildConfig.DEBUG) {
        Log.e(TAG, "Error while saving bitmap to cache", e);
      }
    }
  }

  /**
   * Retrieve bitmap from cache if present or null.
   */
  @WorkerThread
  synchronized Bitmap get(String key) {
    File bitmapFile = new File(cacheDir, FileUtil.hashKeyForDisk(key));
    if (bitmapFile.exists()) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inPreferredConfig = Bitmap.Config.ARGB_8888;
      try {
        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(bitmapFile), null, options);
        if (BuildConfig.DEBUG && bitmap != null) {
          Log.d(TAG, "Disk cache hit");
        }
        return bitmap;
      } catch (FileNotFoundException e) {
        return null;
      }
    }
    return null;
  }

  /**
   * Delete everything under cache directory.
   */
  @WorkerThread
  synchronized void clearCache() {
    FileUtil.deleteFolder(cacheDir);
    initDiskCache();
  }
}
