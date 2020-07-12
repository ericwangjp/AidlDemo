package com.example.aidlapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCenter;
    private Button btnBroadcast, btnAidl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();

    }

    private void initData() {
        tvCenter = findViewById(R.id.tv_center);
        btnBroadcast = findViewById(R.id.btn_broadcast);
        btnAidl = findViewById(R.id.btn_aidl);

        btnBroadcast.setOnClickListener(this);
        btnAidl.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_broadcast:
                startActivity(new Intent(MainActivity.this, BroadcastActivity.class));
                break;
            case R.id.btn_aidl:
                startActivity(new Intent(MainActivity.this, AidlClientActivity.class));
                break;
            default:
                break;
        }
    }
}
