package com.example.smartbusinesscard2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by 현욱 on 2016-11-03.
 * 명함들이 syrup어플처럼 보여져야하는 java파일
 */

public class MainActivity extends AppCompatActivity {

    private Uri imageUri;
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    ListView list;
    CardmemberAdapter adapter;
    ArrayList<Cardmember> data = null;
    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_CAMERA = 1;

    private TessBaseAPI baseAPI;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void inspectFromBitmap(Bitmap bitmap) {
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
        bitmap = null;
        startActivityForResult(intent, 1);
    }

    private void inspect1(Uri uri) {
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 2;
            options.inScreenDensity = DisplayMetrics.DENSITY_LOW;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
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
        switch (requestCode) {
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    inspect(data.getData());
                }
                break;
            case REQUEST_CAMERA:
                if (imageUri != null) {
                    inspect1(imageUri);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        dbManager = new DBManager(this, "myDB.db", null, 1);
        list = (ListView)findViewById(R.id.listView);

        sqLiteDatabase = dbManager.getReadableDatabase();
        cursor = sqLiteDatabase.query("CARDMEMBER", null, null, null, null, null, null);
        data = new ArrayList<Cardmember>();
        Cardmember cardmember;
        while(cursor.moveToNext()) {
            cardmember = new Cardmember();
            cardmember._id = cursor.getInt(0);
            cardmember.p_name = cursor.getString(1);
            cardmember.c_name = cursor.getString(2);
            cardmember.phone = cursor.getString(3);
            cardmember.email = cursor.getString(4);
            cardmember.fax = cursor.getString(5);
            cardmember.position = cursor.getString(6);
            data.add(cardmember);
        }
        cursor.close();
        sqLiteDatabase.close();
        dbManager.close();

        adapter = new CardmemberAdapter(this, R.layout.card, data);
        list.setAdapter(adapter);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_camera:
                String filename = System.currentTimeMillis() + ".jpg";

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_CAMERA);
                break;
            case R.id.action_album:
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent1, REQUEST_GALLERY);
                break;
            case R.id.action_myinfo:
                startActivity(new Intent(this, MyInformation.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
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
}
