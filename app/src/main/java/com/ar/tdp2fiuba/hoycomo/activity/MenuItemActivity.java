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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.adapter.ImagePagerAdapter;
import com.ar.tdp2fiuba.hoycomo.model.MenuItem;
import com.ar.tdp2fiuba.hoycomo.model.Order;
import com.ar.tdp2fiuba.hoycomo.model.OrderItem;
import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.ar.tdp2fiuba.hoycomo.model.User;
import com.ar.tdp2fiuba.hoycomo.service.OrderService;
import com.ar.tdp2fiuba.hoycomo.service.UserAuthenticationManager;
import com.ar.tdp2fiuba.hoycomo.utils.NumberUtils;
import com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesConstants.SHP_USER;

public class MenuItemActivity extends AppCompatActivity {

    public final static String ARG_MENU_ITEM = "menuItem";
    public final static String ARG_STORE = "store";

    private final static String TAG = "MenuItemActivity";

    private MenuItem mMenuItem;
    private Store mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            if (getIntent() != null) {
                if (getIntent().getStringExtra(ARG_MENU_ITEM) != null) {
                    mMenuItem = new Gson().fromJson(getIntent().getStringExtra(ARG_MENU_ITEM), MenuItem.class);
                    getSupportActionBar().setTitle(mMenuItem.getName());
                    displayInfo();
                }
                if (getIntent().getStringExtra(ARG_STORE) != null) {
                    mStore = new Gson().fromJson(getIntent().getStringExtra(ARG_STORE), Store.class);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAddToOrderButtonVisibility();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void addToMyOrder(View v) {
        Log.d(TAG, "MenuItem with ID " + mMenuItem.getId() + " added to My Order");
        Order myCurrentOrder = OrderService.getMyCurrentOrder();
        if (myCurrentOrder == null) {
            if (UserAuthenticationManager.isUserLoggedIn(this)) {
                String serializedUser = SharedPreferencesUtils.load(this, SHP_USER, null);
                User user = new Gson().fromJson(serializedUser, User.class);
                List<OrderItem> items = new ArrayList<>();
                OrderItem item = buildOrderItemFromInput();
                items.add(item);
                Order newOrder = new Order(user.getUserId(), mStore, item.getPrice() * item.getQuantity(), items, user.getAddress());
                OrderService.setAsCurrentOrder(newOrder);
                Log.d(TAG, "My Order: " + newOrder);
                finish();
            } else {
                Toast.makeText(this, R.string.log_in_for_order, Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!myCurrentOrder.getStore().equals(mStore)) {
                Toast.makeText(this, R.string.one_store_for_order, Toast.LENGTH_LONG).show();
            } else {
                myCurrentOrder.addItem(buildOrderItemFromInput());
                Log.d(TAG, "My Order: " + myCurrentOrder);
                finish();
            }
        }
    }

    private void displayInfo() {
        ((TextView) findViewById(R.id.menu_item_name)).setText(mMenuItem.getName());
        ((TextView) findViewById(R.id.menu_item_description)).setText(mMenuItem.getDescription());
        setImages();
        setPrice();
        setForm();
        setSuitableForTable();
    }

    private void setAddToOrderButtonVisibility() {
        Button addToMyOrderButton = findViewById(R.id.menu_item_add_to_my_order_button);
        if (!OrderService.isThereCurrentOrder() || OrderService.getMyCurrentOrder().getStore().equals(mStore)) {
            addToMyOrderButton.setVisibility(View.VISIBLE);
        } else {
            addToMyOrderButton.setVisibility(View.GONE);
        }
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
            finalPrice = NumberUtils.subtractPercentage(mMenuItem.getPrice(), mMenuItem.getDiscount());
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

    private OrderItem buildOrderItemFromInput() {
        Integer itemQuantity = Integer.valueOf(((Spinner) findViewById(R.id.menu_item_quantity_spinner)).getSelectedItem().toString());

        Spinner garnishSpinner = (Spinner) findViewById(R.id.menu_item_garnish_spinner);
        String garnish = garnishSpinner.getSelectedItem() != null ?
                garnishSpinner.getSelectedItem().toString() : null;

        EditText commentsInput = (EditText) findViewById(R.id.menu_item_comments_edit_text);
        String itemComments = commentsInput.getText() != null ?
                commentsInput.getText().toString() : null;

        return new OrderItem(
                mMenuItem.getId(),
                mMenuItem.getName(),
                mMenuItem.getDiscount() != null ? NumberUtils.subtractPercentage(mMenuItem.getPrice(), mMenuItem.getDiscount()) : mMenuItem.getPrice(),
                itemQuantity,
                garnish,
                itemComments
        );
    }
}
