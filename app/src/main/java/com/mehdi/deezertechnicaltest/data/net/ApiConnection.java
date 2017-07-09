package com.mehdi.deezertechnicaltest.data.net;

import android.support.annotation.WorkerThread;
import com.mehdi.deezertechnicaltest.data.exception.NetworkConnectionException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class making HTTP calls.
 */
public class ApiConnection {

  /**
   * All http code equals or above 400 are error code.
   */
  private static final int HTTP_ERROR_CODE = 400;

  private static final String KEY_HEADER_CACHE = "Cache-Control";
  private static final String VALUE_HEADER_CACHE = "max-stale=";

  private static final int CACHE_VALIDITY = 60 * 60; // 1h

  private static final int TIMEOUT_REQUEST = 30000; // 30sec

  private URL url;

  private HttpURLConnection connection;

  private ApiConnection(String url) throws MalformedURLException {
    this.url = new URL(url);
  }

  public static ApiConnection createGET(String url) throws MalformedURLException {
    return new ApiConnection(url);
  }

  /**
   * Start HTTP call
   *
   * @param cacheRequest True if cache enable for this request.
   * @return An {@link InputStream} containing the result of network call.
   * @throws NetworkConnectionException Throws if any error occurs during network call.
   */
  @WorkerThread
  public InputStream call(boolean cacheRequest) throws NetworkConnectionException {
    try {
      connection = (HttpURLConnection) url.openConnection();
      connection.setConnectTimeout(TIMEOUT_REQUEST);
      if (cacheRequest) {
        connection.addRequestProperty(KEY_HEADER_CACHE, VALUE_HEADER_CACHE + CACHE_VALIDITY);
      }
      if (connection.getResponseCode() >= HTTP_ERROR_CODE) {
        throw new NetworkConnectionException();
      }
      return new BufferedInputStream(connection.getInputStream());
    } catch (IOException e) {
      throw new NetworkConnectionException(e.getCause());
    }
  }

  /**
   * Close connection.
   */
  public void close() {
    if (connection != null) {
      connection.disconnect();
    }
  }
}
