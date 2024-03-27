package com.example.lab.lab3;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao()
public interface PhoneDao {
    @Query("SELECT * FROM phone")
    LiveData<List<Phone>> getAll();
    @Insert()
    void insert(Phone phone);
    @Query("DELETE FROM phone")
    void deleteAll();
    @Update
    void updatePhone(Phone phone);
    @Delete
    void deletePhone(Phone phone);
}
