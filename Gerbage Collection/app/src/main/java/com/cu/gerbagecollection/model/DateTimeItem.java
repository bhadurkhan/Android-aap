package com.cu.gerbagecollection.model;

public class DateTimeItem {
    private String Date,Time;

    public DateTimeItem(String date, String time) {
        Date = date;
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }
}
