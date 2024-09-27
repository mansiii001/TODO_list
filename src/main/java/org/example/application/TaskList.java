package org.example.application;

import org.example.database.MySQLConnect;

import java.util.*;
import java.util.stream.Collectors;

public class TaskList {

    public void addTask(String taskName) throws Exception {

        if(taskName.isEmpty()){
            throw new Exception("taskName can not be empty");
        }

        MySQLConnect mySQLConnect = new MySQLConnect();
        mySQLConnect.createNewTask(taskName);

    }

    public List<Task> getAllTasks() {
        try{
            MySQLConnect mySQLConnect = new MySQLConnect();
            return mySQLConnect.readAll();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Task> allUnCkecked() {
        try{
            MySQLConnect mySQLConnect = new MySQLConnect();
            ArrayList<Task> taskArrayList = mySQLConnect.readAll();
            return unCheckedTasks(taskArrayList);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Task getTaskToBeEdit(int taskID) {
        try {
            MySQLConnect mySQLConnect = new MySQLConnect();
            Task task = mySQLConnect.findTask(taskID);
            return task;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Object allCkecked() {
        try{
            MySQLConnect mySQLConnect = new MySQLConnect();
            ArrayList<Task> taskArrayList = mySQLConnect.readAll();
            return checkedTasks(taskArrayList);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Task> unCheckedTasks(ArrayList<Task> taskArrayList) {
        return taskArrayList.stream().filter(task -> !task.isDone).collect(Collectors.toList());
    }

    private List<Task> checkedTasks(ArrayList<Task> taskArrayList) {
        return taskArrayList.stream().filter(task -> task.isDone).collect(Collectors.toList());
    }

    public void deleteTask(int taskID) {
        try{
            MySQLConnect mySQLConnect = new MySQLConnect();
            mySQLConnect.deleteTask(taskID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void editTask(Integer taskID, String taskName) throws Exception {
        if(taskName.isEmpty()){
            throw new Exception("Task name cannot be empty");
        }

        MySQLConnect mySQLConnect = new MySQLConnect();
        mySQLConnect.editTask(taskID, taskName);
    }

    public void toggleCompleteCheckbox(Integer taskId) {

        List<Task> allTasks = getAllTasks();
        Task filteredTask = allTasks.stream().filter(task -> task.id == taskId).collect(Collectors.toList()).get(0);
        boolean isCompleted = filteredTask.isDone;

        try {
            MySQLConnect mySQLConnect = new MySQLConnect();
            mySQLConnect.markCompleted(taskId, !isCompleted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
