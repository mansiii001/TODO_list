import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

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
            return sortTasks(allTasks);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<Integer, String> sortTasks(Map<Integer, String> allTasks){
        Map<Integer, String> sortedTreeMap = new TreeMap<>(Comparator.naturalOrder());
        sortedTreeMap.putAll(allTasks);
        return sortedTreeMap;
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
}
