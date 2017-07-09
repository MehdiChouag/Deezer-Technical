package com.mehdi.deezertechnicaltest.data.cache;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.mehdi.deezertechnicaltest.BuildConfig;
import java.io.File;
import java.io.IOException;

/**
 * Cache for http request.
 */
class HttpCache {

  private static final int HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 10 MiB
  private static final String HTTP_CACHE_FOLDER = "http";

  private final File cacheHttpFolder;

  HttpCache(Context context) {
    this.cacheHttpFolder = new File(context.getCacheDir(), HTTP_CACHE_FOLDER);
  }

  /**
   * Initialize network caching in a background thread.
   */
  @WorkerThread
  void initHttpCache() {
    try {
      HttpResponseCache.install(cacheHttpFolder, HTTP_CACHE_SIZE);
    } catch (IOException e) {
      if (BuildConfig.DEBUG) {
        Log.e("Cache", "Error while initializing http cache");
      }
    }
  }

  /**
   * Clear cache and reinitialize cache as {@link HttpResponseCache#delete()} uninstall cache.
   */
  @WorkerThread
  void clearCache() {
    try {
      HttpResponseCache.getInstalled().delete();
      initHttpCache();
    } catch (IOException e) {
      Log.e("Cache", "Error while deleting http cache");
    }
  }
}
