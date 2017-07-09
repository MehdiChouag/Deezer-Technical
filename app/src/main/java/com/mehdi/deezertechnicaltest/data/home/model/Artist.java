package com.mehdi.deezertechnicaltest.data.home.model;

/**
 * POJO for Artist.
 */
public class Artist {

  private int id;
  private String name;
  private String coverUrl;

  public Artist() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCoverUrl() {
    return coverUrl;
  }

  public void setCoverUrl(String coverUrl) {
    this.coverUrl = coverUrl;
  }
}
