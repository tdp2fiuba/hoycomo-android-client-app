package com.ar.tdp2fiuba.hoycomo.activity;

import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.adapter.ImagePagerAdapter;
import com.ar.tdp2fiuba.hoycomo.model.MenuItem;
import com.google.gson.Gson;

public class MenuItemActivity extends AppCompatActivity {

    public final static String ARG_MENU_ITEM = "menuItem";

    private final static String TAG = "MenuItemActivity";

    private MenuItem mMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getStringExtra(ARG_MENU_ITEM) != null) {
                mMenuItem = new Gson().fromJson(getIntent().getStringExtra(ARG_MENU_ITEM), MenuItem.class);
                displayInfo();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void addToCart(View v) {
        Log.d(TAG, "MenuItem with ID " + mMenuItem.getId() + " added to My Cart");
        // TODO: 3/5/18 Add MenuItem to My Cart
    }

    private void displayInfo() {
        ((TextView) findViewById(R.id.menu_item_name)).setText(mMenuItem.getName());
        ((TextView) findViewById(R.id.menu_item_description)).setText(mMenuItem.getDescription());
        setImages();
        setPrice();
        setForm();
        setSuitableForTable();
    }

    private void setImages() {
        final View container = findViewById(R.id.menu_item_image_slider_container);
        if (mMenuItem.getPictures() != null && !mMenuItem.getPictures().isEmpty()) {
            container.setVisibility(View.VISIBLE);
            ViewPager viewPager = findViewById(R.id.menu_item_slider_view_pager);
            TabLayout tabLayout = findViewById(R.id.menu_item_slider_tab_dots);
            tabLayout.setupWithViewPager(viewPager, true);
            PagerAdapter adapter = new ImagePagerAdapter(this, mMenuItem.getPictures());
            viewPager.setAdapter(adapter);
        } else {
            container.setVisibility(View.GONE);
        }
    }

    private void setPrice() {
        final TextView discountView = findViewById(R.id.menu_item_price_without_discount);
        final TextView priceView = findViewById(R.id.menu_item_price);
        Integer finalPrice = mMenuItem.getPrice();
        if (mMenuItem.getDiscount() != null && mMenuItem.getDiscount() != 0) {
            finalPrice -= mMenuItem.getDiscount();
            discountView.setVisibility(View.VISIBLE);
            discountView.setText("$" + String.valueOf(mMenuItem.getPrice()));
            discountView.setPaintFlags(discountView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            discountView.setVisibility(View.GONE);
        }
        priceView.setText("$" + String.valueOf(finalPrice));
    }

    private void setForm() {
        setQuantitySpinner();
        setGarnishSpinner();
    }

    private void setSuitableForTable() {
        setSuitableFor(mMenuItem.isCeliac(), R.id.menu_item_coeliacs_icon);
        setSuitableFor(mMenuItem.isDiabetic(), R.id.menu_item_diabetics_icon);
        setSuitableFor(mMenuItem.isVegan(), R.id.menu_item_vegans_icon);
        setSuitableFor(mMenuItem.isVegetarian(), R.id.menu_item_vegetarians_icon);
    }

    private void setSuitableFor(boolean suitableFor, int imageViewId) {
        final ImageView icon = findViewById(imageViewId);
        if (suitableFor) {
            icon.setImageResource(R.mipmap.ic_done_black_18dp);
            icon.setColorFilter(ContextCompat.getColor(this, R.color.success), PorterDuff.Mode.SRC_IN);
        } else {
            icon.setImageResource(R.mipmap.ic_close_black_36dp);
            icon.setColorFilter(ContextCompat.getColor(this, R.color.error), PorterDuff.Mode.SRC_IN);
        }
    }

    private void setQuantitySpinner() {
        final Spinner quantitySpinner = findViewById(R.id.menu_item_quantity_spinner);
        String[] options = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        quantitySpinner.setAdapter(adapter);
    }

    private void setGarnishSpinner() {
        final Spinner garnishSpinner = findViewById(R.id.menu_item_garnish_spinner);
        final TextView garnishLabel = findViewById(R.id.menu_item_garnish_label);

        if (mMenuItem.getGarnishes() != null && !mMenuItem.getGarnishes().isEmpty()) {
            garnishLabel.setVisibility(View.VISIBLE);
            garnishSpinner.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mMenuItem.getGarnishes());
            garnishSpinner.setAdapter(adapter);
        } else {
            garnishLabel.setVisibility(View.GONE);
            garnishSpinner.setVisibility(View.GONE);
        }
    }
}
