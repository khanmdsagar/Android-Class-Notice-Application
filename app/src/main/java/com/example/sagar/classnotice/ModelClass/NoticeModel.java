package com.example.sagar.classnotice.ModelClass;

public class NoticeModel {

  public  String id,date,time,text;
  public long count;

    public NoticeModel() {
    }

    public NoticeModel(String id, String date, String time, String text, long count) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.text = text;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
