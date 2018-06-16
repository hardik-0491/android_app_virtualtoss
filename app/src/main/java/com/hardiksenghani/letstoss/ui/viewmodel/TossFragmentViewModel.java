package com.hardiksenghani.letstoss.ui.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.hardiksenghani.letstoss.controller.TossFlipper;
import com.hardiksenghani.letstoss.controller.TossManager;
import com.hardiksenghani.letstoss.db.AppDatabase;
import com.hardiksenghani.letstoss.db.DBUtils;
import com.hardiksenghani.letstoss.model.Toss;

import java.util.HashMap;

public class TossFragmentViewModel extends ViewModel {

    MutableLiveData<Toss> currentToss;
    MutableLiveData<TossFlipper> tossFlipperMutableLiveData;
    MutableLiveData<Boolean> tossFlipInProgress;

    public TossFragmentViewModel() {
        currentToss = new MutableLiveData<>();
        tossFlipperMutableLiveData = new MutableLiveData<>();
        tossFlipInProgress = new MutableLiveData<>();
    }

    public MutableLiveData<Toss> getCurrentToss() {
        return currentToss;
    }

    public MutableLiveData<TossFlipper> getTossFlipperMutableLiveData() {
        return tossFlipperMutableLiveData;
    }

    public void setCurrentToss(final int tossId) {

        if (tossId == -1) {
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        new DBUtils.DBAsyncTask(new DBUtils.DBTask() {
            @Override
            public void performTask(AppDatabase appDatabase, HashMap<String, Object> objectMap) {
                currentToss.postValue(Toss.readFromDB(appDatabase, tossId));
            }

            @Override
            public void taskCompleted() {

            }
        }, map).execute();

    }

    public MutableLiveData<Boolean> getTossFlipInProgress() {
        return tossFlipInProgress;
    }

    public void flipToss(int outcomeDelayTime) {
        TossFlipper.UpdateListener updateListener = new TossFlipper.UpdateListener() {
            @Override
            public void dataChanged(TossFlipper tossFlipper, boolean taskCompleted) {
                tossFlipperMutableLiveData.postValue(tossFlipper);
                tossFlipInProgress.postValue(!taskCompleted);
            }
        };
        TossFlipper flipper = TossManager.createTossFlipper(currentToss.getValue(), updateListener,
                outcomeDelayTime);
        flipper.flip();
    }

}
