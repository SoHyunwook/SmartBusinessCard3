package com.example.smartbusinesscard2;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by 현욱 on 2016-11-03.
 */
public class AppUserLogin  extends AppCompatActivity {
    Button cameraBtn = null;
    private static final int REQUEST_CAMERA = 1;
    private TessBaseAPI baseAPI;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (imageUri != null) {
            inspect(imageUri);
        }
    }

    private void inspectFromBitmap(Bitmap bitmap) {
        Intent intent = new Intent();
        baseAPI.setImage(bitmap);
        String text = baseAPI.getUTF8Text();
        System.out.println(text +"");
        String[] strarray;
        strarray = text.split("\n");
        int arraysz = strarray.length;
        for(int i = 0; i < arraysz; i++) {
            if(strarray[i].contains("@")) {
                strarray[i].trim();
                intent.putExtra("email", strarray[i]);
                continue;
            }
            //strarray[i].matches(".*[0-9].*")
            if(strarray[i].contains("TEL") || strarray[i].contains("Tel")) {
                strarray[i].trim();
                int start = strarray[i].indexOf("L");
                if(start < 1)
                    start = strarray[i].indexOf("l");
                intent.putExtra("tel", strarray[i].substring(start));
                continue;
            }
            if (strarray[i].contains("FAX") || strarray[i].contains("Fax")) {
                strarray[i].trim();
                int start = strarray[i].indexOf("X");
                if(start < 1)
                    start = strarray[i].indexOf("x");
                intent.putExtra("fax", strarray[i].substring(start));
                continue;
            }
        }
        bitmap = null;
        startActivity(new Intent(this, DialogCardInformation.class));
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
}

