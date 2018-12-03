package me.fredsun.roundrectangleview;

import android.support.annotation.NonNull;

/**
 * Created by fred on 2018/8/30.
 */

public class PieData {
    // 用户关心数据
    private String name;        // 名字
    private float value;        // 数值
    private float percentage;   // 百分比
    private float currentStartAngle; // 当前角度

    // 非用户关心数据
    private int color = 0;      // 颜色
    private float angle = 0;    // 角度

    public PieData(@NonNull String name, @NonNull float value, @NonNull int color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getCurrentStartAngle() {
        return currentStartAngle;
    }

    public void setCurrentStartAngle(float currentStartAngle) {
        this.currentStartAngle = currentStartAngle;
    }
}
