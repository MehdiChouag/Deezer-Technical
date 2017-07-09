package com.mehdi.deezertechnicaltest.data.home.service;

import com.mehdi.deezertechnicaltest.data.exception.NetworkConnectionException;
import com.mehdi.deezertechnicaltest.data.exception.ParsingException;
import com.mehdi.deezertechnicaltest.data.home.model.Album;
import com.mehdi.deezertechnicaltest.data.net.ApiConnection;
import com.mehdi.deezertechnicaltest.data.parsing.StreamParser;
import com.mehdi.deezertechnicaltest.data.util.FileUtil;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Implementation of {@link HomeService}.
 */
public class HomeServiceImpl implements HomeService {

  private ApiConnection connection;
  private StreamParser<List<Album>> parser;

  public HomeServiceImpl(StreamParser<List<Album>> parser) {
    try {
      this.connection = ApiConnection.createGET(API_HOME_URL);
      this.parser = parser;
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<Album> fetchAlbums() throws NetworkConnectionException, ParsingException {
    InputStream in;
    List<Album> albums = null;
    if (connection != null) {
      in = connection.call(true);
      albums = parser.parse(in);
      FileUtil.closeSilently(in);
      connection.close();
    }
    return albums;
  }
}
