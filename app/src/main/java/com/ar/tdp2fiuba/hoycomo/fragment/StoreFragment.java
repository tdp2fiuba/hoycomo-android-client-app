package com.ar.tdp2fiuba.hoycomo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.DailyTimeWindow;
import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

/**
 * Activities that contain this fragment must implement the
 * {@link OnStoreFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragment extends Fragment {

    private static final String ARG_STORE = "STORE";
    private Store mStore;

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

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

    private void displayInfo(final View view) {
        loadImage((ImageView) view.findViewById(R.id.fragment_store_image));
        ((TextView) view.findViewById(R.id.fragment_store_name)).setText(mStore.getName());
        setDelayTime((TextView) view.findViewById(R.id.fragment_store_delay_time));
        displayTimetable(view.findViewById(R.id.timetable));
    }

    private void loadImage(final ImageView imageView) {
        Picasso.get()
                .load(mStore.getImageUrl())
                .fit()
                .into(imageView);
    }

    private void setDelayTime(final TextView textView) {
        String minDelayTime = mStore.getMinDelayTime() != null ? mStore.getMinDelayTime().toString() : null;
        String maxDelayTime = mStore.getMaxDelayTime().toString();
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

    private void displayDailyTimeWindow(@Nullable DailyTimeWindow timeWindow, final View timetable, int timeWindowViewId) {
        TextView hoursTextView = (TextView) timetable.findViewById(timeWindowViewId);
        if (timeWindow != null) {
            if (timeWindow.getStartTime().equals(timeWindow.getEndTime())) {
                hoursTextView.setText(R.string.open_all_day);
            } else {
                hoursTextView.setText(
                        hoursTextView.getText().toString()
                                .replace(":min", timeWindow.getStartTime())
                                .replace(":max", timeWindow.getEndTime())
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
        // TODO: 02/04/18 Define interaction methods.
    }

}
