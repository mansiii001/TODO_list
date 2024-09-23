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
            ArrayList<Task> taskArrayList = mySQLConnect.readAll();
            return arrangeTaskOrder(taskArrayList);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Task> arrangeTaskOrder(ArrayList<Task> taskArrayList) {
        ArrayList<Task> arrangedTasks = new ArrayList<>();

        arrangedTasks.addAll(unCheckedTasks(taskArrayList));
        arrangedTasks.addAll(checkedTasks(taskArrayList));

        return arrangedTasks;
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
