package com.example.smartbusinesscard2;

import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.gmail.model.*;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by yi_te on 2016-11-09.
 */

public class APIConnected extends Activity {
    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    ProgressDialog mProgress;
    private EmailActivity get = new EmailActivity();
    String htmlText;
    String url;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { "https://www.googleapis.com/auth/gmail.send" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout activityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(tlp);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        activityLayout.addView(mOutputText);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Gmail API ...");

        setContentView(activityLayout);

        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            refreshResults();
        } else {
            mOutputText.setText("Google Play Services required: " +
                    "after installing, close and relaunch this app.");
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    mOutputText.setText("Account unspecified.");
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshResults() {
        if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
                new MakeRequestTask(mCredential).execute();
            } else {
                mOutputText.setText("No network connection available.");
            }
        }
    }

    private void chooseAccount() {
        startActivityForResult(
                mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                connectionStatusCode,
                APIConnected.this,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                sendMail();
                return null;
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private Message MimeMessage2Message(MimeMessage msg) throws MessagingException, IOException
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            msg.writeTo(baos);
            String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
            Message message = new Message();
            message.setRaw(encodedEmail);
            return message;
        }
        private void sendMail() throws IOException
        {
            String str = get.s_message;
            str += '\n';
            String str2 = "";
            int start=0, end;
            for(int i=0; i<str.length(); i++){
                if(str.charAt(i) == '\n'){
                    end = i;
                    str2 += str.substring(start, end);
                    str2 +="<br/>";
                    start = i+1;
                }
            }
            System.out.println("!!!!!!!!!!!!!!!!" + str2);
            Properties props = System.getProperties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage mimeMessage = new MimeMessage(session);
            Message msg = null;
//            url = "http://imageshack.com/a/img924/876/NUnz8W.png";
            url = "http://imageshack.com/a/img924/139/uI27Ri.jpg";
            htmlText = "<HTML> <HEAD> <TITLE> </TITLE> </HEAD> <BODY> <table background = \""
                    + url
                    + "\" width=\"690\" height=\"471\" style=\"table-layout:fixed\">"
                    + " <tr> <td height=\"auto\" style=\"word-break:break-all\">"
                    + " <div style=\"padding-left: 50px; padding-right: 100px;\"> <h1>"
                    + str2
                    + " </div> </td> </tr>"
                    + " </BODY>"
                    + " </HTML>";
            /*
            htmlText = "<HTML>"
                    + "<HEAD> <TITLE> </TITLE> </HEAD>"
                    + "<BODY> <table background=\""
                    + url
                    + "\" width=\"210\" height=\"336\" style=\"table-layout:fixed\">"
                    + "<tr> <td> </td> </tr> <tr> <td height=\"auto\" style=\"word-break:break-all\">"
                    + "<div style=\"padding-left: 5px; padding-right: 5px;\">"
                    + get.s_message
                    + "</div> </td> </tr>"
                    + "</BODY>"
                    + "</HTML>";
*/
            System.out.println("htmlText = " + htmlText);

            try{
                for(int i=0; i<get.count; i++) {
                    mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(String.format("%s", get.address2.get(i))));
                    mimeMessage.setSubject(String.format("%s", get.s_subject));
                    // mimeMessage.setText(String.format("%s", get.s_message));
//                    mimeMessage.setContent(htmlText, "text/html; charset=utf-8");
                    mimeMessage.setContent(htmlText, "text/html; charset=utf-8");
                    msg = MimeMessage2Message(mimeMessage);
                    System.out.println("2222222222" + get.address2.get(i));
                    System.out.println("count = " + get.count);
                }
                get.count=0;
                System.out.println("count = " + get.count);
            }
            catch (Exception e){
                return;
            }
            mService.users().messages().send("me", msg ).execute();
        }

        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
                mOutputText.setText("Send Mail!");
            } else {
                output.add(0, "Data retrieved using the Gmail API:");
                mOutputText.setText(TextUtils.join("\n", output));
            }
//            finish();
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            APIConnected.REQUEST_AUTHORIZATION);
                } else {
                    mOutputText.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                mOutputText.setText("Request cancelled.");
            }
        }
    }
}