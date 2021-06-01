package com.ajayasija.bluetoothdemo.threads;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.ajayasija.bluetoothdemo.listener.ConnectionStatusListener;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter bluetoothAdapter;
    private final ConnectionStatusListener listener;
    private final String MY_UUID="30096f4b-6403-4fc5-a439-b3575ed64fed";

    public ConnectThread(String device,ConnectionStatusListener listener) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        this.listener=listener;
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        BluetoothSocket tmp = null;
        mmDevice = bluetoothAdapter.getRemoteDevice(device);

        try {
            tmp = mmDevice.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
        } catch (IOException e) {
            Log.e("exception", "Socket's create() method failed", e);
            listener.onError();
        }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            listener.onError();
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e("exception", "Could not close the client socket", closeException);
            }
            return;
        }
        Log.d("succeeded",mmSocket.getRemoteDevice().getName());
        listener.onSucces(mmSocket);
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("exception", "Could not close the client socket", e);
        }
    }
}
