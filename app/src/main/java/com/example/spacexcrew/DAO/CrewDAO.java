package com.example.spacexcrew.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.spacexcrew.Model.CrewDetails;

import java.util.List;
@Dao
public interface CrewDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<CrewDetails> itemDetailsList);

    @Query("SELECT * FROM crewTable")
    LiveData<List<CrewDetails>> getAllData();

    @Query("DELETE FROM crewTable")
    void deleteAll();

    @Query("DELETE FROM crewTable WHERE id = :id")
    void deleteCrewMember(int id);
}
