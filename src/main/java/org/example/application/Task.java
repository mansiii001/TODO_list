package org.example.application;

import java.time.LocalDate;

public class Task {
    public int id;
    public String taskName;
    public Boolean isDone;
    public String taskDescription;
    public LocalDate dueDate;

    public Task(int id, String taskName, Boolean isDone, String taskDescription) {
        this.id = id;
        this.taskName = taskName;
        this.isDone = isDone;
        this.taskDescription = taskDescription;
    }

    public Task(int id, String taskName, Boolean isDone, String taskDescription, LocalDate dueDate) {
        this.id = id;
        this.taskName = taskName;
        this.isDone = isDone;
        this.taskDescription = taskDescription;
        this.dueDate = dueDate;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
}
