package com.mehdi.deezertechnicaltest.data.home.service;

import com.mehdi.deezertechnicaltest.data.exception.NetworkConnectionException;
import com.mehdi.deezertechnicaltest.data.exception.ParsingException;
import com.mehdi.deezertechnicaltest.data.home.model.Album;
import java.util.List;

/**
 * Service use to retrieve data from api.
 */
public interface HomeService {

  /**
   * Url used to fetch data from cloud.
   */
  String API_HOME_URL = "http://api.deezer.com/2.0/user/2529/albums";

  /**
   * Retrieve a list of albums from either cloud or cache.
   */
  List<Album> fetchAlbums() throws NetworkConnectionException, ParsingException;
}
