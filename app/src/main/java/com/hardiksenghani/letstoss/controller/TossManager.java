package com.hardiksenghani.letstoss.controller;

import android.arch.lifecycle.LiveData;

import com.hardiksenghani.letstoss.db.AppDatabase;
import com.hardiksenghani.letstoss.model.Toss;
import com.hardiksenghani.letstoss.model.TossOutcome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TossManager {

    public static final int DEFAULT_OUTCOME_DELAY_TIME = 3;

    public static TossFlipper createTossFlipper(Toss toss,
                                                TossFlipper.UpdateListener updateListener,
                                                int outcomeDelayTime) {
        TossFlipper flipper = new TossFlipper(toss, outcomeDelayTime, updateListener);
        return flipper;
    }

    public static TossOutcome getTossOutcome(Toss toss) {
        TossOutcome outcome = null;
        if (toss != null) {
            Random random = new Random();
            outcome = toss.getAllOutComes().get(random.nextInt(toss.getNumberOfOutComes()));
        }
        return outcome;
    }

    public static ArrayList<Toss> getAllToss(AppDatabase appDatabase) {
        ArrayList<Toss> allTosses = new ArrayList<>();
        ArrayList<Integer> tossesById = new ArrayList<>(appDatabase.tossDao().getAllTossById());
        for (Integer tossId : tossesById) {
            allTosses.add(Toss.readFromDB(appDatabase, tossId.intValue()));
        }
        return allTosses;
    }

    public static LiveData<List<Integer>> getAllTossWithLiveData(AppDatabase appDatabase) {
        return appDatabase.tossDao().getAllTossByIdWithLiveData();
    }

}
