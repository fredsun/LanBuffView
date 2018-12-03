package me.fredsun.roundrectangleview;

/**
 * Created by fred on 2018/9/12.
 */

public class KcalData {
    int minute;
    int kcal;

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public KcalData(int minute, int kcal) {
        this.minute = minute;
        this.kcal = kcal;
    }
}
