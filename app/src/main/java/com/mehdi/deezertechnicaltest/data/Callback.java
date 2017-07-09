package com.mehdi.deezertechnicaltest.data;

import android.support.annotation.MainThread;

/**
 * Contract use to deliver result of fetching data operation.
 */
public interface Callback<T> {

  /**
   * Invoked when the data is available.
   */
  @MainThread
  void onSuccess(T data);

  /**
   * Invoked when an error occurs
   */
  @MainThread
  void onFailure(Exception throwable);
}
