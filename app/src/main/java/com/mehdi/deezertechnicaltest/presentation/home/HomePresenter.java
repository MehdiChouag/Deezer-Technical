package com.mehdi.deezertechnicaltest.presentation.home;

import com.mehdi.deezertechnicaltest.data.Callback;
import com.mehdi.deezertechnicaltest.data.home.model.Album;
import com.mehdi.deezertechnicaltest.data.home.repository.HomeRepository;
import com.mehdi.deezertechnicaltest.presentation.base.BasePresenter;
import java.util.List;

/**
 * Presenter in charge to retrieve data from {@link HomeRepository} and dispatch it to the UI.
 */
class HomePresenter extends BasePresenter<HomeView> {

  private final HomeRepository repository;

  HomePresenter(HomeRepository repository) {
    this.repository = repository;
  }

  void retrieveAlbums() {
    view.hideRetry();
    view.showLoading();
    repository.fetchAlbumList(new Callback<List<Album>>() {
      @Override
      public void onSuccess(List<Album> data) {
        view.hideLoading();
        view.displayAlbum(data);
      }

      @Override
      public void onFailure(Exception e) {
        view.hideLoading();
        view.showError(e);
        view.showRetry();
      }
    });
  }

  @Override
  public void stop() {
    repository.cancel();
  }
}
