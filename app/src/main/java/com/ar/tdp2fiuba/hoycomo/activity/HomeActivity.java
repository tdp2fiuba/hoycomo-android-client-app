package com.ar.tdp2fiuba.hoycomo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.fragment.StoreFragment;
import com.ar.tdp2fiuba.hoycomo.fragment.StoreListFragment;
import com.ar.tdp2fiuba.hoycomo.model.Address;
import com.ar.tdp2fiuba.hoycomo.model.Filter;
import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.google.gson.Gson;

public class HomeActivity extends AppCompatActivity
        implements StoreListFragment.OnStoreListFragmentInteractionListener,
            StoreFragment.OnStoreFragmentInteractionListener {

    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            String filterJSON = intent.getExtras().getString("filter");
            filter = Filter.parseJSONFilter(filterJSON);
        }
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            showListing();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        // Para que el ícono sea blanco, ya que android:iconTint sólo es compatible desde 26 para arriba
        MenuItem filter = menu.findItem(R.id.filter);
        if (filter != null) {
            Drawable normalDrawable = filter.getIcon();
            Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
            DrawableCompat.setTint(wrapDrawable, this.getResources().getColor(R.color.white));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.filter:
                Intent intent = new Intent(this, FilterActivity.class);
                intent.putExtra("filter", new Gson().toJson(filter));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onStoreTap(Store item) {
        Log.d(this.getLocalClassName(), "Item selected: " + item.toString());
        showStore(item);
    }

    private void showListing() {
        if (findViewById(R.id.home_fragment_container) != null) {
            StoreListFragment listFragment = StoreListFragment.newInstance();
            Bundle filterBundle = new Bundle();
            filterBundle.putString("filter", new Gson().toJson(filter));
            listFragment.setArguments(filterBundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_fragment_container, listFragment)
                    .commit();
        }
    }

    private void showStore(Store store) {
        if (findViewById(R.id.home_fragment_container) != null) {
            StoreFragment storeFragment = StoreFragment.newInstance(store);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_fragment_container, storeFragment)
                    .addToBackStack(store.getId())
                    .commit();
        }
    }

    @Override
    public void onStoreMapTap(Address address, String markerName) {

    }
}
