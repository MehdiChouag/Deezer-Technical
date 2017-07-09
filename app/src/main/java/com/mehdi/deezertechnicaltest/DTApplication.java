package com.mehdi.deezertechnicaltest;

import android.app.Application;
import com.mehdi.deezertechnicaltest.data.cache.CacheManager;
import com.mehdi.deezertechnicaltest.image.ImageLoader;

/**
 * Application class use to initialize singleton component.
 */
public class DTApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Injection.init();
    CacheManager.init(this);
    ImageLoader.init(this, Injection.getInstance().getBackgroundExecutor(),
        CacheManager.getInstance().getImageCache());
  }
}
