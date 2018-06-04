package com.ar.tdp2fiuba.hoycomo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.fragment.MenuFragment;
import com.ar.tdp2fiuba.hoycomo.fragment.StoreFragment;
import com.ar.tdp2fiuba.hoycomo.fragment.StoreListFragment;

import com.ar.tdp2fiuba.hoycomo.model.Address;
import com.ar.tdp2fiuba.hoycomo.model.Filter;
import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.google.gson.Gson;
import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.ar.tdp2fiuba.hoycomo.model.User;
import com.ar.tdp2fiuba.hoycomo.service.OrderService;
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

    private Filter filter;
    private final int FILTER_REQUEST_CODE = 1;

    private static final String TAG_STORE_LIST_FRAGMENT = "STORE_LIST";
    private static final String TAG_STORE_FRAGMENT = "STORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!checkCurrentOrder()) {
                super.onBackPressed();
                getSupportActionBar().setTitle(R.string.app_name);
            }
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
                startActivityForResult(intent, FILTER_REQUEST_CODE);
                return true;
            case R.id.delete_filters:
                filter = null;
                showListing();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILTER_REQUEST_CODE:
                if (data != null && data.getExtras() != null) {
                    String filterJSON = data.getExtras().getString("filter");
                    filter = Filter.parseJSONFilter(filterJSON);
                }
                showListing();
                break;
        }
    }

    @Override
    public void onStoreTap(Store item) {
        Log.d(this.getLocalClassName(), "Item selected: " + item.toString());
        showStore(item);
    }

    @Override
    public void onMenuItemTap(com.ar.tdp2fiuba.hoycomo.model.MenuItem item, Store store) {
        Log.d(this.getLocalClassName(), "Menu item selected: " + item.toString());
        openMenuItem(item, store);
    }

    @Override
    public void onMyOrderButtonPressed() {
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
    }

    @Override
    public void onReviewsButtonPressed(Store store) {
        Intent intent = new Intent(this, StoreReviewsActivity.class);
        intent.putExtra(StoreReviewsActivity.ARG_REVIEWED_STORE, new Gson().toJson(store));
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_log_out:
                if (OrderService.isThereCurrentOrder()) {
                    OrderService.clearOrder();
                }
                UserAuthenticationManager.logOut(this);
                continueToLogIn();
                break;
            case R.id.nav_my_orders:
                if (!checkCurrentOrder()) {
                    openMyOrders();
                }
                break;
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

            CircleImageView drawerUserPicture = navigationView.getHeaderView(0).findViewById(R.id.nav_header_home_user_picture);
            drawerUserPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openProfile();
                }
            });
            if (currentUser.getAvatar() != null) {
                Picasso.get().load(currentUser.getAvatar()).into(drawerUserPicture);
            }
        }
    }

    private boolean checkCurrentOrder() {
        if (OrderService.isThereCurrentOrder()) {
            StoreFragment storeFragment = (StoreFragment) getSupportFragmentManager().findFragmentByTag(TAG_STORE_FRAGMENT);
            if (storeFragment != null && storeFragment.isVisible()) {
                openMyOrderCancellationDialog();
                return true;
            }
        }
        return false;
    }

    private void openProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void openMyOrders() {
        Intent intent = new Intent(this, MyOrdersActivity.class);
        startActivity(intent);
    }

    private void continueToLogIn() {
        Intent intent = new Intent();
        intent.setClass(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        HomeActivity.this.finish();
    }

    private void showListing() {
        if (findViewById(R.id.home_fragment_container) != null) {
            StoreListFragment listFragment = StoreListFragment.newInstance(filter);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_fragment_container, listFragment, TAG_STORE_LIST_FRAGMENT)
                    .commit();
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    private void showStore(Store store) {
        if (findViewById(R.id.home_fragment_container) != null) {
            StoreFragment storeFragment = StoreFragment.newInstance(store);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_fragment_container, storeFragment, TAG_STORE_FRAGMENT)
                    .addToBackStack(store.getId())
                    .commit();
            getSupportActionBar().setTitle(store.getName());
        }
    }

    private void openMenuItem(com.ar.tdp2fiuba.hoycomo.model.MenuItem item, Store store) {
        Intent intent = new Intent(this, MenuItemActivity.class);
        intent.putExtra(MenuItemActivity.ARG_MENU_ITEM, new Gson().toJson(item));
        intent.putExtra(MenuItemActivity.ARG_STORE, new Gson().toJson(store));
        startActivity(intent);
    }

    private void openMyOrderCancellationDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(R.string.my_order)
                .setMessage(R.string.my_order_cancellation_dialog_body)
                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        OrderService.clearOrder();
                        HomeActivity.super.onBackPressed();
                        getSupportActionBar().setTitle(R.string.app_name);
                    }
                })
                .setNegativeButton(R.string.better_not, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(R.mipmap.ic_shopping_cart_black_24dp)
                .show();
    }
}
