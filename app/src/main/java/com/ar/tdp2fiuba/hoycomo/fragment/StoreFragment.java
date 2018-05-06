package com.ar.tdp2fiuba.hoycomo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.DailyTimeWindow;
import com.ar.tdp2fiuba.hoycomo.model.DelayTime;
import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.ar.tdp2fiuba.hoycomo.service.OrderService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

/**
 * Activities that contain this fragment must implement the
 * {@link OnStoreFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragment extends Fragment
        implements OnMapReadyCallback {

    private static final String ARG_STORE = "STORE";
    private Store mStore;

    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mMap;

    private OnStoreFragmentInteractionListener mListener;

    public StoreFragment() {
        // Required empty public constructor
    }

    public static StoreFragment newInstance(Store store) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STORE, new Gson().toJson(store));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStore = new Gson().fromJson(getArguments().getString(ARG_STORE), Store.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        if (view != null) {
            displayInfo(view);
        }

        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_store_map);
        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setMyOrderButtonVisibility();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStoreFragmentInteractionListener) {
            mListener = (OnStoreFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStoreFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(mStore.getAddress().getLat(), mStore.getAddress().getLon());
        mMap.addMarker(new MarkerOptions().position(latLng).title(mStore.getName()).snippet(mStore.getAddress().getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        fixScrollOnMap();
    }

    private void displayInfo(final View view) {
        loadImage((ImageView) view.findViewById(R.id.fragment_store_image));
        ((TextView) view.findViewById(R.id.fragment_store_name)).setText(mStore.getName());
        setDelayTime((TextView) view.findViewById(R.id.fragment_store_delay_time));
        showMenu(view);
        displayTimetable(view.findViewById(R.id.timetable));

        ((Button) view.findViewById(R.id.fragment_store_my_order_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMyOrderButtonPressed();
            }
        });
    }

    private void setMyOrderButtonVisibility() {
        if (getView() != null) {
            final Button myOrderButton = getView().findViewById(R.id.fragment_store_my_order_button);
            if (OrderService.isThereCurrentOrder()
                    && OrderService.getMyCurrentOrder().getStore().equals(mStore)) {
                myOrderButton.setVisibility(View.VISIBLE);
            } else {
                myOrderButton.setVisibility(View.GONE);
            }
        }
    }

    private void showMenu(final View view) {
        if (view.findViewById(R.id.fragment_store_menu) != null) {
            MenuFragment menuFragment = MenuFragment.newInstance(mStore);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_store_menu, menuFragment)
                    .commit();
        }
    }

    private void loadImage(final ImageView imageView) {
        Picasso.get()
                .load(mStore.getAvatar())
                .fit()
                .into(imageView);
    }

    private void setDelayTime(final TextView textView) {
        DelayTime storeDelayTime = mStore.getDelayTime();
        if (storeDelayTime == null) {
            textView.setVisibility(View.GONE);
            return;
        }
        String minDelayTime = storeDelayTime.getMin() != null ? storeDelayTime.getMin().toString() : null;
        String maxDelayTime = storeDelayTime.getMax().toString();
        String delayTime = minDelayTime != null ?
                getResources().getString(R.string.minutes_range)
                        .replace(":min", minDelayTime)
                        .replace(":max", maxDelayTime) :
                getResources().getString(R.string.up_to_minutes)
                        .replace(":max", maxDelayTime);
        textView.setText(delayTime);
    }

    private void displayTimetable(final View timetable) {
        displayDailyTimeWindow(mStore.getAvailability().getMonday(), timetable, R.id.timetable_monday_hours);
        displayDailyTimeWindow(mStore.getAvailability().getTuesday(), timetable, R.id.timetable_tuesday_hours);
        displayDailyTimeWindow(mStore.getAvailability().getWednesday(), timetable, R.id.timetable_wednesday_hours);
        displayDailyTimeWindow(mStore.getAvailability().getThursday(), timetable, R.id.timetable_thursday_hours);
        displayDailyTimeWindow(mStore.getAvailability().getFriday(), timetable, R.id.timetable_friday_hours);
        displayDailyTimeWindow(mStore.getAvailability().getSaturday(), timetable, R.id.timetable_saturday_hours);
        displayDailyTimeWindow(mStore.getAvailability().getSunday(), timetable, R.id.timetable_sunday_hours);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void fixScrollOnMap() {
        final NestedScrollView mainScrollView = (NestedScrollView) getView().findViewById(R.id.fragment_store_scroll_view);
        final ImageView transparentImageView = (ImageView) getView().findViewById(R.id.fragment_store_transparent_image);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    private void displayDailyTimeWindow(DailyTimeWindow timeWindow, final View timetable, int timeWindowViewId) {
        TextView hoursTextView = (TextView) timetable.findViewById(timeWindowViewId);
        if (timeWindow != null && !timeWindow.isClosedAllDay()) {
            if (timeWindow.isOpenAllDay()) {
                hoursTextView.setText(R.string.open_all_day);
            } else if (timeWindow.getStartTime() == null && timeWindow.getEndTime() == null) {
                hoursTextView.setText("-");
            } else {
                hoursTextView.setText(
                        hoursTextView.getText().toString()
                                .replace(":min", timeWindow.getStartTime() != null ? timeWindow.getStartTime() : "-")
                                .replace(":max", timeWindow.getEndTime() != null ? timeWindow.getEndTime() : "-")
                );
            }
        } else {
            hoursTextView.setText(R.string.closed);
            hoursTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnStoreFragmentInteractionListener {
        void onMyOrderButtonPressed();
    }

}
