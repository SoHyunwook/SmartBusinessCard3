package com.example.smartbusinesscard2;

/**
 * Created by 현욱 on 2016-11-29.
 */
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class ThreeFragment extends Fragment{

    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ArrayList<MyData2> myDataset;
    AdapterThree mAdapter;
    AdapterThree.ItemClick itemClick;

    public ThreeFragment() {
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
        myDataset.add(new MyData2("Upper than My Position"));
        myDataset.add(new MyData2("Same as My Position"));
        myDataset.add(new MyData2("Lower than My Position"));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.three_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listView_pos);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new AdapterThree(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setItemClick(new AdapterThree.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(getActivity(), FragmentForThree.class);
                switch(position) {
                    case 0:
                        i.putExtra("pos", "up");
                        break;
                    case 1:
                        i.putExtra("pos", "same");
                        break;
                    case 2:
                        i.putExtra("pos", "low");
                        break;
                }
                startActivity(i);
            }
        });
        return view;
    }
    void dbOpen() {
        if(dbManager == null) {
            dbManager = new DBManager(getActivity(), "myDB.db", null, 1);
        }
        sqLiteDatabase = dbManager.getWritableDatabase();
    }
    void dbClose() {
        if(sqLiteDatabase != null) {
            if(sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
    }
}
