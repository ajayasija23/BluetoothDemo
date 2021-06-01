package com.ajayasija.bluetoothdemo.activity;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ajayasija.bluetoothdemo.R;
import com.ajayasija.bluetoothdemo.databinding.ActivityMainBinding;
import com.ajayasija.bluetoothdemo.listener.ConnectionStatusListener;
import com.ajayasija.bluetoothdemo.listener.MsgStatusListener;
import com.ajayasija.bluetoothdemo.model.ServerResposnse;
import com.ajayasija.bluetoothdemo.threads.ConnectThread;
import com.ajayasija.bluetoothdemo.threads.ReadThread;
import com.ajayasija.bluetoothdemo.viewModel.ResponseViewModel;

public class MainActivity extends BaseActivity implements View.OnClickListener, ConnectionStatusListener, MsgStatusListener {
    private static final int REQUEST_ENABLE_BT = 101;
    private static final int REQUEST_CONNECT = 102;
    private BluetoothAdapter bluetoothAdapter;
    private ActivityMainBinding binding;
    private BluetoothSocket mSocket;
    private String message;
    private ResponseViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        viewModel= ViewModelProviders.of(this).get(ResponseViewModel.class);
        initUi();
        fetchLocation(this);
        binding.tvConnect.setOnClickListener(this);
        binding.btnBluetoothData.setOnClickListener(this);
        binding.btnUpload.setOnClickListener(this);
    }

    private void initUi() {
        if (bluetoothAdapter==null){
            Toast.makeText(this,"This device does not support bluetooth",Toast.LENGTH_SHORT).show();
        }
        else {
            binding.tvBluetoothname.setText("My Bluetooth:"+bluetoothAdapter.getName());
            if (bluetoothAdapter.isEnabled()){
                binding.switchEnable.setChecked(true);
                binding.switchEnable.setText("On");
                binding.image.setImageResource(R.drawable.ic_bluetooth_connected);
            }
        }
        binding.switchEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    binding.image.setImageResource(R.drawable.ic_bluetooth_connected);
                }
                else {
                    binding.switchEnable.setChecked(false);
                    binding.switchEnable.setText("Off");
                    bluetoothAdapter.disable();
                    binding.image.setImageResource(R.drawable.ic_bluetoothoff);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_CANCELED&&requestCode==REQUEST_ENABLE_BT){
            binding.switchEnable.setChecked(false);
            binding.switchEnable.setText("Off");
            binding.image.setImageResource(R.drawable.ic_bluetoothoff);
        }
        if (requestCode==REQUEST_CONNECT&&resultCode==RESULT_OK){
            if (data!=null){
                String address=data.getStringExtra("address");
                showProgress();
                new ConnectThread(address,this).start();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvConnect:
                if (bluetoothAdapter.isEnabled()){
                    Intent intent=new Intent(MainActivity.this,ConnectedDevicesActivity.class);
                    startActivityForResult(intent,REQUEST_CONNECT);
                }
                else {
                    Toast.makeText(this, "Enable Bluetooth to connect", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnBluetoothData:
                if (mSocket!=null){
                    new ReadThread(mSocket,this).start();
                }else {
                    messageDialog("No Bluetooth Device Connected",null);
                }
                break;
            case R.id.btnUpload:
                if (!message.equals(""))
                    uploadData();
                break;
        }
    }

    private void uploadData() {
        showProgress();
        viewModel.uploadData(message,getLocation());
        viewModel.getLiveData().observe(this, new Observer<ServerResposnse>() {
            @Override
            public void onChanged(ServerResposnse serverResposnse) {
                dismissProgress();
                if (serverResposnse!=null){
                    if (serverResposnse.getStatus().equals("ok")){
                        messageDialog("Uploaded to server successfully",null);
                    }
                    else {
                        messageDialog("Something went wrong",null);
                    }
                }
            }
        });
    }

    @Override
    public void onError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgress();
                messageDialog("Unable to connecting device",null);
            }
        });
    }

    @Override
    public void onSucces(BluetoothSocket socket) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSocket=socket;
                messageDialog("Connected Successfully",null);
                dismissProgress();
                binding.tvConnectedTo.setText("Connected To: "+socket.getRemoteDevice().getName());
            }
        });
    }

    @Override
    public void onMsgSuccess(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                message=msg;
                binding.tvData.setText(message);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMsgError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Some Error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }
}