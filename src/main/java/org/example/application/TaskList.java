package org.example.application;

import java.util.*;
import java.util.stream.Collectors;

public class TaskList {

    NewTaskList newTaskList = new NewTaskList();

    public void addTask(String taskName) throws Exception {
        if(taskName.isEmpty()){
            throw new Exception("taskName can not be empty");
        }
        NewTaskList.addNewTask(taskName);
    }

    public List<Task> getAllTasks() {
        return  newTaskList.allTasks;
    }

    public static Task getTaskToBeEdit(int taskID) {
        return NewTaskList.findTask(taskID);
    }

    public List<Task> unCheckedTasks() {
        List<Task> allTasks = getAllTasks();
        return allTasks.stream().filter(task -> !task.isDone).collect(Collectors.toList());
    }

    public List<Task> checkedTasks() {
        List<Task> allTasks = getAllTasks();
        return allTasks.stream().filter(task -> task.isDone).collect(Collectors.toList());
    }

    public void deleteTask(int taskID) {
        NewTaskList.removeTask(taskID);
    }

    public void editTask(Integer taskID, String taskName) throws Exception {
        if(taskName.isEmpty()){
            throw new Exception("Task name cannot be empty");
        }
        NewTaskList.editTask(taskID, taskName);
    }

    public void toggleCompleteCheckbox(Integer taskId) {
        Task task = findTask(taskId);
        boolean checkBoxMark = task.isDone;
        NewTaskList.toggleTaskCompleteMark(taskId, !checkBoxMark);
    }

    public Task findTask(Integer taskID) {
        List<Task> allTasks = getAllTasks();
        return allTasks.stream().filter(task -> task.id == taskID).collect(Collectors.toList()).get(0);
    }
}
