package com.example.smartbusinesscard2;

/**
 * Created by 현욱 on 2016-11-29.
 */
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TwoFragment extends Fragment{

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ArrayList<MyData> myDataset;
    AdapterTwo mAdapter;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }

    private void initDataset() {
        //for Test
        myDataset = new ArrayList<>();
        myDataset.add(new MyData(R.drawable.cj));
        myDataset.add(new MyData(R.drawable.kb));
        myDataset.add(new MyData(R.drawable.lg));
        myDataset.add(new MyData(R.drawable.lotte));
        myDataset.add(new MyData(R.drawable.samsung));
        myDataset.add(new MyData(R.drawable.shinsegae));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.two_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listView_com);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new AdapterTwo(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }
}
