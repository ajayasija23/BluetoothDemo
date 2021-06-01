package com.ajayasija.bluetoothdemo.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothUtil {

    private static BluetoothUtil instance;


    public List<BluetoothDevice> getConnectedDevices(BluetoothAdapter bluetoothAdapter){
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        List<BluetoothDevice> list=new ArrayList<>();

        for (BluetoothDevice device:bondedDevices){
            list.add(device);
        }
        Log.d("connected_devices",list.toString());
        return list;
    }

    public static BluetoothUtil getInstance(){
        if (instance==null){
            instance=new BluetoothUtil();
        }
        return instance;
    }

}
