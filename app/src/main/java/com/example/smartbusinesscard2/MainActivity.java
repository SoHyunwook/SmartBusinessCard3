package com.example.smartbusinesscard2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_CAMERA = 1;

    private TessBaseAPI baseAPI;


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button :
                /*
                버튼 클릭하면 일단 갤러리 사진 요청 합니다.
                이 부분은 그냥 가져다 썼습니다. 해보질 않아서
                 */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_GALLERY);
                break;
        }
    }

    private void setTextInTextView(String str) {
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(str);
    }

    private void inspectFromBitmap(Bitmap bitmap) {
        /*
        생성자에서 baseAPI를 만들어주었지요.
        recycle 메소드를 통한 재사용과 사용 종료 후에는 end()메소드를 호출하는 등
        추가 메소드가 있습니다.
        밑에는 이제 setImage 메소드로 비트맵 설정 후
        getUTF8Text() 메소드로 이미지안의 텍스트를 추출하여 반환 합니다.
        이를 텍스트에 표시하여 줍니다.
         */
        baseAPI.setImage(bitmap);
        String text = baseAPI.getUTF8Text();
        setTextInTextView(text);
        bitmap = null;
        //bitmap.recycle();
    }

    private void inspect(Uri uri) {
        InputStream is = null;

        try {
            /*
            받은 경로에서 이미지를 추출합니다. 여기서 BitmapFactory.Options를 통해서
            해상도 수정하는 부분인데, 이 테스트코드를 썼던 분이 고화질 이미지를 다루어서 그런지...
            잘 모르겠지만, 이 부분을 처음에 적용시키고 한 결과 인식률이 상당히 낮게 나왔습니다.
            하지만 인식률이 낮다는 반응은 별로 없었고, 밑에 코드를 주석처리하여 원하는 텍스트
            추출이 가능했습니다. 멋모르고 가져다쓴 결과죠....
             */
            is = getContentResolver().openInputStream(uri);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            options.inSampleSize = 2;
//            options.inScreenDensity = DisplayMetrics.DENSITY_LOW;
//            Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
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

        /*
        갤러리에서 사진 선택을 선택했으면, 해당 갤러리 인텐트로부터 받은 정보
        즉 경로 Uri가 되겟죠. 이를 insepct 메소드에 넘겨줍니다.
         */
        switch (requestCode) {
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    inspect(data.getData());
                }
                break;
            default :
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("MainActivity");

        /*
        버튼 이벤트를 부여해줍니다.
         */
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        /*
        TessBaseAPI가 Tesseract OCR을 사용하게 될 클래스 입니다.
        setDebug 설정은 정확히 추가적으로 무슨 기능을 하는지 모르겠으나, 일단 목적은 디버그 이므로
        설정을 해줬습니다.
         */
        baseAPI = new TessBaseAPI();
        baseAPI.setDebug(true);

        /*
        제 휴대폰 상 storage/emulated/0/data/tesseract/ 위치 입니다.
        tesseract안에는 tessdata 폴더가있어야 되므로 만들어 주었습니다.
        mkdir -> mkdirs 메소드는 상위폴더까지 만들어 줍니다.
         */
        String path = Environment.getExternalStorageDirectory().getPath() + "/data/tesseract/";
        File file = new File(path + "/tessdata");
        if (!file.exists())
            file.mkdirs();

        /*
        테스트시 사용했던 코드 입니다. 권한 없이 폴더가 생성 및 존재유무가 확인 되지 않는 부분
        테스트할때 사용했습니다.
         */
        //setTextInTextView((result == true) ? "true" : "false");
        //setTextInTextView(file.getPath());

        /*
        init 메소드의 에러메세지를 캐치하기 위해 적어놓았으며,
        github의 eng.tessdata파일을 tessdata폴더 안에 추가함으로써 문제는 해결 됬습니다.
         */
        try {
            baseAPI.init(path, "eng");
        } catch(Exception e){
            setTextInTextView(path + "||" + e.toString());
        }

        /*
        이 부분은 텍스트 추출시 사용할 문자와 추출하지 않을 문자를 구분하는 부분인 듯 한데,
        인식률이 낮아서 원인 찾으려고 사용했으나, 해상도 문제 해결 후 주석 처리 했습니다.
        setPageSegMode는 https://rmtheis.github.io/tess-two/javadoc/com/googlecode/tesseract/android/TessBaseAPI.PageSegMode.html
        링크에 각 옵션 별 설명이 적혀있습니다. 일단 저는 사진상에 여러 텍스트가 있으므로, 사용하면 되지 않을까해서
        사용했습니다.
         */
        //baseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        //baseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-[]}{;:'\"\\|~`,./<>?");
        baseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT);
        //baseAPI.end();
    }
}
