package com.mehdi.deezertechnicaltest.presentation.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.mehdi.deezertechnicaltest.R;
import com.mehdi.deezertechnicaltest.data.executor.Priority;
import com.mehdi.deezertechnicaltest.data.home.model.Album;
import com.mehdi.deezertechnicaltest.image.ImageLoader;
import java.util.List;

class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.AlbumViewHolder> {

  private List<Album> albums;
  private int lastAnimatedPosition = -1;

  private final Context context;

  HomeAdapter(Context context) {
    this.context = context;
  }

  @Override
  public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);
    return new AlbumViewHolder(view);
  }

  @Override
  public void onBindViewHolder(AlbumViewHolder holder, int position) {
    runEnterAnimation(holder.itemView, position);
    holder.setData(albums.get(position));
  }

  /**
   * Start an animation when an item is bind.
   */
  private void runEnterAnimation(View view, int position) {
    int animRes = position > lastAnimatedPosition ? R.anim.slide_up : R.anim.slide_down;
    lastAnimatedPosition = position;
    Animation animation = AnimationUtils.loadAnimation(context, animRes);
    view.startAnimation(animation);
  }

  @Override
  public int getItemCount() {
    return albums != null ? albums.size() : 0;
  }

  void setAlbums(List<Album> albums) {
    this.albums = albums;
    notifyDataSetChanged(); // This can be improve using DiffUtil
  }

  @Override
  public void onViewDetachedFromWindow(AlbumViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    holder.itemView.clearAnimation();
  }

  class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView albumName;
    private TextView artistName;
    private ImageView cover;

    AlbumViewHolder(View itemView) {
      super(itemView);
      albumName = (TextView) itemView.findViewById(R.id.album_name);
      artistName = (TextView) itemView.findViewById(R.id.artist_name);
      cover = (ImageView) itemView.findViewById(R.id.cover);
      cover.setOnClickListener(this);
    }

    public void setData(Album album) {
      albumName.setText(album.getTitle());
      artistName.setText(album.getArtist().getName());
      String urlToLoad =
          album.isAlbumCover() ? album.getCoverUrl() : album.getArtist().getCoverUrl();
      ImageLoader.getInstance().loadImage(urlToLoad, cover);
    }

    @Override
    public void onClick(View v) {
      final Album album = albums.get(getAdapterPosition());
      String urlToLoad =
          album.isAlbumCover() ? album.getArtist().getCoverUrl() : album.getCoverUrl();
      ImageLoader.getInstance().loadImage(urlToLoad, cover, Priority.HIGH, false);
      album.toggleAlbumCover();
    }
  }
}
