package com.mehdi.deezertechnicaltest.data.home.parsing;

import android.util.JsonReader;
import com.mehdi.deezertechnicaltest.data.exception.ParsingException;
import com.mehdi.deezertechnicaltest.data.home.model.Album;
import com.mehdi.deezertechnicaltest.data.home.model.Artist;
import com.mehdi.deezertechnicaltest.data.parsing.StreamParser;
import com.mehdi.deezertechnicaltest.data.util.FileUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link StreamParser} for home data.
 */
public class HomeStreamParser implements StreamParser<List<Album>> {

  private List<Album> albumList;

  private JsonReader reader;

  public HomeStreamParser() {
  }

  @Override
  public List<Album> parse(InputStream stream) throws ParsingException {
    albumList = new ArrayList<>();
    reader = new JsonReader(new InputStreamReader(stream));
    try {
      readData();
      FileUtil.closeSilently(stream);
    } catch (IOException e) {
      throw new ParsingException(e.getCause());
    }
    return albumList;
  }

  private void readData() throws IOException {
    reader.beginObject();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case HomeParsingContract.INDEX_DATA:
          addAlbum();
          break;
        default:
          reader.skipValue();
      }
    }
    reader.endObject();
  }

  /**
   * Navigate through Album nodes.
   */
  private void addAlbum() throws IOException {
    reader.beginArray();
    while (reader.hasNext()) {
      reader.beginObject();
      albumList.add(readAlbum());
      reader.endObject();
    }
    reader.endArray();
  }

  /**
   * Parsing album object.
   */
  private Album readAlbum() throws IOException {
    final Album album = new Album();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case HomeParsingContract.VALUE_ID:
          album.setId(reader.nextInt());
          break;
        case HomeParsingContract.VALUE_TITLE:
          album.setTitle(reader.nextString());
          break;
        case HomeParsingContract.VALUE_COVER:
          album.setCoverUrl(reader.nextString());
          break;
        case HomeParsingContract.INDEX_ARTIST:
          reader.beginObject();
          album.setArtist(readArtist());
          reader.endObject();
          break;
        default:
          reader.skipValue();
      }
    }
    return album;
  }

  /**
   * Parsing artist object.
   */
  private Artist readArtist() throws IOException {
    final Artist artist = new Artist();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case HomeParsingContract.VALUE_ID:
          artist.setId(reader.nextInt());
          break;
        case HomeParsingContract.VALUE_NAME:
          artist.setName(reader.nextString());
          break;
        case HomeParsingContract.VALUE_PICTURE_ARTIST:
          artist.setCoverUrl(reader.nextString());
          break;
        default:
          reader.skipValue();
      }
    }
    return artist;
  }
}
