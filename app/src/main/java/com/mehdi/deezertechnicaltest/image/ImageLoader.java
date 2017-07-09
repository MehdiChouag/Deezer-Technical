package com.mehdi.deezertechnicaltest.image;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.widget.ImageView;
import com.mehdi.deezertechnicaltest.BuildConfig;
import com.mehdi.deezertechnicaltest.R;
import com.mehdi.deezertechnicaltest.data.cache.image.ImageCache;
import com.mehdi.deezertechnicaltest.data.exception.NetworkConnectionException;
import com.mehdi.deezertechnicaltest.data.executor.Priority;
import com.mehdi.deezertechnicaltest.data.executor.PriorityRunnable;
import com.mehdi.deezertechnicaltest.data.executor.PriorityThreadPoolExecutor;
import com.mehdi.deezertechnicaltest.data.net.ApiConnection;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.Future;

/**
 * Responsible to poll bitmap either from cache or network and display it in an {@link ImageView}.
 */
public class ImageLoader {

  private static final String TAG = ImageLoader.class.getSimpleName();

  private static final int MESSAGE_SUCCESS = 1;
  private static final int MESSAGE_ERROR = 2;

  private static final int FADE_IN_TIME = 200;

  private static ImageLoader instance;

  private final ImageCache imageCache;
  private final PriorityThreadPoolExecutor executor;
  private final Resources resources;

  private HashMap<String, WeakReference<ImageView>> loadingImage = new HashMap<>();

  private Handler mainThread = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MESSAGE_SUCCESS:
          displayDrawable((ImageMessage) msg.obj);
          break;
        case MESSAGE_ERROR:
          loadingImage.remove((String) msg.obj);
          break;
        default:
          super.handleMessage(msg);
      }
    }
  };

  public static ImageLoader getInstance() {
    if (instance == null) {
      throw new RuntimeException("init() should be call first");
    }
    return instance;
  }

  public static void init(Context context, PriorityThreadPoolExecutor executor,
      ImageCache imageCache) {
    instance = new ImageLoader(context, executor, imageCache);
  }

  private ImageLoader(Context context, PriorityThreadPoolExecutor executor, ImageCache imageCache) {
    this.resources = context.getResources();
    this.executor = executor;
    this.imageCache = imageCache;
  }

  /**
   * Loading asynchronously image.
   *
   * @param url url image.
   * @param imageView ImageView receiving loaded image.
   * @param priority Task priority for async loading.
   * @param loadingPlaceholder true if a loading drawable should use.
   */
  public void loadImage(String url, ImageView imageView, Priority priority,
      boolean loadingPlaceholder) {
    if (url == null || imageView == null) {
      throw new IllegalArgumentException("url and imageView should not be null");
    }

    BitmapDrawable drawable = imageCache.getBitmapFromMemCache(url);
    if (drawable != null) {
      setImageDrawable(imageView, drawable);
    } else {
      cancelOnGoingWork(imageView);
      Future runningTask = executor.submit(new ImageTask(priority, url, mainThread));
      imageView.setTag(R.id.image_loader_tag, runningTask);
      if (loadingPlaceholder) {
        imageView.setImageResource(R.drawable.placeholder);
      }
      loadingImage.put(url, new WeakReference<>(imageView));
    }
  }

  public void loadImage(String url, ImageView imageView) {
    loadImage(url, imageView, Priority.MEDIUM, true);
  }

  /**
   * Cancel any ongoing work associate an ImageView.
   */
  private void cancelOnGoingWork(ImageView view) {
    final Future runningTask = (Future) view.getTag(R.id.image_loader_tag);
    if (runningTask != null && !runningTask.isDone()) {
      runningTask.cancel(true);
    }
  }

  /**
   * Extract data from {@link ImageMessage} to display a bitmap to it's associate {@link ImageView}.
   */
  @MainThread
  private void displayDrawable(ImageMessage message) {
    ImageView imageView = loadingImage.remove(message.getUrl()).get();
    if (imageView != null) {
      imageView.setTag(R.id.image_loader_tag, null);
      setImageDrawable(imageView, message.getBitmapDrawable());
      loadingImage.remove(message.getUrl());
    }
  }

  /**
   * Setting drawable with a cross-faded transition to an ImageView.
   */
  @MainThread
  private void setImageDrawable(ImageView imageView, Drawable drawable) {
    final Drawable previousDrawable = imageView.getDrawable();
    if (previousDrawable != null) {
      TransitionDrawable td = new TransitionDrawable(new Drawable[] {
          previousDrawable, drawable
      });
      imageView.setImageDrawable(td);
      td.startTransition(FADE_IN_TIME);
    } else {
      imageView.setImageDrawable(drawable);
    }
  }

  /**
   * Downloading an {@link InputStream} and transform it into a bitmap.
   *
   * @param urlString Url of bitmap to download.
   */
  @WorkerThread
  private Bitmap downloadBitmap(String urlString) {
    try {
      if (BuildConfig.DEBUG) {
        Log.d(TAG, "Network download bitmap");
      }
      ApiConnection connection = ApiConnection.createGET(urlString);
      return BitmapFactory.decodeStream(connection.call(true));
    } catch (NetworkConnectionException | IOException e) {
      if (BuildConfig.DEBUG && e instanceof NetworkConnectionException) {
        Log.d(TAG, "Network error while downloading bitmap", e);
      }
      return null;
    }
  }

  /**
   * Task in charge to retrieve asynchronously a bitmap either for cloud or disk cache.
   */
  private class ImageTask extends PriorityRunnable {

    private final String url;
    private final Handler handler;

    ImageTask(Priority priority, String url, Handler handler) {
      super(priority);
      this.url = url;
      this.handler = handler;
    }

    @Override
    public void run() {
      if (BuildConfig.DEBUG) {
        Log.d(TAG, "Starting image task for " + url + " with priority : " + priority.name());
      }

      BitmapDrawable drawable;
      Bitmap bitmap = imageCache.getBitmapFromDiskCache(url);
      bitmap = bitmap == null ? downloadBitmap(url) : bitmap;

      if (bitmap != null) {
        drawable = new BitmapDrawable(resources, bitmap);
        imageCache.addBitmapInCache(url, drawable);
        handler.obtainMessage(MESSAGE_SUCCESS, new ImageMessage(drawable, url)).sendToTarget();
      } else {
        handler.obtainMessage(MESSAGE_ERROR, url).sendToTarget();
      }

      if (BuildConfig.DEBUG) {
        Log.d(TAG, "Finish image task for " + url + " with priority : " + priority.name());
      }
    }
  }
}
