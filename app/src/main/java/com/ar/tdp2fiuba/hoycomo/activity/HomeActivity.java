package com.ar.tdp2fiuba.hoycomo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.fragment.MenuFragment;
import com.ar.tdp2fiuba.hoycomo.fragment.StoreFragment;
import com.ar.tdp2fiuba.hoycomo.fragment.StoreListFragment;
import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.ar.tdp2fiuba.hoycomo.model.User;
import com.ar.tdp2fiuba.hoycomo.service.UserAuthenticationManager;
import com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesConstants.SHP_USER;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
            StoreListFragment.OnStoreListFragmentInteractionListener,
            StoreFragment.OnStoreFragmentInteractionListener,
            MenuFragment.OnMenuFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setDrawer(toolbar);

        if (savedInstanceState == null) {
            showListing();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStoreTap(Store item) {
        Log.d(this.getLocalClassName(), "Item selected: " + item.toString());
        showStore(item);
    }

    @Override
    public void onMenuItemTap(com.ar.tdp2fiuba.hoycomo.model.MenuItem item, String storeId) {
        Log.d(this.getLocalClassName(), "Menu item selected: " + item.toString());
        openMenuItem(item, storeId);
    }

    @Override
    public void onMyOrderButtonPressed() {
        // TODO: 5/5/18 Open MyOrder activity.
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_log_out) {
            UserAuthenticationManager.logOut(this);
            continueToLogIn();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String storedUser = SharedPreferencesUtils.load(this, SHP_USER, null);
        if (storedUser != null) {
            User currentUser = new Gson().fromJson(storedUser, User.class);
            if (currentUser.getFirstName() != null) {
                TextView drawerUsername = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_home_user_name);
                drawerUsername.setText(currentUser.getFirstName());
            }
            if (currentUser.getAvatar() != null) {
                CircleImageView drawerUserPicture = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_home_user_picture);
                Picasso.get().load(currentUser.getAvatar()).into(drawerUserPicture);
            }
        }
    }

    private void continueToLogIn() {
        Intent intent = new Intent();
        intent.setClass(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        HomeActivity.this.finish();
    }

    private void showListing() {
        if (findViewById(R.id.home_fragment_container) != null) {
            StoreListFragment listFragment = StoreListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_fragment_container, listFragment)
                    .commit();
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    private void showStore(Store store) {
        if (findViewById(R.id.home_fragment_container) != null) {
            StoreFragment storeFragment = StoreFragment.newInstance(store);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_fragment_container, storeFragment)
                    .addToBackStack(store.getId())
                    .commit();
            getSupportActionBar().setTitle(store.getName());
        }
    }

    private void openMenuItem(com.ar.tdp2fiuba.hoycomo.model.MenuItem item, String storeId) {
        Intent intent = new Intent(this, MenuItemActivity.class);
        intent.putExtra(MenuItemActivity.ARG_MENU_ITEM, new Gson().toJson(item));
        intent.putExtra(MenuItemActivity.ARG_STORE_ID, storeId);
        startActivity(intent);
    }
}
