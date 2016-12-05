package com.example.smartbusinesscard2;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by yi_te on 2016-11-30.
 */
public class SelectPosition extends Activity {
    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildListContent = null;
    private ArrayList<String> mChildListContent2 = null;
    private ArrayList<String> mChildListContent3 = null;
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor, cursor2, cursor3, cursor4;
    static String MyPosition;
    static String same_add, se_add, ju_add, Address;
    int user_rank, mem_rank;
    String name, senior, junior;
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
        mGroupList.add("Senior");
        mGroupList.add("My Position");
        mGroupList.add("Junior");

        ok = (Button) findViewById(R.id.s_button);
        mChildList = new ArrayList<ArrayList<String>>();

        mChildListContent = new ArrayList<String>();
        mChildListContent2 = new ArrayList<String>();
        mChildListContent3 = new ArrayList<String>();

        insert("Professor", 1);
        insert("Associate Professor", 1);
        insert("Chairman", 1);
        insert("President", 1);
        insert("CEO", 1);
        insert("Vice Chairman", 2);
        insert("Vice President", 2);
        insert("Representative Director", 3);
        insert("Managing Director", 3);
        insert("Senior Managing Director", 3);
        insert("Director", 3);
        insert("Manager", 4);
        insert("General Manager", 4);
        insert("Assistant Manager", 5);
        insert("Deputy General Manager", 5);
        insert("Staff", 6);

        /*
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Professor', 1);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Associate Professor', 1);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Chairman', 1);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('President', 1);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('CEO', 1);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Vice Chairman', 2);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Vice President', 2);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Representative Director', 3);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Managing Director', 3);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Senior Managing Director', 3);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Director', 3);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Manager', 4);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('General Manager', 4);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Assistant Manager', 5);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Deputy General Manager', 5);");
        sqLiteDatabase.execSQL("insert into POSITION (pos, rank) values ('Staff', 6);");
        dbClose();
*/

        dbOpen();
        cursor = sqLiteDatabase.query("USER", null, null, null, null, null, null);
        while(cursor.moveToNext()) {
            MyPosition = null;
            MyPosition = cursor.getString(6); // user의 position
        }
        cursor.close();
        System.out.println("!!!!!!22222222222!" + MyPosition);
        cursor4 = sqLiteDatabase.rawQuery("SELECT * FROM POSIT WHERE pos='"
                + MyPosition
                + "'", null);
        while(cursor4.moveToNext())
            user_rank = cursor4.getInt(2); // user의 position의 rank
        cursor4.close();
        System.out.println("@@@@@@@@@@@@user" + user_rank);
        cursor2 = sqLiteDatabase.rawQuery("SELECT * FROM CARDMEMBER", null);
        while(cursor2.moveToNext()){
            cursor3 = sqLiteDatabase.rawQuery("SELECT * FROM POSIT WHERE pos='"
                    + cursor2.getString(6)
                    + "'", null);
            while(cursor3.moveToNext())
                mem_rank = cursor3.getInt(2);
            if(user_rank > mem_rank){
                senior = cursor2.getString(1);
                if(cursor2.getString(4) != null) {
                    se_add += cursor2.getString(4);
                    se_add += ",";
                }
            }
            else if(user_rank == mem_rank){
                name = cursor2.getString(1);
                if(cursor2.getString(4) != null) {
                    same_add += cursor2.getString(4);
                    same_add += ",";
                }
            }
            else if(user_rank < mem_rank){
                junior = cursor2.getString(1);
                if(cursor2.getString(4) != null) {
                    ju_add += cursor2.getString(4);
                    ju_add += ",";
                }
            }
            System.out.println("@@@@@@@@@@@@mem" + mem_rank);

            mChildListContent.add(senior);
            mChildListContent2.add(name);
            mChildListContent3.add(junior);
        }
        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent2);
        mChildList.add(mChildListContent3);

        cursor3.close();
        cursor2.close();
        dbClose();
        dbManager.close();

        setLayout();

        mListView.setAdapter(new BaseExpandableAdapter(this, mGroupList, mChildList));

        // 그룹 클릭 했을 경우 이벤트
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if(groupPosition == 0) {
                    Address = se_add;
                    Toast.makeText(getApplicationContext(), "Select My Company",
                            Toast.LENGTH_SHORT).show();
                    System.out.println("EmailAddress = " + Address);
                }
                else if(groupPosition == 1) {
                    Address = same_add;
                    Toast.makeText(getApplicationContext(), "Select Not My Company",
                            Toast.LENGTH_SHORT).show();
                    System.out.println("EmailAddress = " + Address);
                }
                else if(groupPosition == 2) {
                    Address = ju_add;
                    Toast.makeText(getApplicationContext(), "Select Not My Company",
                            Toast.LENGTH_SHORT).show();
                    System.out.println("EmailAddress = " + Address);
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
                Toast.makeText(getApplicationContext(), "c click = " + childPosition,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // 그룹이 닫힐 경우 이벤트
        mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), "g Collapse = " + groupPosition,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 그룹이 열릴 경우 이벤트
        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), "g Expand = " + groupPosition,
                        Toast.LENGTH_SHORT).show();
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

    public void insert(String pos, int rank){
        dbOpen();
        ContentValues values = new ContentValues();
        values.put("pos", pos);
        values.put("rank", rank);
        sqLiteDatabase.insert("POSIT", null, values);
        dbClose();
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