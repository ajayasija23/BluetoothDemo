package com.ajayasija.bluetoothdemo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ajayasija.bluetoothdemo.R;
import com.ajayasija.bluetoothdemo.databinding.LayoutProgressbarBinding;
import com.ajayasija.bluetoothdemo.listener.OkListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class BaseActivity extends AppCompatActivity {

    private static final int RC_CAMERA_AND_LOCATION = 123;
    private static Dialog dialog;
    private LayoutProgressbarBinding binding;
    private String mLocation;
    private FusedLocationProviderClient fusedLocationClient;

    public void showProgress(){
        dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding= LayoutProgressbarBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void dismissProgress(){
        try {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }catch (Exception e){

        }
    }
    public void messageDialog(String msg, OkListener listener){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
                if (listener!=null){
                    listener.onOkClick();
                }
            }
        });
        builder.show();
    }
    public String getLocation(){
        return mLocation;
    }

    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    public void fetchLocation(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            fetchLastLocation();
        } else {

            EasyPermissions.requestPermissions(this, getString(R.string.camera_and_location_rationale),
                    RC_CAMERA_AND_LOCATION, perms);
        }
    }

    @SuppressLint("MissingPermission")
    private void fetchLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mLocation=getLocationFromCordinate(location);
                        }
                    }
                });
    }

    private String getLocationFromCordinate(Location location) {
        Geocoder geocoder=new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Log.d("address",addresses.get(0).getLocality());
            return addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
