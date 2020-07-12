// IMyAidlInterface.aidl
package com.example.serverapp;

// Declare any non-default types here with import statements
import com.example.serverapp.NewPerson;
import com.example.serverapp.IDownloadCallback;

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

      void setName(String name);
      String getName();
      NewPerson getPerson(in NewPerson person);
      void regisgerCallback(in IDownloadCallback callBack);
      void unRegisgerCallback(in IDownloadCallback callBack);
}
