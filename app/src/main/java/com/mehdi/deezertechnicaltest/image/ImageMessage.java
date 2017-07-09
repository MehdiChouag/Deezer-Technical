package com.mehdi.deezertechnicaltest.image;

import android.graphics.drawable.BitmapDrawable;

/**
 * POJO used to communicate between main and background thread.
 */
class ImageMessage {
  private BitmapDrawable drawable;
  private String url;

  ImageMessage(BitmapDrawable drawable, String url) {
    this.drawable = drawable;
    this.url = url;
  }

  BitmapDrawable getBitmapDrawable() {
    return drawable;
  }

  String getUrl() {
    return url;
  }
}