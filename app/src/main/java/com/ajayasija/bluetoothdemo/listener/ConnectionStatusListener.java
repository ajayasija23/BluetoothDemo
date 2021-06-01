package com.ajayasija.bluetoothdemo.listener;

import android.bluetooth.BluetoothSocket;

public interface ConnectionStatusListener {
    void onError();
    void onSucces(BluetoothSocket name);
}
