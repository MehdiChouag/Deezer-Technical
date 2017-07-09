package com.mehdi.deezertechnicaltest.data.home.model;

/**
 * POJO for Album
 */
public class Album {

  private int id;
  private String title;
  private String coverUrl;
  private Artist artist;

  private boolean isAlbumCover = true;

  public Album() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCoverUrl() {
    return coverUrl;
  }

  public void setCoverUrl(String coverUrl) {
    this.coverUrl = coverUrl;
  }

  public Artist getArtist() {
    return artist;
  }

  public void setArtist(Artist artist) {
    this.artist = artist;
  }

  public boolean isAlbumCover() {
    return isAlbumCover;
  }

  public void toggleAlbumCover() {
    isAlbumCover = !isAlbumCover;
  }
}
