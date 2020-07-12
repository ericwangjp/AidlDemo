// IDownloadCallback.aidl
package com.example.serverapp;

// Declare any non-default types here with import statements
import com.example.serverapp.NewPerson;
interface IDownloadCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void download(in NewPerson newPerson);
}
