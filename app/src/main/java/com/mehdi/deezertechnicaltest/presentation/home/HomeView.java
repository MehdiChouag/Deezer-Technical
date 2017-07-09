package com.mehdi.deezertechnicaltest.presentation.home;

import com.mehdi.deezertechnicaltest.data.home.model.Album;
import com.mehdi.deezertechnicaltest.presentation.base.LoadDataView;
import java.util.List;

/**
 * View contract for home screen.
 */
interface HomeView extends LoadDataView {
  void displayAlbum(List<Album> albums);
}
