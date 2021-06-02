package com.example.spacexcrew.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.spacexcrew.DAO.CrewDAO;
import com.example.spacexcrew.Model.CrewDetails;

@Database(entities = {CrewDetails.class},version = 1)
public abstract class CrewDatabase extends RoomDatabase {

    private static final String DATABASE_NAME="itemDatabase";

    public abstract CrewDAO crewDao();

    private static volatile CrewDatabase databaseInstance=null;

    public static CrewDatabase getCrewDatabaseInstance(Context context){
        if(databaseInstance==null){
            synchronized (CrewDatabase.class){
                databaseInstance= Room.databaseBuilder(context,CrewDatabase.class,DATABASE_NAME)
                        .addCallback(callback)
                        .build();
            }
        }
        return databaseInstance;
    }
    static Callback callback=new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(databaseInstance);
        }
    };
    static class PopulateAsyncTask extends AsyncTask<Void,Void,Void>{
        private CrewDAO crewDao;

        public PopulateAsyncTask(CrewDatabase itemDatabase) {
            this.crewDao = itemDatabase.crewDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            crewDao.deleteAll();
            return null;

        }
    }

}




