package com.example.spacexcrew.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;

import com.example.spacexcrew.DAO.CrewDAO;
import com.example.spacexcrew.Database.CrewDatabase;
import com.example.spacexcrew.Model.CrewDetails;

import java.util.List;

public class CrewRepository {
    private CrewDatabase crewDatabase;
    private LiveData<List<CrewDetails>> crewDetailsLiveList;

    public CrewRepository(Application application) {
       this.crewDatabase=CrewDatabase.getCrewDatabaseInstance(application);
       crewDetailsLiveList=crewDatabase.crewDao().getAllData();
    }

    public void insert(List<CrewDetails> crewDetailsList){
        new Insert(crewDatabase).execute(crewDetailsList);
    }
    public LiveData<List<CrewDetails>> getAllData(){
        return crewDetailsLiveList;
    }


    static class Insert extends AsyncTask<List<CrewDetails>,Void,Void> {
        private CrewDAO crewDAO;

        public Insert(CrewDatabase database) {
            this.crewDAO = database.crewDao();
        }

        @Override
        protected Void doInBackground(List<CrewDetails>... lists) {
            crewDAO.insert(lists[0]);
            return null;
        }
    }
}
