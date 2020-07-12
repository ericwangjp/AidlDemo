package com.example.aidlapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BroadcastActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCenter;
    private Button btnSendLocal, btnSendStatic, btnSendDynamic;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceive myBroadcastReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        initData();

        registerReceivers();
    }

    private void initData() {
        tvCenter = findViewById(R.id.tv_center);
        btnSendLocal = findViewById(R.id.btn_send_local);
        btnSendStatic = findViewById(R.id.btn_send_static);
        btnSendDynamic = findViewById(R.id.btn_send_dynamic);

        btnSendLocal.setOnClickListener(this);
        btnSendStatic.setOnClickListener(this);
        btnSendDynamic.setOnClickListener(this);
    }


    private void registerReceivers() {
//        注册本地静态广播
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter(Const.BROADCAST_LOCAL);
        myBroadcastReceive = new MyBroadcastReceive();
        localBroadcastManager.registerReceiver(myBroadcastReceive, intentFilter);

//        动态注册广播
        IntentFilter intentFilterDynamic = new IntentFilter(Const.BROADCAST_DYNAMIC);
        registerReceiver(myBroadcastReceive, intentFilterDynamic);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_local:
//                发送本地广播，注册、发送、注销都必须通过LocalBroadcastManager发送，不需要设置Component
                Intent intent = new Intent(Const.BROADCAST_LOCAL);
                intent.putExtra("msg", "this is local broadcast");
                localBroadcastManager.sendBroadcast(intent);
                break;
            case R.id.btn_send_static:
//                发送静态广播，广播需要在AndroidManifest注册，需要指定Component，无法注销
                Intent intent2 = new Intent(Const.BROADCAST_STATIC);
                intent2.putExtra("msg", "this is static broadcast");
                intent2.setComponent(new ComponentName(getPackageName(), getPackageName() + ".MyBroadcastReceive"));
                sendBroadcast(intent2);
                break;
            case R.id.btn_send_dynamic:
//                发送动态注册的广播，无需注册和设置Component，需要注销
                Intent intent3 = new Intent(Const.BROADCAST_DYNAMIC);
                intent3.putExtra("msg", "this is dynamic broadcast");
                sendBroadcast(intent3);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(myBroadcastReceive);
        unregisterReceiver(myBroadcastReceive);
    }
}
