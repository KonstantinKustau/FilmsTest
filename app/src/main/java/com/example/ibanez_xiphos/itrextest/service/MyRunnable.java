package com.example.ibanez_xiphos.itrextest.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;

import com.example.ibanez_xiphos.itrextest.database.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.ibanez_xiphos.itrextest.activity.MainActivity.TAG;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_DESCRIPTION;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_IMAGE;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_NAME;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_NAME_ENG;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_PREMIERE;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.TABLE_FILMS;

public class MyRunnable implements Runnable {

    private final String SERVER_NAME = "http://www.mocky.io/v2/57cffac8260000181e650041";
    public static final int CONNECTING_SUCCESS = 0;
    public static final int ERROR_CONNECTING = 1;
    public static final int ERROR_SERVER_NOT_RESPONSE = 2;
    public static final int ERROR_INVALID_JSON = 3;
    public static final int ERROR_NOT_CONTAINS_JSON = 4;

    private Context context;
    private String answer;
    private HttpURLConnection urlConnection;
    private Handler handler;

    public MyRunnable(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void run() {

        try {
            Log.i(TAG, "Соединение...");

            urlConnection = (HttpURLConnection) new URL(SERVER_NAME).openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
        } catch (Exception e) {
            Log.i(TAG, "Ошибка соединения: " + e.getMessage());
            handler.sendEmptyMessage(ERROR_CONNECTING);
        }

        try {
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String bfr_st;

            while ((bfr_st = br.readLine()) != null) {
                sb.append(bfr_st);
                Log.i(TAG, "" + bfr_st + "\n");
            }
            Log.i(TAG, "Пришел ответ сервера!");
            Log.i(TAG, "Полный ответ сервера:\n" + sb.toString());

            answer = sb.toString();
            answer = answer.substring(answer.indexOf("["), answer.indexOf("]") + 1);

            is.close();
            br.close();

        } catch (Exception e) {
            Log.i(TAG, "Нет ответа сервера: " + e.getMessage());
            handler.sendEmptyMessage(ERROR_SERVER_NOT_RESPONSE);
        } finally {
            urlConnection.disconnect();
            Log.i(TAG, "Соединение закрыто");
        }

        if (answer != null && !answer.trim().equals("")) {
            Log.i(TAG, "Ответ содержит JSON:\n");

            try {
                JSONArray ja = new JSONArray(answer);
                JSONObject jo;

                int i = 0;

                DBHelper dbHelper = new DBHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                int clearCount = db.delete(TABLE_FILMS, null, null);
                Log.i(TAG, "delete: rows count = " + clearCount);

                while (i < ja.length()) {
                    jo = ja.getJSONObject(i);

                    ContentValues cv = new ContentValues();
                    cv.put(FILMS_IMAGE, jo.getString("image"));
                    cv.put(FILMS_NAME, jo.getString("name"));
                    cv.put(FILMS_NAME_ENG, jo.getString("name_eng"));
                    cv.put(FILMS_PREMIERE, jo.getString("premiere"));
                    cv.put(FILMS_DESCRIPTION, jo.getString("description"));
                    long rowID = db.insert(TABLE_FILMS, null, cv);
                    Log.i(TAG, "rowID = " + rowID);

                    i++;
                }

                dbHelper.close();
                handler.sendEmptyMessage(CONNECTING_SUCCESS);

            } catch (Exception e) {
                Log.i(TAG, "Ответ сервера содержит не валидный JSON: " + e.getMessage());
                handler.sendEmptyMessage(ERROR_INVALID_JSON);
            }
        } else {
            Log.i(TAG, "Ответ не содержит JSON");
            handler.sendEmptyMessage(ERROR_NOT_CONTAINS_JSON);
        }
    }
}
