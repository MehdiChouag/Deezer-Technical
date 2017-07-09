package com.mehdi.deezertechnicaltest.presentation.base;

/**
 * Base class representing a Presenter in a model view presenter (MVP) pattern.
 *
 * @param <T> Represent the interface will use to communicate with the view (Activity or Fragment).
 */
public abstract class BasePresenter<T extends LoadDataView> {

  protected T view;

  public void setView(T view) {
    this.view = view;
  }

  /**
   * Cancel any ongoing tasks.
   */
  public abstract void stop();
}
