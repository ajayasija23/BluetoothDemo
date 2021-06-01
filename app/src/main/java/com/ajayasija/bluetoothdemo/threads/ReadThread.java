package com.ajayasija.bluetoothdemo.threads;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ajayasija.bluetoothdemo.listener.MsgStatusListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ReadThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private byte[] mmBuffer;
    private MsgStatusListener listener;


    public ReadThread(BluetoothSocket socket,MsgStatusListener listener){
        this.listener=listener;
        mmSocket=socket;
        InputStream tmpIn = null;
        try{
           tmpIn=socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmInStream=tmpIn;
    }
    public void run(){
        mmBuffer = new byte[1024];
        int numBytes;
        try{
            int available=mmInStream.available();
            byte[] bytes= new byte[available];
            mmInStream.read(bytes,0,available);
            String text=new String(bytes);
            Log.d("text",text);
            listener.onMsgSuccess(text);
        }catch (IOException e){
            Log.d("error",e.getLocalizedMessage());
            listener.onMsgError();
        }
    }
}
