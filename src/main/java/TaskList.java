import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskList {
    private int id;
    private final ArrayList<Task> taskList = new ArrayList<>();
    private final Map<Integer, String> taskListMap = new HashMap<>();

    public ArrayList<Task> getTaskList() {
        for (Task task : this.taskList) {
            System.out.println(task.getTitle());
        }
        return this.taskList;
    }

    public void addTask(Task task) {
        ArrayList<Task> taskList = this.getTaskList();
        taskList.add(task);

        for (Task task1 : taskList) {
            this.taskListMap.put(taskList.indexOf(task1), task1.getTitle().toString());
        }
    }

    public Map<Integer, String> getAllTasks() {
        System.out.println("------this.taskListMap : "+this.taskListMap);
        return this.taskListMap;
    }
}
