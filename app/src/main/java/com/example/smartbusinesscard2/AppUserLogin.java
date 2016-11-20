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
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 현욱 on 2016-11-03.
 * 앱초기 실행시에 사용자 정보 등록을 위한 페이지
 * 앱이 실행되고 사용자의 정보가 db에 저장된 후에는 나타나지 않음
 */
public class AppUserLogin extends AppCompatActivity {

    Button cameraBtn = null;
    private static final int REQUEST_CAMERA = 1;
    private TessBaseAPI baseAPI;
    private Uri imageUri;
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            System.out.println("AppUserLogin onCreate");
            dbOpen();
            ContentValues values = new ContentValues();
            cursor = sqLiteDatabase.rawQuery("select * from USER", null);
            int num = cursor.getCount();
            System.out.println(num);
            if(num == 0) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.app_user_login);
                baseAPI = new TessBaseAPI();
                baseAPI.setDebug(true);

                cameraBtn = (Button)findViewById(R.id.registerBtn);
                cameraBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String filename = System.currentTimeMillis() + ".jpg";

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, filename);
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                        Intent intent = new Intent();
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }
                });

                String path = Environment.getExternalStorageDirectory().getPath() + "/data/tesseract/";
                File file = new File(path + "/tessdata");
                if (!file.exists())
                    file.mkdirs();
                try {
                    baseAPI.init(path, "eng");
                } catch(Exception e){

                }
                baseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT);
                //baseAPI.end();
            }
            else {
                super.onCreate(savedInstanceState);
                System.out.println("true");
                finish();
                System.out.println("finish()!");
                startActivity(new Intent(this, MainActivity.class));
                System.out.println("startActivity(main)");
            }
        } catch(Exception e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (imageUri != null) {
            inspect(imageUri);
        }
    }

    private void inspectFromBitmap(Bitmap bitmap) {
        Intent intent = new Intent(AppUserLogin.this,DialogCardInformation.class);
        baseAPI.setImage(bitmap);
        String text = baseAPI.getUTF8Text();
        System.out.println(text + "");
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
    }

    private void inspect(Uri uri) {
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

