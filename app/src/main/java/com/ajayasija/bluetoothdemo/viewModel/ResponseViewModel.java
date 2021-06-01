package com.ajayasija.bluetoothdemo.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ajayasija.bluetoothdemo.model.ServerResposnse;
import com.ajayasija.bluetoothdemo.repository.Repository;

public class ResponseViewModel extends ViewModel {
    private Repository repository;
    private LiveData<ServerResposnse> liveData;

    public ResponseViewModel(){
        repository=new Repository();
        liveData=repository.getResult();
    }
    public void uploadData(String data,String location){
        repository.uploadData(data,location);
    }

    public LiveData<ServerResposnse> getLiveData() {
        return liveData;
    }
}
