package com.ajayasija.bluetoothdemo.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajayasija.bluetoothdemo.R;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.MyViewHolder> {
    private Context context;
    private List<BluetoothDevice> deviceList;
    private OnItemClick listener;

    public DevicesAdapter(Context context, List<BluetoothDevice> deviceList, OnItemClick listener) {
        this.context = context;
        this.deviceList = deviceList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_device,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(deviceList.get(position).getName());
        holder.tvAddress.setText(deviceList.get(position).getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(deviceList.get(position).getAddress());
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName,tvAddress;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress=itemView.findViewById(R.id.tvAddress);
            tvName=itemView.findViewById(R.id.tvName);
        }
    }

    public interface OnItemClick{
        void onItemClick(String address);
    }
}
