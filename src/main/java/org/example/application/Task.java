package org.example.application;

public class Task {
    public int id;
    public String taskName;
    public Boolean isDone;

    public Task(int id, String taskName, Boolean isDone) {
        this.id = id;
        this.taskName = taskName;
        this.isDone = isDone;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }
}
