package com.hardiksenghani.letstoss.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hardiksenghani.letstoss.model.Toss;

import java.util.List;

@Dao
public interface TossDao {

    @Query("SELECT tossId FROM toss")
    List<Integer> getAllTossById();

    @Query("SELECT tossId FROM toss")
    LiveData<List<Integer>> getAllTossByIdWithLiveData();

    @Query("SELECT * FROM toss WHERE tossId = :tossId")
    Toss getById(int tossId);

    @Insert
    void insert(Toss toss);

    @Insert
    void insert(List<Toss> tosses);

    @Delete
    void delete(Toss toss);

    @Delete
    void delete(List<Toss> tosses);

    @Update
    void update(Toss toss);

    @Update
    void update(List<Toss> tosses);

    @Query("UPDATE toss SET numberOfTimesPlayed = numberOfTimesPlayed + 1 " +
            "WHERE tossId = :tossId")
    void updatePlayCount(int tossId);

    @Query("UPDATE toss SET numberOfTimesPlayed = numberOfTimesPlayed + :adjustment " +
            "WHERE tossId = :tossId")
    void adjustPlayCount(int tossId, int adjustment);

    @Query("SELECT numberOfTimesPlayed FROM toss WHERE tossId = :tossId")
    int getPlayCount(int tossId);

}
