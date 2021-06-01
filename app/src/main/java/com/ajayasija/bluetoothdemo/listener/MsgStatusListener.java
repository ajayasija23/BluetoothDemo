package com.ajayasija.bluetoothdemo.listener;

public interface MsgStatusListener {
    void onMsgSuccess(String msg);
    void onMsgError();
}
