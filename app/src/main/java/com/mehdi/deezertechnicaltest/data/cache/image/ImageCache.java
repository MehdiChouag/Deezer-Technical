package com.mehdi.deezertechnicaltest.data.cache.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.util.LruCache;
import com.mehdi.deezertechnicaltest.BuildConfig;

/**
 * This class handles disk and memory caching of bitmaps.
 */
public class ImageCache {
  private static final String TAG = ImageCache.class.getSimpleName();

  private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; // 5MB

  private final DiskImageCache diskCache;
  private LruCache<String, BitmapDrawable> memoryCache;

  public ImageCache(Context context) {
    diskCache = new DiskImageCache(context);
  }

  /**
   * Initialize both memory and disk cache.
   */
  @WorkerThread
  public void initImageCache() {
    diskCache.initDiskCache();
    memoryCache = new LruCache<String, BitmapDrawable>(DEFAULT_MEM_CACHE_SIZE) {
      @Override
      protected int sizeOf(String key, BitmapDrawable value) {
        return value.getBitmap().getByteCount() / 1024;
      }
    };
  }

  /**
   * Return bitmap drawable associate to a key if found in memory cache, null otherwise.
   */
  public BitmapDrawable getBitmapFromMemCache(String key) {
    BitmapDrawable memValue = null;

    if (memoryCache != null) {
      memValue = memoryCache.get(key);
    }
    if (BuildConfig.DEBUG && memValue != null) {
      Log.d(TAG, "Memory cache hit");
    }
    return memValue;
  }

  /**
   * Return bitmap associate to a key if found in disk cache, null otherwise.
   */
  @WorkerThread
  public Bitmap getBitmapFromDiskCache(String key) {
    return diskCache.get(key);
  }

  /**
   * Add bitmap in both cache.
   */
  @WorkerThread
  public void addBitmapInCache(String key, BitmapDrawable bitmap) {
    if (key != null && bitmap != null) {
      memoryCache.put(key, bitmap);
      diskCache.put(key, bitmap.getBitmap());
    }
  }

  /**
   * Clear all image cached both in memory and disk.
   */
  @WorkerThread
  public void clearCache() {
    memoryCache.evictAll();
    diskCache.clearCache();
  }
}
