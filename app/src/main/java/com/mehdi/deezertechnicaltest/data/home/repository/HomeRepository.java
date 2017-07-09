package com.mehdi.deezertechnicaltest.data.home.repository;

import com.mehdi.deezertechnicaltest.data.Callback;
import com.mehdi.deezertechnicaltest.data.home.model.Album;
import java.util.List;

/**
 * Contract used between a presenter and the data implementation of this interface.
 */
public interface HomeRepository {

  /**
   * Fetch album list and communicate back result using {@link Callback}.
   */
  void fetchAlbumList(Callback<List<Album>> callable);

  /**
   * Cancel ongoing task and clear reference.
   */
  void cancel();
}
