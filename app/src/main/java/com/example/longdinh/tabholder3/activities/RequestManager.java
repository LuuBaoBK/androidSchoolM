package com.example.longdinh.tabholder3.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Windows on 18/05/2016.
 */
public class RequestManager {

    public String methodGet(String urlinput, String token) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String url_ = Constant.ROOT_API + urlinput;
        try {
            URL url = new URL(url_);
            httpURLConnection = (HttpURLConnection) url.openConnection();



                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty(Constant.X_AUTH, token);


                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }

//                JSONObject jsonObject = new JSONObject(stringBuffer.toString());
//                userinfo_string = jsonObject.getString("data");
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
    }

    public String parentGetSchedule(String urlinput, String token, String id) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String url_ = Constant.ROOT_API + urlinput;
        try {
            URL url = new URL(url_);
            httpURLConnection = (HttpURLConnection) url.openConnection();



            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty(Constant.X_AUTH, token);
            httpURLConnection.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write("data=" + id);
            out.close();

            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

//                JSONObject jsonObject = new JSONObject(stringBuffer.toString());
//                userinfo_string = jsonObject.getString("data");
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
    }

    public String studentGetNotice(String urlinput, String token, String day) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String url_ = Constant.ROOT_API + urlinput;
        try {
            URL url = new URL(url_);
            httpURLConnection = (HttpURLConnection) url.openConnection();



            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty(Constant.X_AUTH, token);
            httpURLConnection.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write("data=" + day);
            out.close();

            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

//                JSONObject jsonObject = new JSONObject(stringBuffer.toString());
//                userinfo_string = jsonObject.getString("data");
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
    }

    public String parentGetNotice(String urlinput, String token, String day, String id) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String url_ = Constant.ROOT_API + urlinput;
        try {
            URL url = new URL(url_);
            httpURLConnection = (HttpURLConnection) url.openConnection();



            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty(Constant.X_AUTH, token);
            httpURLConnection.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write("data=" + day + "&" + "id=" +id);
            out.close();

            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

//                JSONObject jsonObject = new JSONObject(stringBuffer.toString());
//                userinfo_string = jsonObject.getString("data");
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
    }

    public String getTranscript(String urlinput, String token, String id, String thang) {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String url_ = Constant.ROOT_API + urlinput;
        try {
            URL url = new URL(url_);
            httpURLConnection = (HttpURLConnection) url.openConnection();



            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty(Constant.X_AUTH, token);
            httpURLConnection.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write("data=" + thang + "&" + "id=" +id);
            out.close();

            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

//                JSONObject jsonObject = new JSONObject(stringBuffer.toString());
//                userinfo_string = jsonObject.getString("data");
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
    }


    public String getNoticeDetail(String urlinput, String token, String nid, String child) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String url_ = Constant.ROOT_API + urlinput;
        try {
            URL url = new URL(url_);
            httpURLConnection = (HttpURLConnection) url.openConnection();



            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty(Constant.X_AUTH, token);
            httpURLConnection.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write("data=" + nid + "&child=" + child);
            out.close();

            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

//                JSONObject jsonObject = new JSONObject(stringBuffer.toString());
//                userinfo_string = jsonObject.getString("data");
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
    }




    public String getInboxMail(String urlinput, String token, int length) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String url_ = Constant.ROOT_API + urlinput;
        try {
            URL url = new URL(url_);
            httpURLConnection = (HttpURLConnection) url.openConnection();



            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty(Constant.X_AUTH, token);
            httpURLConnection.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write("length=" + length);
            out.close();

            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

//                JSONObject jsonObject = new JSONObject(stringBuffer.toString());
//                userinfo_string = jsonObject.getString("data");
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
    }



    public String postDataToServer(String urlinput, String token, String data) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String url_ = Constant.ROOT_API + urlinput;
        try {
            URL url = new URL(url_);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty(Constant.X_AUTH, token);
            httpURLConnection.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(httpURLConnection.getOutputStream());
            out.write(data);
            out.close();

            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

//                JSONObject jsonObject = new JSONObject(stringBuffer.toString());
//                userinfo_string = jsonObject.getString("data");
            System.out.println(stringBuffer.toString() + " return_data");
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
    }
}
