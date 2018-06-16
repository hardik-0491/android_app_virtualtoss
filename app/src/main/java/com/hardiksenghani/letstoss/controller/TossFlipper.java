package com.hardiksenghani.letstoss.controller;

import com.hardiksenghani.letstoss.model.Toss;
import com.hardiksenghani.letstoss.model.TossOutcome;

public class TossFlipper extends Thread {

    private Toss toss;
    private int totalTime, remainingTime;
    private String tossOutcome = null;
    private UpdateListener updateListener;

    public TossFlipper(Toss toss, int totalTime, UpdateListener updateListener) {
        this.toss = toss;
        this.totalTime = totalTime;
        this.remainingTime = totalTime;
        this.tossOutcome = "";
        this.updateListener = updateListener;
    }

    public void flip() {
        start();
    }

    @Override
    public void run() {
        TossOutcome outcome = TossManager.getTossOutcome(toss);
        if (outcome == null) {
            return;
        }
        TossOutcome.updateAppStats(outcome);
        setOutComeText("..." + Integer.toString(remainingTime) + "...", false);
        while (remainingTime > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            remainingTime--;
            setOutComeText("..." + Integer.toString(remainingTime) + "...", false);
        }
        setOutComeText(outcome.getOutcomeText(), true);
    }

    public void setOutComeText(String data, boolean taskCompleted) {
        tossOutcome = data;
        if (updateListener != null) {
            updateListener.dataChanged(this, taskCompleted);
        }
    }

    public String getTossOutcome() {
        return tossOutcome;
    }

    public static interface UpdateListener {
        public void dataChanged(TossFlipper tossFlipper, boolean taskCompleted);
    }

}
