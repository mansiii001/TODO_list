package org.example.application;

import java.util.*;
import java.util.stream.Collectors;

public class TaskListOperations {

    TaskList taskList = new TaskList();

    public void addTask(String taskName) throws Exception {
        if(taskName.isEmpty()){
            throw new Exception("taskName can not be empty");
        }
        TaskList.addNewTask(taskName);
    }

    public List<Task> getAllTasks() {
        return  taskList.allTasks;
    }

    public static Task getTaskToBeEdit(int taskID) {
        return TaskList.findTask(taskID);
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
        TaskList.removeTask(taskID);
    }

    public void editTask(Integer taskID, String taskName) throws Exception {
        if(taskName.isEmpty()){
            throw new Exception("Task name cannot be empty");
        }
        TaskList.editTask(taskID, taskName);
    }

    public void toggleCompleteCheckbox(Integer taskId) {
        Task task = findTask(taskId);
        boolean checkBoxMark = task.isDone;
        TaskList.toggleTaskCompleteMark(taskId, !checkBoxMark);
    }

    public Task findTask(Integer taskID) {
        List<Task> allTasks = getAllTasks();
        return allTasks.stream().filter(task -> task.id == taskID).collect(Collectors.toList()).get(0);
    }
}
