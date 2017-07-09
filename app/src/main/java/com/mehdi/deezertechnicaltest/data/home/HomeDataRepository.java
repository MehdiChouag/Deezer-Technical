package com.mehdi.deezertechnicaltest.data.home;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.mehdi.deezertechnicaltest.data.Callback;
import com.mehdi.deezertechnicaltest.data.executor.PriorityThreadPoolExecutor;
import com.mehdi.deezertechnicaltest.data.home.model.Album;
import com.mehdi.deezertechnicaltest.data.home.repository.HomeRepository;
import com.mehdi.deezertechnicaltest.data.home.service.HomeService;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Implementation of {@link HomeRepository}.
 */
public class HomeDataRepository implements HomeRepository {

  private final PriorityThreadPoolExecutor executor;
  private final HomeTask homeTask;

  private Callback<List<Album>> callback;

  private Future task;

  @SuppressWarnings("unchecked")
  public HomeDataRepository(PriorityThreadPoolExecutor executor, HomeService homeService) {
    this.executor = executor;
    this.homeTask = new HomeTask(mainThreadHandler, homeService);
  }

  private Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(Message msg) {
      final HomeMessage homeMessage = (HomeMessage) msg.obj;
      switch (msg.what) {
        case HomeTask.TASK_COMPLETE:
          if (callback != null) {
            callback.onSuccess(homeMessage.getAlbumList());
          }
          break;
        case HomeTask.TASK_ERROR:
          if (callback != null) {
            callback.onFailure(homeMessage.getException());
          }
          break;
        default:
          super.handleMessage(msg);
      }
    }
  };

  /**
   * Starting fetching task in a background thread.
   */
  @Override
  public void fetchAlbumList(Callback<List<Album>> callback) {
    this.callback = callback;
    task = executor.submit(homeTask);
  }

  @Override
  public void cancel() {
    if (task != null && !task.isCancelled() && !task.isDone()) {
      task.cancel(true);
    }
    callback = null;
  }
}
