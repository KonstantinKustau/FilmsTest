package com.example.ibanez_xiphos.itrextest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ibanez_xiphos.itrextest.activity.MainActivity;

import static com.example.ibanez_xiphos.itrextest.service.MyRunnable.CONNECTING_SUCCESS;
import static com.example.ibanez_xiphos.itrextest.activity.MainActivity.PARAM_STATUS;
import static com.example.ibanez_xiphos.itrextest.activity.MainActivity.STATUS_ERROR;
import static com.example.ibanez_xiphos.itrextest.activity.MainActivity.STATUS_SUCCESS;
import static com.example.ibanez_xiphos.itrextest.activity.MainActivity.TAG;

public class MainService extends Service{

    private Handler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {

                Intent intent = new Intent(MainActivity.BROADCAST_ACTION);

                if(msg.what == CONNECTING_SUCCESS) {
                    Log.i(TAG, "CONNECTING_SUCCESS");
                    intent.putExtra(PARAM_STATUS, STATUS_SUCCESS);
                } else {
                    Log.i(TAG, "Ошибка соединения с сервером");
                    intent.putExtra(PARAM_STATUS, STATUS_ERROR);
                }

                sendBroadcast(intent);
            }
        };

        MyRunnable myRunnable = new MyRunnable(this, handler);
        new Thread(myRunnable).start();

        return START_NOT_STICKY;
    }
}
