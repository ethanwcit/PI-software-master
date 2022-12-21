package com.group20.pi_software.model;

public class StepsModel {

    private int id;
    private String date;
    private String time;
    private int steps;

    public StepsModel(int id, String date, String time, int steps) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.steps = steps;
    }

    public StepsModel() {
    }

    @Override
    public String toString() {
        return "StepsModel{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", steps=" + steps +
                '}';
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
