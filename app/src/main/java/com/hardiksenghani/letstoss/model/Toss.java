package com.hardiksenghani.letstoss.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.hardiksenghani.letstoss.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

@Entity
public final class Toss {

    public int getTossId() {
        return tossId;
    }

    public void setTossId(int tossId) {
        this.tossId = tossId;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tossId")
    private int tossId;

    @ColumnInfo(name = "name")
    @NonNull
    private String name;

    @ColumnInfo(name = "numberOfOutComes")
    private int numberOfOutComes;

    @ColumnInfo(name = "isSystemToss")
    private boolean isSystemToss;

    public int getNumberOfTimesPlayed() {
        return numberOfTimesPlayed;
    }

    public void setSystemToss(boolean systemToss) {
        isSystemToss = systemToss;
    }

    public void setNumberOfTimesPlayed(int numberOfTimesPlayed) {
        this.numberOfTimesPlayed = numberOfTimesPlayed;
    }

    @ColumnInfo(name = "numberOfTimesPlayed")
    private int numberOfTimesPlayed;

    @Ignore
    private ArrayList<TossOutcome> outcomes;

    @Ignore
    private int outcome;

    @Ignore
    private boolean isNewToss;

    public Toss() {
        outcomes = new ArrayList<>();
        tossId = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfOutComes() {
        return numberOfOutComes;
    }

    public void setNumberOfOutComes(int numberOfOutComes) {
        this.numberOfOutComes = numberOfOutComes;
    }

    public String getOutComeText(int outcomeId) {
        return outcomes.get(outcomeId).getOutcomeText();
    }

    public void setOutComeText(String outComeText, int outcomeId) {
        this.outcomes.get(outcomeId).setOutcomeText(outComeText);
    }

    public ArrayList<TossOutcome> getAllOutComes() {
        return outcomes;
    }

    public int getOutcome() {
        return outcome;
    }

    public void setOutcome(int outcome) {
        this.outcome = outcome;
    }

    public void setOutcomes(ArrayList<TossOutcome> outcomes) {
        this.outcomes = outcomes;
    }

    public boolean isNewToss() {
        return isNewToss;
    }

    public boolean isSystemToss() {
        return isSystemToss;
    }

    public void setNewToss(boolean newToss) {
        isNewToss = newToss;
    }

    public static Toss readFromDB(AppDatabase appDatabase, int tossId) {
        Toss newToss = appDatabase.tossDao().getById(tossId);
        newToss.setOutcomes(new ArrayList<>(appDatabase.tossOutcomeDao().getTossOutcomes(newToss.getTossId())));
        return newToss;
    }

    public static void save(AppDatabase appDatabase, Toss toss) {
        if (toss.isNewToss()) {
            appDatabase.tossDao().insert(toss);
            appDatabase.tossOutcomeDao().insert(toss.outcomes);
        } else {
            appDatabase.tossDao().update(toss);
            for (TossOutcome outcome : toss.outcomes) {
                if (outcome.isNewOutcome()) {
                    appDatabase.tossOutcomeDao().insert(outcome);
                } else {
                    appDatabase.tossOutcomeDao().update(outcome);
                }
            }
        }
    }

    public static void delete(AppDatabase appDatabase, Toss toss) {

        if (toss.isNewToss()) {
            return;
        }

        appDatabase.tossOutcomeDao().delete(toss.outcomes);
        appDatabase.tossDao().delete(toss);
    }

    public static List<Toss> getSystemToss() {
        ArrayList<Toss> systemToss = new ArrayList<>();

        Toss simpleToss = new Toss();
        simpleToss.tossId = 1;
        simpleToss.name = "Coin";
        simpleToss.numberOfOutComes = 2;
        simpleToss.isSystemToss = true;
        simpleToss.outcomes.add(new TossOutcome(simpleToss.tossId, 1, "Head"));
        simpleToss.outcomes.add(new TossOutcome(simpleToss.tossId, 2, "Tail"));
        systemToss.add(simpleToss);

        Toss dice = new Toss();
        dice.tossId = 2;
        dice.name = "Dice";
        dice.numberOfOutComes = 6;
        dice.isSystemToss = true;
        for (int i = 1; i <= 6; i++) {
            dice.outcomes.add(new TossOutcome(dice.tossId, i, Integer.toString(i)));
        }
        systemToss.add(dice);

        Toss doubleDice = new Toss();
        doubleDice.tossId = 3;
        doubleDice.name = "2 Dice";
        doubleDice.numberOfOutComes = 11;
        doubleDice.isSystemToss = true;
        for (int i = 1; i <= doubleDice.numberOfOutComes; i++) {
            doubleDice.outcomes.add(new TossOutcome(doubleDice.tossId, i, Integer.toString(i + 1)));
        }
        systemToss.add(doubleDice);

        return systemToss;
    }

}
