package com.ar.tdp2fiuba.hoycomo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.User;
import com.ar.tdp2fiuba.hoycomo.service.UserAuthenticationManager;
import com.ar.tdp2fiuba.hoycomo.utils.GeocodingUtils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadUser();
        displayUserInfo();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadUser() {
        mUser = UserAuthenticationManager.getUser(this);
    }

    private void displayUserInfo() {
        displayPicture();
        displayUsername();
        displayAddress();
    }

    private void displayPicture() {
        CircleImageView userPicture = findViewById(R.id.profile_user_picture);
        Picasso.get().load(mUser.getAvatar()).into(userPicture);
    }

    private void displayUsername() {
        ((TextView) findViewById(R.id.profile_username)).setText(mUser.getFirstName() + " " + mUser.getLastName());
    }

    private void displayAddress() {
        setAddress();
        setEditAddressButtonListener();
    }

    private void setAddress() {
        if (mUser.getAddress() != null && !TextUtils.isEmpty(mUser.getAddress().getName())) {
            ((TextView) findViewById(R.id.profile_address)).setText(GeocodingUtils.extractLocalAddress(mUser.getAddress().getName()));
        }
    }

    private void setEditAddressButtonListener() {
        findViewById(R.id.profile_edit_address_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlaceAutocomplete();
            }
        });
    }

    private void openPlaceAutocomplete() {
        // TODO: 3/6/18 Copy from ConfirmOrderActivity
    }
}
