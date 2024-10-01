package org.example.application;

import java.util.ArrayList;
import java.util.Date;

public class TaskList {

    static final ArrayList<Task> allTasks = new ArrayList<>();

    public TaskList() {
        Task homeWork = new Task(1, "HomeWork", false, "", new Date(2024 - 1900, 12 - 1, 19));
        Task learning = new Task(2, "Learning", false, "", new Date(2022 - 1900, 2 - 1, 10));
        Task reading = new Task(3, "Reading", false, "", new Date(2019 - 1900, 11 - 1, 9));
        Task swimming = new Task(4, "Swimming", false, "", new Date(2001 - 1900, 8 - 1, 4));
        Task writing = new Task(5, "Writing", false, "taskDescription testing...", new Date(2024 - 1900, 7 - 1, 15));

        allTasks.add(homeWork);
        allTasks.add(learning);
        allTasks.add(reading);
        allTasks.add(swimming);
        allTasks.add(writing);
    }

    public static Task findTask(int taskID){
        for (Task task : allTasks){
            if(task.id == taskID){
                return task;
            }
        }
        return null;
    }

    public static void addNewTask(String taskName, String taskDescription, Date dueDate){
        Task task = new Task(allTasks.size() + 1, taskName, false, taskDescription, dueDate);
        allTasks.add(task);
    }

    public static void removeTask(int taskID){
        allTasks.remove(findTask(taskID));
    }

    public static void editTask(int taskID, String taskName, String taskDescription, Date dueDate){
        Task task = findTask(taskID);
        task.setTaskName(taskName);
        task.setTaskDescription(taskDescription);
        task.setTaskDueDate(dueDate);
    }

    public static void toggleTaskCompleteMark(int taskID, Boolean checkBoxMark){
        Task task = findTask(taskID);
        task.setDone(checkBoxMark);
    }
}
