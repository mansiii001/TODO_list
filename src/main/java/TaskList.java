import java.util.Map;

public class TaskList {

    public void addTask(String task) {
        try {
            MySQLConnect mySQLConnect = new MySQLConnect();
            mySQLConnect.createNewTask(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, String> getAllTasks() {
        try{
            MySQLConnect mySQLConnect = new MySQLConnect();
            Map<Integer, String> allTasks = mySQLConnect.readDataBase();
            return allTasks;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteTask(int taskID) {
        try{
            MySQLConnect mySQLConnect = new MySQLConnect();
            mySQLConnect.deleteTask(taskID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
