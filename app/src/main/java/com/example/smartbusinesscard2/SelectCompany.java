package com.example.smartbusinesscard2;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by yi_te on 2016-11-30.
 */
public class SelectCompany extends Activity {
    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> company = null;
    private ArrayList<String> mChildListContent = null;
    private ArrayList<String> mChildListContent2 = null;
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor, cursor2, cursor3;
    static String s_company;
    static String EmailAddress, CompanyAddress, NotCompanyAddress;
    Button ok;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_list);

        FontClass.setDefaultFont(this, "DEFAULT", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "MONOSPACE", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SERIF", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SANS_SERIF", "NotoSans-Bold.ttf");

        mGroupList = new ArrayList<String>();
        mGroupList.add("My Company");
        mGroupList.add("Not My Company");

        ok = (Button) findViewById(R.id.s_button);
        company = new ArrayList<ArrayList<String>>();

        mChildListContent = new ArrayList<String>();
        mChildListContent2 = new ArrayList<String>();

        dbOpen();
        cursor = sqLiteDatabase.query("USER", null, null, null, null, null, null);
        while(cursor.moveToNext()) {
            s_company = null;
            s_company = cursor.getString(2);
        }

        cursor2 = sqLiteDatabase.rawQuery("SELECT * FROM CARDMEMBER WHERE c_name='"
                + s_company
                + "'", null);
        System.out.println("company = " + s_company);
        CompanyAddress = null;
        while(cursor2.moveToNext()){
            String name = cursor2.getString(1);
            if(cursor2.getString(4) != null) {
                CompanyAddress += cursor2.getString(4);
                CompanyAddress += ",";
            }
            System.out.println("CompanyAddress = " + CompanyAddress);
            mChildListContent.add(name);
        }
        company.add(mChildListContent);

        cursor3 = sqLiteDatabase.rawQuery("SELECT * FROM CARDMEMBER WHERE c_name!='"
                + s_company
                + "'", null);
        NotCompanyAddress = null;
        while(cursor3.moveToNext()){
            String nname = cursor3.getString(1);
            if(cursor3.getString(4) != null) {
                NotCompanyAddress += cursor3.getString(4);
                NotCompanyAddress += ",";
            }
            System.out.println("NotCompanyAddress = " + NotCompanyAddress);
            mChildListContent2.add(nname);
        }
        company.add(mChildListContent2);

        cursor3.close();
        cursor2.close();
        cursor.close();
        dbClose();
        dbManager.close();

        setLayout();

        mListView.setAdapter(new BaseExpandableAdapter(this, mGroupList, company));

        // 그룹 클릭 했을 경우 이벤트
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if(groupPosition == 0) {
                    EmailAddress = CompanyAddress;
                    //                 Toast.makeText(getApplicationContext(), "Select My Company",
                    //                        Toast.LENGTH_SHORT).show();
                    System.out.println("EmailAddress = " + EmailAddress);
                }
                else if(groupPosition == 1) {
                    EmailAddress = NotCompanyAddress;
                    //               Toast.makeText(getApplicationContext(), "Select Not My Company",
                    //                      Toast.LENGTH_SHORT).show();
                    System.out.println("EmailAddress = " + EmailAddress);
                }

                return false;
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 차일드 클릭 했을 경우 이벤트
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //        Toast.makeText(getApplicationContext(), "c click = " + childPosition,
                //               Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // 그룹이 닫힐 경우 이벤트
        mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //     Toast.makeText(getApplicationContext(), "g Collapse = " + groupPosition,
                //           Toast.LENGTH_SHORT).show();
            }
        });

        // 그룹이 열릴 경우 이벤트
        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //   Toast.makeText(getApplicationContext(), "g Expand = " + groupPosition,
                //         Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * Layout
     */
    private ExpandableListView mListView;

    private void setLayout(){
        mListView = (ExpandableListView) findViewById(R.id.expandablelist);
    }
    void dbOpen() {
        if(dbManager == null) {
            dbManager = new DBManager(this, "myDB.db", null, 1);
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