package com.hardiksenghani.letstoss.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.hardiksenghani.letstoss.db.AppDatabase;
import com.hardiksenghani.letstoss.db.DBUtils;
import com.hardiksenghani.letstoss.model.Toss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class TossActivityViewModel extends ViewModel {

    ArrayList<Toss> allToss;
    MutableLiveData<ArrayList<Toss>> allTossLiveData;

    LiveData<List<Integer>> allTossNamesLiveData;

    public TossActivityViewModel() {
        allToss = new ArrayList<>();
        allTossLiveData = new MutableLiveData<>();
    }

    public void updateTossData(final List<Integer> tossIds) {
        HashMap<String, Object> map = new HashMap<>();
        new DBUtils.DBAsyncTask(new DBUtils.DBTask() {
            @Override
            public void performTask(AppDatabase appDatabase, HashMap<String, Object> objectMap) {
                synchronized (allToss) {
                    allToss.clear();
                    for (Integer tossId : tossIds) {
                        allToss.add(Toss.readFromDB(appDatabase, tossId.intValue()));
                    }
                }
                allTossLiveData.postValue(allToss);
            }

            @Override
            public void taskCompleted() {
            }
        }, map).execute();
    }

    public MutableLiveData<ArrayList<Toss>> getAllTossLiveData() {
        return allTossLiveData;
    }

    public LiveData<List<Integer>> getAllTossNamesLiveData() {
        return allTossNamesLiveData;
    }

    public void setAllTossNamesLiveData(LiveData<List<Integer>> allTossNamesLiveData) {
        this.allTossNamesLiveData = allTossNamesLiveData;
    }
}
