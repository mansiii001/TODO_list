import java.util.*;
import java.util.stream.Collectors;

public class TaskList {

    public void addTask(String task) {
        try {
            MySQLConnect mySQLConnect = new MySQLConnect();
            mySQLConnect.createNewTask(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static String getTaskToBeEdit(int taskID) {
        try {
            MySQLConnect mySQLConnect = new MySQLConnect();
            String task = mySQLConnect.findTask(taskID);
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

    public void editTask(Integer taskID, String taskName) {
        try{
            MySQLConnect mySQLConnect = new MySQLConnect();
            mySQLConnect.editTask(taskID, taskName);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void toggleCompleteCheckbox(Integer taskId, Boolean isCompleted) {
        try {
            MySQLConnect mySQLConnect = new MySQLConnect();
            mySQLConnect.markCompleted(taskId, isCompleted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
