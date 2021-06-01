package com.ajayasija.bluetoothdemo.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.ajayasija.bluetoothdemo.adapter.DevicesAdapter;
import com.ajayasija.bluetoothdemo.databinding.ActivityConnectedDevicesBinding;
import com.ajayasija.bluetoothdemo.util.BluetoothUtil;

public class ConnectedDevicesActivity extends AppCompatActivity implements DevicesAdapter.OnItemClick {
    private ActivityConnectedDevicesBinding binding;
    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_CONNECT = 102;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityConnectedDevicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        binding.rvConnectedDevices.setAdapter(new DevicesAdapter(this, BluetoothUtil.getInstance().getConnectedDevices(bluetoothAdapter),this));
        binding.rvConnectedDevices.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }



    @Override
    public void onItemClick(String address) {
        Intent intent=new Intent();
        intent.putExtra("address",address);
        setResult(RESULT_OK,intent);
        finish();
    }
}
