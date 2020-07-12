package com.example.serverapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2020, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: Person
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2020/7/7 5:32 PM
 */
public class NewPerson implements Parcelable {
    public String name;
    public int age;
    public boolean pass;

//    public NewPerson() {
//    }

    protected NewPerson(Parcel in) {
        name = in.readString();
        age = in.readInt();
        pass = in.readByte() != 0;
    }

    public static final Creator<NewPerson> CREATOR = new Creator<NewPerson>() {
        @Override
        public NewPerson createFromParcel(Parcel in) {
            return new NewPerson(in);
        }

        @Override
        public NewPerson[] newArray(int size) {
            return new NewPerson[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeByte((byte) (pass ? 1 : 0));
    }
}
