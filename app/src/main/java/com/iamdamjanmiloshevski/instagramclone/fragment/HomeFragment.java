package com.iamdamjanmiloshevski.instagramclone.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iamdamjanmiloshevski.instagramclone.R;
import com.iamdamjanmiloshevski.instagramclone.adapter.UserAdapter;
import com.iamdamjanmiloshevski.instagramclone.utility.Utility;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * --------------------------------------------
 * Author: Damjan Miloshevski
 * E-mail: damjan.milosevski@korvus.mk
 * Date: 15.12.2017
 * Project: instagram-clone
 * © Corvus Info d.o.o Zagreb, Croatia
 * --------------------------------------------
 */

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private RecyclerView mUsers;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);

        initFragmentUI(view);

        return view;
    }


    @Override
    public void initFragmentUI(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mUsers = view.findViewById(R.id.rv_users);
        mUsers.setLayoutManager(layoutManager);
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    private void getFeed() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        UserAdapter adapter = new UserAdapter(getContext(), objects);
                        mUsers.setAdapter(adapter);
                    }
                }
            }
        });
    }

    private void displayCachedUsers() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRefresh();
    }

    @Override
    public int getLayoutId() {
        return R.layout.tab_home;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        if (Utility.isNetworkAvailable(getContext())) {
            getFeed();
        } else {
            Toast.makeText(getContext(), "Couldn't refresh feed", Toast.LENGTH_SHORT).show();
            displayCachedUsers();
        }


    }
}
