package com.example.smartbusinesscard2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.GravityCompat;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView nameTv, emailTv;
    private Uri imageUri;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    DBManager dbManager, dbManager2;
    SQLiteDatabase sqLiteDatabase, sqLiteDatabase2;
    Cursor cursor, cursor2;
    RecyclerView list;
    CardmemberAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Cardmember> data1 = null;
    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_CAMERA = 1;

    private TessBaseAPI baseAPI;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void inspectFromBitmap(Bitmap bitmap) {
        System.out.println("inspectFromBitmap");
        Intent intent = new Intent(MainActivity.this,PrintInformation.class);
        baseAPI.setImage(bitmap);
        String text = baseAPI.getUTF8Text();

        String[] strarray;
        strarray = text.split("\n");
        int arraysz = strarray.length;
        for(int i = 0; i < arraysz; i++) {
            if(strarray[i].contains("@")) {
                strarray[i].trim();
                System.out.println("email:" + strarray[i]);
                intent.putExtra("email1", strarray[i]);
                continue;
            }
            //strarray[i].matches(".*[0-9].*")
            if(strarray[i].contains("TEL") || strarray[i].contains("Tel")) {
                strarray[i].trim();
                int start = strarray[i].indexOf("L") + 3;
                if(start < 1)
                    start = strarray[i].indexOf("l") + 3;
                System.out.println("tel:"+strarray[i].substring(start));
                intent.putExtra("tel1", strarray[i].substring(start));
                continue;
            }
            if (strarray[i].contains("FAX") || strarray[i].contains("Fax")) {
                strarray[i].trim();
                int start = strarray[i].indexOf("X") + 3;
                if(start < 1)
                    start = strarray[i].indexOf("x") + 3;
                System.out.println("fax:"+strarray[i].substring(start));
                intent.putExtra("fax1", strarray[i].substring(start));
                continue;
            }
        }
        intent.putExtra("pname", strarray[0]);
        intent.putExtra("position", strarray[1]);
        intent.putExtra("comname", strarray[2]);
        bitmap = null;
        startActivityForResult(intent, 1);
        return;
    }

    private void inspect1(Uri uri) {
        InputStream is = null;
        try {
            System.out.println("inspect1 start");
            is = getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 2;
            options.inScreenDensity = DisplayMetrics.DENSITY_LOW;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
            inspectFromBitmap(bitmap);
            System.out.println("inspect1 before end");
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    System.out.println("is.close()");
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void inspect(Uri uri) {
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            inspectFromBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult Start");

        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        dbOpen();
        cursor = sqLiteDatabase.query("CARDMEMBER", null, null, null, null, null, null);
        data1 = new ArrayList<Cardmember>();
        Cardmember cardmember;
        while(cursor.moveToNext()) {
            cardmember = new Cardmember(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
            cardmember._id = cursor.getInt(0);
            cardmember.p_name = cursor.getString(1);
            cardmember.c_name = cursor.getString(2);
            cardmember.phone = cursor.getString(3);
            cardmember.email = cursor.getString(4);
            cardmember.fax = cursor.getString(5);
            cardmember.position = cursor.getString(6);
            cardmember.op_name = cursor.getString(7);
            cardmember.ophone = cursor.getString(8);
            data1.add(cardmember);
        }
        cursor.close();
        dbClose();
        //sqLiteDatabase.close();
        dbManager.close();


        switch (requestCode) {
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    inspect(data.getData());
                }
                break;
            case REQUEST_CAMERA:
                if (imageUri != null) {
                    System.out.println("onActivityResult's REQUEST_CAMERA");
                    inspect1(imageUri);
                    return;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "ENTIRE");
        adapter.addFragment(new TwoFragment(), "COMPANY");
        adapter.addFragment(new ThreeFragment(), "POSITION");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FontClass.setDefaultFont(this, "DEFAULT", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "MONOSPACE", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SERIF", "NotoSans-Regular.ttf");
        FontClass.setDefaultFont(this, "SANS_SERIF", "NotoSans-Bold.ttf");

        dbOpen();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        list = (RecyclerView)findViewById(R.id.listView);
        list.setHasFixedSize(true);
        list.setLayoutManager(linearLayoutManager);

        cursor = sqLiteDatabase.query("CARDMEMBER", null, null, null, null, null, null);
        data1 = new ArrayList<Cardmember>();
        Cardmember cardmember;
        while(cursor.moveToNext()) {
            cardmember = new Cardmember(cursor.getInt(0), cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
            cardmember._id = cursor.getInt(0);
            System.out.println("_id is : " + cardmember._id);
            cardmember.p_name = cursor.getString(1);
            cardmember.c_name = cursor.getString(2);
            cardmember.phone = cursor.getString(3);
            cardmember.email = cursor.getString(4);
            cardmember.fax = cursor.getString(5);
            cardmember.position = cursor.getString(6);
            cardmember.op_name = cursor.getString(7);
            cardmember.ophone = cursor.getString(8);
            data1.add(cardmember);
        }
        cursor.close();
        dbClose();
        //sqLiteDatabase.close();
        dbManager.close();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EmailActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav_header_view = navigationView.getHeaderView(0);

        nameTv = (TextView)nav_header_view.findViewById(R.id.nav_name);
        emailTv = (TextView)nav_header_view.findViewById(R.id.nav_email);

        dbManager2 = new DBManager(this, "myDB.db", null, 1);
        sqLiteDatabase2 = dbManager2.getReadableDatabase();
        cursor2 = sqLiteDatabase2.query("USER", null, null, null, null, null, null);
        cursor2.moveToFirst();
        nameTv.setText(cursor2.getString(1).toString());
        emailTv.setText(cursor2.getString(4).toString());

        cursor2.close();
        sqLiteDatabase2.close();
        dbManager2.close();

        adapter = new CardmemberAdapter(this, R.layout.card, data1);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(list);

        adapter.setItemClick(new CardmemberAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                dbOpen();
                //System.out.println("onItemClick: " + id);
                System.out.println("onItemClick position:" + position);
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM CARDMEMBER", null);
                Cursor c = cursor;
                c.moveToPosition(position);
                Intent i = new Intent(MainActivity.this, PrintInformation2.class);
                i.putExtra("_id", position);
                i.putExtra("pname", c.getString(c.getColumnIndexOrThrow("p_name")));
                i.putExtra("comname", c.getString(c.getColumnIndexOrThrow("c_name")));
                i.putExtra("tel1", c.getString(c.getColumnIndexOrThrow("phone")));
                i.putExtra("email1", c.getString(c.getColumnIndexOrThrow("email")));
                i.putExtra("fax1", c.getString(c.getColumnIndexOrThrow("fax")));
                i.putExtra("position", c.getString(c.getColumnIndexOrThrow("position")));
                i.putExtra("op_name", c.getString(c.getColumnIndexOrThrow("op_name")));
                i.putExtra("ophone", c.getString(c.getColumnIndexOrThrow("ophone")));
                startActivity(i);
                System.out.println("after startActivity()");

                cursor.close();
                dbClose();
                dbManager.close();
                System.out.println("onclick end");
            }
        });
/*
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            int position1;
            long id1;
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                position1 = position;
                id1 = id;
                System.out.println("onItemLongClick:" + id);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want delete this?")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton){
                                dbOpen();
                                cursor = sqLiteDatabase.rawQuery("SELECT * FROM CARDMEMBER", null);
                                Cursor c1 = cursor;
                                c1.moveToPosition(position1);
                                System.out.println("position:" + position1);
                                System.out.println("id+1:" + (id1 + 1));
                                String sql = "delete from CARDMEMBER" + " where op_name = '"+ c1.getString(c1.getColumnIndexOrThrow("op_name")) + "' and ophone = '" + c1.getString(c1.getColumnIndexOrThrow("ophone")) + "';";
                                System.out.println("sql delete:" + sql);
                                try {
                                    sqLiteDatabase.execSQL(sql);
                                    System.out.println("complete delete");
                                } catch(SQLiteException e) {
                                    System.out.println("error delete:" + e);
                                }

                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton){
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기


                cursor.close();
                dbClose();
                dbManager.close();
                System.out.println("end of Long click");
                return true;
            }
        });
*/
        baseAPI = new TessBaseAPI();
        baseAPI.setDebug(true);

        String path = Environment.getExternalStorageDirectory().getPath() + "/data/tesseract/";
        File file = new File(path + "/tessdata");
        if (!file.exists())
            file.mkdirs();

        try {
            baseAPI.init(path, "eng");
        } catch (Exception e) {
        }
        baseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        System.out.println("end of MainActiity oncreate");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.smartbusinesscard2/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.smartbusinesscard2/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            String filename = System.currentTimeMillis() + ".jpg";

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, filename);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            System.out.println("REQUEST_CAMERA");
            startActivityForResult(intent, REQUEST_CAMERA);
            return true;
        } else if (id == R.id.nav_gallery) {
            Intent intent1 = new Intent();
            intent1.setType("image/*");
            intent1.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent1, REQUEST_GALLERY);
            return true;
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, MyInformation.class));
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(MainActivity.this, MailCheck.class);
            startActivity(i);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(MainActivity.this, EmailActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
