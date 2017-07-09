package com.mehdi.deezertechnicaltest.data.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.WorkerThread;
import com.mehdi.deezertechnicaltest.data.cache.image.ImageCache;

/**
 * Class that will manager all different cache.
 */
public class CacheManager {

  private static final int MESSAGE_CLEAR = 0;
  private static final int MESSAGE_INIT = 1;

  private static CacheManager instance;

  private final HttpCache httpCache;
  private final ImageCache imageCache;

  public static CacheManager getInstance() {
    if (instance == null) {
      throw new RuntimeException("init() should be call first");
    }
    return instance;
  }

  public static void init(Context context) {
    instance = new CacheManager(context);
    instance.initializeCache();
  }

  private CacheManager(Context context) {
    this.httpCache = new HttpCache(context);
    this.imageCache = new ImageCache(context);
  }

  public ImageCache getImageCache() {
    return imageCache;
  }

  /**
   * Start an {@link AsyncTask} that will initialize cache in a worker thread.
   */
  private void initializeCache() {
    new CacheAsyncTask().execute(MESSAGE_INIT);
  }

  /**
   * Start an {@link AsyncTask} that will clear cache in a worker thread.
   */
  public void clearCache() {
    new CacheAsyncTask().execute(MESSAGE_CLEAR);
  }

  @WorkerThread
  private void initDiskCacheInternal() {
    httpCache.initHttpCache();
    imageCache.initImageCache();
  }

  @WorkerThread
  private void clearCacheInternal() {
    httpCache.clearCache();
    imageCache.clearCache();
  }

  private class CacheAsyncTask extends AsyncTask<Object, Void, Void> {
    @Override
    protected Void doInBackground(Object... params) {
      switch ((Integer) params[0]) {
        case MESSAGE_CLEAR:
          clearCacheInternal();
          break;
        case MESSAGE_INIT:
          initDiskCacheInternal();
          break;
      }
      return null;
    }
  }
}
