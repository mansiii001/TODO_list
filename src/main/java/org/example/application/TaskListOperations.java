package org.example.application;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TaskListOperations {

    TaskList taskList = new TaskList();

    public Integer addTask(String taskName, String taskDescription, LocalDate dueDate) {
        if(taskName.isEmpty()){
            return 400;
        }
        TaskList.addNewTask(taskName, taskDescription, dueDate);
        return 200;
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

    public Integer editTask(Integer taskID, String taskName, String taskDescription, LocalDate dueDate) {
        if(taskName.isEmpty()){
            return 400;
        }
        TaskList.editTask(taskID, taskName, taskDescription, dueDate);
        return 200;
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
