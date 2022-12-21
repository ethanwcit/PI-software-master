package com.group20.pi_software.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;

public class StudyModel {
    private int id;
    private String date;
    private String startTime;
    private String endTime;

    public StudyModel(int id, String date, String startTime, String endTime) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "StudyModel{" +
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

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
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
    public double getStudyTime() {
        long difference = getEndTimeObj().toSecondOfDay() - getStartTimeObj().toSecondOfDay();
        if (difference < 0){
            return 24 + (double) difference/3600.0;
        }else{
            return (double) difference/3600.0;
        }
    }
}
