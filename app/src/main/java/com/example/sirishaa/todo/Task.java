package com.example.sirishaa.todo;

import java.io.Serializable;
import java.util.HashMap;

public class Task implements Serializable {

    private String taskName;
    //private String time;
    private String category;
    private String date;

    public Task() {

    }

    /*public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }*/
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public HashMap<String, String> toFirebaseObject() {
        HashMap<String, String> todo = new HashMap<String, String>();
        todo.put("taskName", taskName);
        todo.put("category", category);
        //todo.put("date", date);
        //todo.put("time", time);

        return todo;
    }

}
