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

public class TwoFragment extends Fragment{

    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    ArrayList<MyData> myDataset;
    AdapterTwo mAdapter;
    AdapterTwo.ItemClick itemClick;

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
        myDataset.add(new MyData(R.drawable.cj, "CJ MEMBERS"));
        myDataset.add(new MyData(R.drawable.kb, "KB MEMBERS"));
        myDataset.add(new MyData(R.drawable.lg, "LG MEMBERS"));
        myDataset.add(new MyData(R.drawable.lotte, "LOTTE MEMBERS"));
        myDataset.add(new MyData(R.drawable.samsung, "SAMSUNG MEMBERS"));
        myDataset.add(new MyData(R.drawable.shinsegae, "SHINSEGAE MEMBERS"));
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

        mAdapter.setItemClick(new AdapterTwo.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(getActivity(), FragmentForTwo.class);
                switch(position) {
                    case 0:
                        i.putExtra("group", "cj");
                        break;
                    case 1:
                        i.putExtra("group", "kb");
                        break;
                    case 2:
                        i.putExtra("group", "lg");
                        break;
                    case 3:
                        i.putExtra("group", "lotte");
                        break;
                    case 4:
                        i.putExtra("group", "samsung");
                        break;
                    case 5:
                        i.putExtra("group", "shinsegae");
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
