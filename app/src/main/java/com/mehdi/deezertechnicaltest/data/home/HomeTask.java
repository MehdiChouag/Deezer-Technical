package com.mehdi.deezertechnicaltest.data.home;

import android.os.Handler;
import com.mehdi.deezertechnicaltest.data.exception.NetworkConnectionException;
import com.mehdi.deezertechnicaltest.data.exception.ParsingException;
import com.mehdi.deezertechnicaltest.data.executor.Priority;
import com.mehdi.deezertechnicaltest.data.executor.PriorityRunnable;
import com.mehdi.deezertechnicaltest.data.home.model.Album;
import com.mehdi.deezertechnicaltest.data.home.service.HomeService;
import java.util.List;

/**
 * Class in charge to retrieve home data whatever it's source.
 */
class HomeTask extends PriorityRunnable {

  static final int TASK_COMPLETE = 1;
  static final int TASK_ERROR = 2;

  private final Handler handler;
  private final HomeService homeService;
  private final HomeMessage message;

  HomeTask(Handler handler, HomeService homeService) {
    super(Priority.MEDIUM);
    this.handler = handler;
    this.homeService = homeService;
    this.message = new HomeMessage();
  }

  /**
   * Fetch data from {@link HomeService#fetchAlbums()} that comes either from cloud or cache, then
   * parse data and send result back to the main thread.
   */
  @Override
  public void run() {
    try {
      final List<Album> album = homeService.fetchAlbums();
      message.setAlbumList(album);
      handler.obtainMessage(TASK_COMPLETE, message).sendToTarget();
    } catch (NetworkConnectionException | ParsingException e) {
      message.setException(e);
      handler.obtainMessage(TASK_ERROR, message).sendToTarget();
    }
  }
}
