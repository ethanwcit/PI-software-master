package com.group20.pi_software.ui.feed;

public class Feed_Class {
    private String MainMessage;
    private String curr_time_date;
    public Feed_Class(String MainMessage, String curr_time_date) {
        this.MainMessage = MainMessage;
        this.curr_time_date = curr_time_date;
    }
    public String getMainMessage() {
        return this.MainMessage;
    }
    public String getCurrTime() {
        return this.curr_time_date;
    }
}