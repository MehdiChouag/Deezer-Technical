package com.mehdi.deezertechnicaltest.presentation.base;

/**
 * Interface used as a bridge to communicate between UI (activity, fragment, etc...) and presenters.
 */
public interface LoadDataView {
  /**
   * Indicate to UI to display loading view.
   */
  void showLoading();

  /**
   * Indicate to UI to hide loading view.
   */
  void hideLoading();

  /**
   * Indicate to UI to display an error message associate to an exception.
   *
   * @param e Exception used to determinate error message.
   */
  void showError(Exception e);

  /**
   * Indicate to UI to display retry view.
   */
  void showRetry();

  /**
   * Indicate to UI to hide retry view.
   */
  void hideRetry();
}
