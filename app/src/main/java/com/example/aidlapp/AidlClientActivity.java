package com.example.aidlapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.serverapp.IDownloadCallback;
import com.example.serverapp.IMyAidlInterface;
import com.example.serverapp.NewPerson;

import java.util.List;

public class AidlClientActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AidlClientActivity";
    private Button btnBind, btnUnbind;
    private boolean isBind;
    private IMyAidlInterface iMyAidlInterface;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ComponentName=" + name);
            isBind = true;
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                iMyAidlInterface.asBinder().linkToDeath(deathRecipient, 0);
                iMyAidlInterface.regisgerCallback(callback);
                iMyAidlInterface.setName("name from client");
                String getName = iMyAidlInterface.getName();
                Log.e(TAG, "onServiceConnected: getName= " + getName);
//                Log.e(TAG, "onServiceConnected: getPerson=" + iMyAidlInterface.getPerson(new NewPerson()).pass);
                Log.e(TAG, "onServiceConnected: getPerson=" + iMyAidlInterface.getPerson(new NewPerson()).name);
//                Log.e(TAG, "onServiceConnected: getPerson=" + iMyAidlInterface.getPerson(new NewPerson()).age);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ComponentName= " + name);
            try {
                iMyAidlInterface.unRegisgerCallback(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            isBind = false;
            iMyAidlInterface = null;
        }
    };

    private IDownloadCallback callback = new IDownloadCallback.Stub() {
        @Override
        public void download(NewPerson newPerson) throws RemoteException {
            Log.e(TAG, "download: " + newPerson.age);
        }
    };

    IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.e(TAG, "binderDied: " + "意外死亡");
            if (iMyAidlInterface != null) {
                iMyAidlInterface.asBinder().unlinkToDeath(deathRecipient, 0);
                iMyAidlInterface = null;
//                走重新绑定逻辑
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_client);
        initData();
    }

    private void initData() {
        btnBind = findViewById(R.id.btn_bind);
        btnUnbind = findViewById(R.id.btn_unbind);

        btnBind.setOnClickListener(this);
        btnUnbind.setOnClickListener(this);
//        startActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bind:
//                判断控件已安装，但是服务不存在时，启动服务端
//                Intent intentOther = new Intent();
//                intentOther.setClassName("com.example.serverapp","com.example.serverapp.MainActivity");
//                intentOther.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intentOther);

                Intent intentMain = new Intent();
                intentMain.setComponent(new ComponentName("com.example.serverapp", "com.example.serverapp.MainActivity"));
                intentMain.setAction(Intent.ACTION_MAIN);
                intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intentMain, 1001);

//                Log.d(TAG, "onClick: 开始绑定服务");
//                Intent intent = new Intent("com.example.serveraidl");
//                intent.setComponent(new ComponentName("com.example.serverapp", "com.example.serverapp.AidlService"));
//                boolean bindService = getApplicationContext().bindService(intent, serviceConnection, BIND_AUTO_CREATE);
//                Log.d(TAG, "onClick: 绑定服务结果=" + bindService);
                break;
            case R.id.btn_unbind:
                Log.d(TAG, "onClick: 解绑服务");
                if (isBind) {
                    unbindService(serviceConnection);
                    iMyAidlInterface = null;
                    isBind = false;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onClick: 开始绑定服务");
                    Intent intent = new Intent("com.example.serveraidl");
                    intent.setComponent(new ComponentName("com.example.serverapp", "com.example.serverapp.AidlService"));
                    boolean bindService = bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                    Log.d(TAG, "onClick: 绑定服务结果=" + bindService);
//                Intent var2 = new Intent("com.example.serveraidl");
//                var2.setPackage(getPackageName());
//                var2.setComponent(new ComponentName("com.example.serverapp", "com.example.serverapp.AidlService"));
//                Intent var3 = new Intent(getIntents(AidlClientActivity.this, var2));
//                boolean var4 = bindService(var3, serviceConnection, BIND_AUTO_CREATE);
//                Log.d(TAG, "onClick: 绑定服务结果=" + var4);
                }
            }, 1000);
        }
    }

    private static Intent getIntents(Context var0, Intent var1) {
        PackageManager var2 = var0.getPackageManager();
        List var3 = var2.queryIntentServices(var1, 0);
        Log.e(TAG, "getIntents: " + var3);
        if (var3 != null && var3.size() == 1) {
            ResolveInfo var4 = (ResolveInfo) var3.get(0);
            String var5 = var4.serviceInfo.packageName;
            String var6 = var4.serviceInfo.name;
            Log.e(TAG, "getIntents: var5= " + var5);
            Log.e(TAG, "getIntents: var6= " + var6);
            ComponentName var7 = new ComponentName(var5, var6);
            Intent var8 = new Intent(var1);
            var8.setComponent(var7);
            return var8;
        } else {
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            iMyAidlInterface.unRegisgerCallback(callback);
            iMyAidlInterface.asBinder().unlinkToDeath(deathRecipient, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
