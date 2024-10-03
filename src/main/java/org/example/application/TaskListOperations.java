package org.example.application;

import java.util.*;
import java.util.stream.Collectors;

public class TaskListOperations {

    TaskList taskList = new TaskList();

    public Integer addTask(String taskName, String taskDescription, String stringDate) {
        if(taskName.isEmpty()){
            return 400;
        }
        TaskList.addNewTask(taskName, taskDescription, convertStringToDate(stringDate));
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

    public String convertDateToString(Date date) {
        int day = date.getDate();
        int month = date.getMonth() + 1;
        int year = date.getYear() + 1900;

        String monthString = month < 10 ? formatWithLeadingZeros(month) : Integer.toString(month);
        String dayString = day < 10 ? formatWithLeadingZeros(day) : Integer.toString(day);
        String yearString = Integer.toString(year);

        return yearString + "-" + monthString + "-" + dayString;
    }

    public Date convertStringToDate(String stringDate) {
        String[] dateComponents = stringDate.split("-");
        int year = Integer.parseInt(dateComponents[0]) - 1900;
        int month = Integer.parseInt(dateComponents[1]) - 1;
        int day = Integer.parseInt(dateComponents[2]);
        return new Date(year, month, day);
    }

    private String formatWithLeadingZeros(int number){
        return String.format("%02d", number);
    }

    public void deleteTask(int taskID) {
        TaskList.removeTask(taskID);
    }

    public Integer editTask(Integer taskID, String taskName, String taskDescription, String stringDate) {
        if(taskName.isEmpty()){
            return 400;
        }
        TaskList.editTask(taskID, taskName, taskDescription, convertStringToDate(stringDate));
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

    public List<Task> sortByDueDateAsc(List<Task> tasks) {
        Comparator<Task> comparator = (Task1, Task2) -> Long.valueOf(Task1.taskDueDate.getTime()).compareTo(Task2.taskDueDate.getTime());
        Collections.sort(tasks, comparator);
        return tasks;
    }
}
