package com.ar.tdp2fiuba.hoycomo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.adapter.StoreRecyclerViewAdapter;
import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.ar.tdp2fiuba.hoycomo.service.StoreService;
import com.ar.tdp2fiuba.hoycomo.utils.PaginationScrollListener;
import com.ar.tdp2fiuba.hoycomo.utils.RecyclerViewEmptySupport;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnStoreListFragmentInteractionListener}
 * interface.
 */
public class StoreListFragment extends Fragment {

    private OnStoreListFragmentInteractionListener mListener;

    private StoreRecyclerViewAdapter mAdapter = null;

    private static int currentPage = 0;
    private static final int paginationCount = 20;
    private boolean isLoading = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoreListFragment() {}

    public static StoreListFragment newInstance() {
        return new StoreListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_store_list, container, false);

        if (rootView != null) {
            Context context = rootView.getContext();
            RecyclerViewEmptySupport recyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.store_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setEmptyView(rootView.findViewById(R.id.store_empty_list));
            recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                @Override
                protected void loadMoreItems() {
                    retrieveMoreStores();
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }

                @Override
                protected boolean isLastPage() {
                    // TODO: 31/03/18 We should handle status code on API response to know when pagination is over.
                    return false;
                }

                @Override
                protected int getLoadingOffset() {
                    return 5;
                }
            });
            mAdapter = new StoreRecyclerViewAdapter(mListener);
            recyclerView.setAdapter(mAdapter);
            retrieveMoreStores();

            rootView.findViewById(R.id.store_empty_list).setOnClickListener(new View.OnClickListener() {
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
        if (context instanceof OnStoreListFragmentInteractionListener) {
            mListener = (OnStoreListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStoreListFragmentInteractionListener");
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
        currentPage = 0;
    }

    private void retrieveMoreStores() {
        Response.Listener<JSONArray> successListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                stopLoading();
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            mAdapter.add(new Gson().fromJson(response.getJSONObject(i).toString(), Store.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    currentPage++;
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopLoading();
                error.printStackTrace();
                Toast.makeText(getActivity(), R.string.error_no_stores, Toast.LENGTH_SHORT).show();
            }
        };
        startLoading();
        StoreService.getStores(currentPage + 1, paginationCount, successListener, errorListener);
    }

    private void refreshData() {
        currentPage = 0;
        retrieveMoreStores();
    }

    private void startLoading() {
        isLoading = true;
        if (mAdapter != null) {
            mAdapter.addLoadingFooter();
        }
    }

    private void stopLoading() {
        isLoading = false;
        if (mAdapter != null) {
            mAdapter.removeLoadingFooter();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnStoreListFragmentInteractionListener {
        void onStoreTap(Store item);
    }
}
