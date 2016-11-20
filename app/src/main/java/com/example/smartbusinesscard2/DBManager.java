package com.example.smartbusinesscard2;

/**
 * Created by 현욱 on 2016-11-03.
 * db관리 파일
 */
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBManager extends SQLiteOpenHelper {
    Context context;
    public DBManager(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USER (_id INTEGER PRIMARY KEY AUTOINCREMENT, u_name TEXT, c_name Text," +
                "phone TEXT, email TEXT, fax TEXT, position TEXT);");
        db.execSQL("CREATE TABLE CARDMEMBER (_id INTEGER PRIMARY KEY AUTOINCREMENT, p_name TEXT, c_name TEXT, " +
                "phone TEXT, email TEXT, fax TEXT, position TEXT, op_name TEXT, ophone TEXT);");
        db.execSQL("CREATE TABLE EMAIL(_id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT, date TEXT," +
                "to_whom TEXT);");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS "+ );
        onCreate(db);
    }
}