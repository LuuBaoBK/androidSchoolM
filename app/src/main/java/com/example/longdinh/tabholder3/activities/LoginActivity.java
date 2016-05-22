package com.example.longdinh.tabholder3.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

public class LoginActivity extends AppCompatActivity {
    Button button;
    TextView tvEmail;
    TextView tvPassword;
    String userinfo_string;
    String email;
    String password;
    List<NameValuePair> data = new ArrayList<>();
    static final String WRONGPASS = "Id or Password is incorrect\n";
    Boolean isSaved = false;
    String dataInfo = null;


    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        button = (Button) findViewById(R.id.btnLogin);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        this.loading();
        if(dataInfo != null){
            Intent Idashboard = new Intent(getApplicationContext(), MainActivity.class);
            Idashboard.putExtra("userinfo_string",dataInfo);
            startActivity(Idashboard);
            Idashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
        }
    }

    public void login(View v) throws IOException {
        String email_pattern = "[a|t|s|p]_[0-9]{7}@schoolm.com";
        email = tvEmail.getText().toString();
        password = tvPassword.getText().toString();
        if (email.matches(email_pattern)) {
            if (password.length() >= 4) {
                System.out.println("call connect");
                new runLogin().execute();
            } else {
                tvPassword.setError("Password is more than 4 characters");
            }
        } else {
            tvEmail.setError("Wrong email format (Ex: s_0000000@schoolm.com)");
        }
    }

    public void loading(){
        System.out.println("loading datainfor------");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        dataInfo = sp.getString("dataInfo", null);
        System.out.println("data info----" + dataInfo);
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
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.longdinh.tabholder3/http/host/path")
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
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.longdinh.tabholder3/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class runLogin extends AsyncTask<String, String , String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String url_ = Constant.ROOT_API + "api/login";
            try {
                URL url = new URL(url_);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
                out.write("email=" + email + "&password=" + password);
                out.close();

                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
//                    System.out.println(line);
                }

//                JSONObject jsonObject = new JSONObject(stringBuffer.toString());
//                userinfo_string = jsonObject.getString("data");
                System.out.println("1"+stringBuffer.toString());
                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "e1";
            } catch (IOException e) {
                e.printStackTrace();
                return "e2";
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
                try {
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "e4";
                }
            }
//            System.out.println("GEt data from server...");
//            return "{\"id\":\"t_00000013\",\"email\":\"t_0000013@schoolm.com\",\"role\":\"1\",\"fullname\":\"Trịnh Hiếu Vân\",\"token\":\"4ad2b006ff575c89d0c30fdf8b5f2b6a9f4b6a90\"}";
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            System.out.println("a" + result + "2");
            if (!result.equals(WRONGPASS)) {
                Intent Idashboard = new Intent(getApplicationContext(), MainActivity.class);
                Idashboard.putExtra("userinfo_string",result);
                startActivity(Idashboard);
                Idashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            } else {
                tvEmail.setError(WRONGPASS);
                tvPassword.setError(WRONGPASS);
                System.out.println(result);
                return;
            }
        }
    }
}

