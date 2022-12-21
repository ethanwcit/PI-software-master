package com.group20.pi_software.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;

public class SleepModel {

    private int id;
    private String date;
    private String startTime;
    private String endTime;

    public SleepModel(int id, String date, String startTime, String endTime) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public SleepModel() {
    }

    @Override
    public String toString() {
        return "SleepModel{" +
                "id=" + id +
                "date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getDateObj() {
        return LocalDate.parse(date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalTime getStartTimeObj() {
        return LocalTime.parse(startTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalTime getEndTimeObj() {
        return LocalTime.parse(endTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public double getSleepTime() {
        long difference = getEndTimeObj().toSecondOfDay() - getStartTimeObj().toSecondOfDay();
        if (difference < 0){
            return 24 + (double) difference/3600.0;
        }else{
            return (double) difference/3600.0;
        }
    }
}
