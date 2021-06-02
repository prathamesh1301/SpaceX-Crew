package com.example.spacexcrew.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.spacexcrew.Model.CrewDetails;
import com.example.spacexcrew.Repository.CrewRepository;

import java.util.List;

public class CrewViewModel extends AndroidViewModel {
    private CrewRepository repository;
    private LiveData<List<CrewDetails>> getLiveCrewList;
    public CrewViewModel(@NonNull Application application) {
        super(application);
        repository=new CrewRepository(application);
        getLiveCrewList=repository.getAllData();
    }
    public void insert(List<CrewDetails> list){
        repository.insert(list);
    }
    public LiveData<List<CrewDetails>> getAllData(){
        return getLiveCrewList;
    }
}
