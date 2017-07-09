package com.mehdi.deezertechnicaltest.data.home;

import com.mehdi.deezertechnicaltest.data.home.model.Album;
import java.util.List;

/**
 * POJO class used to communicate between background and main thread.
 */
class HomeMessage {

  private List<Album> albumList;
  private Exception exception;

  List<Album> getAlbumList() {
    return albumList;
  }

  void setAlbumList(List<Album> albumList) {
    this.albumList = albumList;
  }

  public Exception getException() {
    return exception;
  }

  public void setException(Exception exception) {
    this.exception = exception;
  }
}
