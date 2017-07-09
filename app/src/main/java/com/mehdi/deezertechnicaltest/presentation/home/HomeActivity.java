package com.mehdi.deezertechnicaltest.presentation.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.mehdi.deezertechnicaltest.R;
import com.mehdi.deezertechnicaltest.data.cache.CacheManager;

public class HomeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.clear_cache) {
      CacheManager.getInstance().clearCache();
      Toast.makeText(this, "Cache cleared", Toast.LENGTH_SHORT).show();
    }
    return super.onOptionsItemSelected(item);
  }
}
