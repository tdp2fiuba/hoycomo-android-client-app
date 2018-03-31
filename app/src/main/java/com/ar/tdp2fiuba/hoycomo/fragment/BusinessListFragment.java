package com.ar.tdp2fiuba.hoycomo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.adapter.BusinessRecyclerViewAdapter;
import com.ar.tdp2fiuba.hoycomo.model.Business;
import com.ar.tdp2fiuba.hoycomo.service.BusinessService;
import com.ar.tdp2fiuba.hoycomo.utils.PaginationScrollListener;
import com.ar.tdp2fiuba.hoycomo.utils.RecyclerViewEmptySupport;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnBusinessListFragmentInteractionListener}
 * interface.
 */
public class BusinessListFragment extends Fragment {

    private OnBusinessListFragmentInteractionListener mListener;

    private BusinessRecyclerViewAdapter mAdapter;

    private final BusinessService businessService;  // TODO: 31/03/18 Don't instantiate it.
    private boolean isLoading = false;
    private static final int paginationCount = 20;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BusinessListFragment() {
        businessService = new BusinessService();
    }

    public static BusinessListFragment newInstance() {
        return new BusinessListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_business_list, container, false);

        if (rootView != null) {
            Context context = rootView.getContext();
            RecyclerViewEmptySupport recyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.business_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setEmptyView(rootView.findViewById(R.id.business_empty_list));
            recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                @Override
                protected void loadMoreItems() {
                    retrieveMoreBusinesses();
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }

                @Override
                protected int getLoadingOffset() {
                    return 5;
                }
            });
            mAdapter = new BusinessRecyclerViewAdapter(mListener);
            recyclerView.setAdapter(mAdapter);
            retrieveMoreBusinesses();

            rootView.findViewById(R.id.business_empty_list).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshData();
                }
            });
        }

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBusinessListFragmentInteractionListener) {
            mListener = (OnBusinessListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBusinessListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        businessService.resetCount();
    }

    private void retrieveMoreBusinesses() {
        isLoading = true;
        mAdapter.add(businessService.getBusinesses(paginationCount));
        isLoading = false;
    }

    private void refreshData() {
        businessService.resetCount();
        retrieveMoreBusinesses();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnBusinessListFragmentInteractionListener {
        void onTap(Business item);
    }
}
