package org.example.application;

import java.util.ArrayList;

public class TaskList {

    static final ArrayList<Task> allTasks = new ArrayList<>();

    public TaskList() {
        Task homeWork = new Task(1, "HomeWork", false);
        Task learning = new Task(2, "Learning", false);
        Task reading = new Task(3, "Reading", false);
        Task swimming = new Task(4, "Swimming", false);
        Task writing = new Task(5, "Writing", false);

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

    public static void addNewTask(String taskName){
        Task task = new Task(allTasks.size() + 1, taskName, false);
        allTasks.add(task);
    }

    public static void removeTask(int taskID){
        allTasks.remove(findTask(taskID));
    }

    public static void editTask(int taskID, String taskName){
        Task task = findTask(taskID);
        task.setTaskName(taskName);
    }

    public static void toggleTaskCompleteMark(int taskID, Boolean checkBoxMark){
        Task task = findTask(taskID);
        task.setDone(checkBoxMark);
    }
}
