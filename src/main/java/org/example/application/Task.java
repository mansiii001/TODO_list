package org.example.application;

import java.util.Date;

public class Task {
    public int id;
    public String taskName;
    public Boolean isDone;
    public String taskDescription;
    public Date taskDueDate;

    public Task(int id, String taskName, Boolean isDone, String taskDescription, Date dueDate) {
        this.id = id;
        this.taskName = taskName;
        this.isDone = isDone;
        this.taskDescription = taskDescription;
        this.taskDueDate = dueDate;
    }

    public void setTaskDueDate(Date taskDueDate) {
        this.taskDueDate = taskDueDate;
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
