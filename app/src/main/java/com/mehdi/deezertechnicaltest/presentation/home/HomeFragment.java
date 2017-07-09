package com.mehdi.deezertechnicaltest.presentation.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.mehdi.deezertechnicaltest.Injection;
import com.mehdi.deezertechnicaltest.R;
import com.mehdi.deezertechnicaltest.data.exception.NetworkConnectionException;
import com.mehdi.deezertechnicaltest.data.exception.ParsingException;
import com.mehdi.deezertechnicaltest.data.home.model.Album;
import java.util.List;

/**
 * Fragment displaying albums.
 */
public class HomeFragment extends Fragment implements HomeView, View.OnClickListener {

  private HomePresenter presenter;
  private HomeAdapter adapter;

  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private AppCompatButton retryBtn;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapter = new HomeAdapter(getContext());
    presenter = new HomePresenter(Injection.getInstance().getHomeRepository());
    presenter.setView(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    progressBar = (ProgressBar) view.findViewById(R.id.progress);
    recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
    retryBtn = (AppCompatButton) view.findViewById(R.id.retry);
    retryBtn.setOnClickListener(this);

    initRecycler();
    presenter.retrieveAlbums();
  }

  private void initRecycler() {
    int rows = getResources().getInteger(R.integer.home_list_row);
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), rows));
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void displayAlbum(List<Album> albums) {
    adapter.setAlbums(albums);
  }

  @Override
  public void showLoading() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideLoading() {
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public void showError(Exception e) {
    if (e instanceof NetworkConnectionException) {
      Toast.makeText(getContext(), R.string.home_error_network, Toast.LENGTH_SHORT).show();
    } else if (e instanceof ParsingException) {
      Toast.makeText(getContext(), R.string.home_error_parsing, Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void showRetry() {
    retryBtn.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideRetry() {
    retryBtn.setVisibility(View.GONE);
  }

  @Override
  public void onClick(View v) {
    presenter.retrieveAlbums();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    presenter.stop();
  }
}
