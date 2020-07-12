package com.example.aidlapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2020, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: MyBroadcastReceive
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2020/7/3 3:03 PM
 */
public class MyBroadcastReceive extends BroadcastReceiver {
    private String msg = "no msg";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if (action.equals(Const.BROADCAST_STATIC) || action.equals(Const.BROADCAST_DYNAMIC)
                    || action.equals(Const.BROADCAST_LOCAL)) {
                msg = intent.getStringExtra("msg");
                msg = TextUtils.isEmpty(msg) ? "接收失败" : msg;
            }
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
