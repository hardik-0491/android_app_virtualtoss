package com.hardiksenghani.letstoss.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.hardiksenghani.letstoss.db.AppDatabase;
import com.hardiksenghani.letstoss.db.DBUtils;

import java.util.HashMap;

@Entity(
        primaryKeys = {"tossId", "outcomeId"},
        foreignKeys = @ForeignKey(
                entity = Toss.class,
                parentColumns = "tossId",
                childColumns = "tossId")
)

public class TossOutcome {

    @NonNull
    public int getTossId() {
        return tossId;
    }

    public void setTossId(@NonNull int tossId) {
        this.tossId = tossId;
    }

    @ColumnInfo(name = "tossId")
    @NonNull
    private int tossId;

    @ColumnInfo(name = "outcomeId")
    @NonNull
    private int outcomeId;

    @ColumnInfo(name = "outcomeText")
    private String outcomeText;

    public int getNumberOfTimesDrawn() {
        return numberOfTimesDrawn;
    }

    public void setNumberOfTimesDrawn(int numberOfTimesDrawn) {
        this.numberOfTimesDrawn = numberOfTimesDrawn;
    }

    @ColumnInfo(name = "numberOfTimesDrawn")
    private int numberOfTimesDrawn;

    @Ignore
    private boolean isNewOutcome;

    public TossOutcome(int tossId, int outcomeId, String outcomeText) {
        this.tossId = tossId;
        this.outcomeId = outcomeId;
        this.outcomeText = outcomeText;
    }

    public int getOutcomeId() {
        return outcomeId;
    }

    public void setOutcomeId(int outcomeId) {
        this.outcomeId = outcomeId;
    }

    public String getOutcomeText() {
        return outcomeText;
    }

    public void setOutcomeText(String outcomeText) {
        this.outcomeText = outcomeText;
    }

    public boolean isNewOutcome() {
        return isNewOutcome;
    }

    public void setNewOutcome(boolean newOutcome) {
        isNewOutcome = newOutcome;
    }

    public static void updateAppStats(final TossOutcome outcome) {
        HashMap<String, Object> map = new HashMap<>();
        new DBUtils.DBAsyncTask(new DBUtils.DBTask() {
            @Override
            public void performTask(AppDatabase appDatabase,
                                    HashMap<String, Object> objectMap) {
                synchronized (appDatabase) {
                    appDatabase.tossDao().updatePlayCount(outcome.tossId);
                    appDatabase.tossOutcomeDao().updateDrawnCount(outcome.tossId, outcome.outcomeId);
                }
            }

            @Override
            public void taskCompleted() {

            }
        }, map).execute();
    }
}
