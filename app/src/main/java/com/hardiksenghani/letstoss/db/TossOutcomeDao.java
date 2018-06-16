package com.hardiksenghani.letstoss.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.hardiksenghani.letstoss.model.TossOutcome;

import java.util.List;

@Dao
public interface TossOutcomeDao {

    @Query("SELECT * FROM tossoutcome WHERE tossId = :tossId")
    List<TossOutcome> getTossOutcomes(int tossId);

    @Insert
    void insert(List<TossOutcome> tossOutcomes);

    @Delete
    void delete(List<TossOutcome> tossOutcomes);

    @Update
    void update(List<TossOutcome> tossOutcomes);

    @Insert
    void insert(TossOutcome tossOutcome);

    @Delete
    void delete(TossOutcome tossOutcome);

    @Update
    void update(TossOutcome tossOutcome);

    @Query("UPDATE tossoutcome SET numberOfTimesDrawn = numberOfTimesDrawn + 1 " +
            "WHERE tossId = :tossId AND outcomeId = :outcomeId")
    void updateDrawnCount(int tossId, int outcomeId);

    @Query("UPDATE tossoutcome SET numberOfTimesDrawn = numberOfTimesDrawn + :adjustment " +
            "WHERE tossId = :tossId AND outcomeId = :outcomeId")
    void adjustDrawnCount(int tossId, int outcomeId, int adjustment);

    @Query("SELECT numberOfTimesDrawn FROM tossoutcome WHERE tossId = :tossId" +
            " AND outcomeId = :outcomeId")
    int getDrawnCount(int tossId, int outcomeId);

}
