package com.example.serverapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2020, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: AidlService
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2020/7/5 3:12 PM
 */
public class AidlService extends Service {
    private static final String TAG = "===> AidlService";
    private RemoteCallbackList<IDownloadCallback> remoteCallbackList = new RemoteCallbackList<>();
    private NewPerson newPerson;

    public AidlService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: onBind");
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: onDestroy");
        remoteCallbackList.kill();
    }

    class MyBinder extends IMyAidlInterface.Stub {
        private String name;

        @Override
        public void setName(String name) throws RemoteException {
            Log.d(TAG, "setName: " + name);
            this.name = name;
        }

        @Override
        public String getName() throws RemoteException {
            Log.d(TAG, "getName: ");
            return name + "[server]";
        }

        @Override
        public NewPerson getPerson(NewPerson person) throws RemoteException {
            person.age = 10;
            person.name = "jack";
            person.pass = true;
            newPerson = person;
            handler.sendEmptyMessage(100);
            return person;
        }

        @Override
        public void registerCallback(IDownloadCallback callBack) throws RemoteException {
            if (callBack != null) {
                remoteCallbackList.register(callBack);
            }
        }

        @Override
        public void unRegisterCallback(IDownloadCallback callBack) throws RemoteException {
            if (callBack != null) {
                remoteCallbackList.unregister(callBack);
            }
        }

    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int i = remoteCallbackList.beginBroadcast();
            Log.e(TAG, "handleMessage: --------------------------->");
            Log.e(TAG, "handleMessage: " + i);
            while (i > 0) {
                i--;
                newPerson.age += 1;
                try {
                    remoteCallbackList.getBroadcastItem(i).download(newPerson);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "handleMessage: -->age: " + newPerson.age);
                if (newPerson.age < 100) {
                    handler.sendEmptyMessageDelayed(100, 1000);
                }
            }
            remoteCallbackList.finishBroadcast();
        }
    };

}
