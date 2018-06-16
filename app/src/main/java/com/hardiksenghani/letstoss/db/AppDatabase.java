package com.hardiksenghani.letstoss.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.hardiksenghani.letstoss.model.Toss;
import com.hardiksenghani.letstoss.model.TossOutcome;

@Database(entities = {Toss.class, TossOutcome.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TossDao tossDao();

    public abstract TossOutcomeDao tossOutcomeDao();
}
