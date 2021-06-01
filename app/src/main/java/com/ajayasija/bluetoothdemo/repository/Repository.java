package com.ajayasija.bluetoothdemo.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ajayasija.bluetoothdemo.model.ServerResposnse;
import com.ajayasija.bluetoothdemo.util.Constants;
import com.ajayasija.bluetoothdemo.web.ApiService;
import com.ajayasija.bluetoothdemo.web.WebApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private WebApi webApi;
    private MutableLiveData<ServerResposnse> result;

    public Repository(){
        webApi= ApiService.getWebApi(Constants.BASE_URL);
        result=new MutableLiveData<>();
    }
    public void uploadData(String data,String location){
        Call<ServerResposnse> call= webApi.uploadData(data, location);
        call.enqueue(new Callback<ServerResposnse>() {
            @Override
            public void onResponse(Call<ServerResposnse> call, Response<ServerResposnse> response) {
                if (response.isSuccessful()){
                    result.postValue(response.body());
                }else {
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ServerResposnse> call, Throwable t) {
                result.postValue(null);
            }
        });
    }

    public LiveData<ServerResposnse> getResult() {
        return result;
    }
}
