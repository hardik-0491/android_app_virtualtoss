package com.hardiksenghani.letstoss.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class DBUtils {

    public static final String DB_NAME = "TOSS_DB";

    public interface Toss {
        String TABLE_NAME = "toss";

        public interface Fields {
            String NAME = "name";
            String NUMBER_OF_OUTCOME = "numberOfOutComes";
        }
    }

    public interface TossOutcome {
        String TABLE_NAME = "tossoutcome";

        public interface Fields {
            String OUTCOME_ID = "outcomeId";
            String OUTCOME_TEXT = "outcomeText";
        }
    }

    private static AppDatabase appDatabase;

    public static void initializeAppDatabase(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);

                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    //Fill System Toss
                                    List<com.hardiksenghani.letstoss.model.Toss> allTosses =
                                            com.hardiksenghani.letstoss.model.Toss.getSystemToss();
                                    for (com.hardiksenghani.letstoss.model.Toss toss : allTosses) {
                                        appDatabase.tossDao().insert(toss);
                                        appDatabase.tossOutcomeDao().insert(toss.getAllOutComes());
                                    }
                                }
                            });


                        }
                    })
                    .fallbackToDestructiveMigrationFrom(1, 2, 3)
                    .build();
        }
    }

    private static AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public interface DBTask {
        public void performTask(AppDatabase appDatabase, HashMap<String, Object> objectMap);

        public void taskCompleted();
    }

    public static final class DBAsyncTask extends AsyncTask<Void, Void, Void> {

        private DBTask task;
        private HashMap<String, Object> objectMap;

        public DBAsyncTask(DBTask task, HashMap<String, Object> objectMap) {
            this.task = task;
            this.objectMap = objectMap;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList objects = new ArrayList();

            if (task == null) {
                return null;
            }

            AppDatabase appDatabase = getAppDatabase();

            if (appDatabase == null) {
                return null;
            }

            task.performTask(appDatabase, objectMap);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (task == null) {
                return;
            }
            task.taskCompleted();
        }
    }

}
