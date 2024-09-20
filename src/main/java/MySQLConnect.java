import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQLConnect {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public Map<Integer, Task> readDataBase() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/todo_list", "root", "12345678");
            statement = connect.createStatement();
            resultSet = statement.executeQuery("select * from todos");

            return allResults(resultSet);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            close();
        }
    }

    public void createNewTask(String task) throws Exception {
        try {
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/todo_list", "root", "12345678");
            PreparedStatement preparedStatement = connect.prepareStatement("INSERT INTO todos (task) VALUES (?)");
            preparedStatement.setString(1, task);
            preparedStatement.executeUpdate();

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            close();
        }
    }

    public void deleteTask(int taskID) throws Exception {
        try {
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/todo_list", "root", "12345678");
            PreparedStatement preparedStatement = connect.prepareStatement("DELETE FROM todos where id = (?)");
            preparedStatement.setInt(1, taskID);
            preparedStatement.executeUpdate();

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            close();
        }
    }

    public String findTask(Integer taskID) throws Exception {
        try {
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/todo_list", "root", "12345678");
            PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM todos where id = (?)");
            preparedStatement.setInt(1, taskID);
            resultSet = preparedStatement.executeQuery();
            Map<Integer, Task> taskResult = allResults(resultSet);
            return taskResult.get(taskID).taskName;

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            close();
        }
    }

    public void editTask(Integer taskID, String taskName) {
        try {
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/todo_list", "root", "12345678");
            PreparedStatement preparedStatement = connect.prepareStatement("UPDATE todos set task = (?) where id = (?)");
            preparedStatement.setString(1, taskName);
            preparedStatement.setInt(2, taskID);
            preparedStatement.executeUpdate();

        } catch (Exception e){
            System.out.println(e.getMessage());
        } finally {
            close();
        }
    }

    private Map<Integer, Task> allResults(ResultSet resultSet) throws Exception {
        Map<Integer, Task> allData = new HashMap<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String task = resultSet.getString("task");
            boolean isCompleted = resultSet.getBoolean("isCompleted");
            allData.put(id, new Task(task, isCompleted));
        }
        return allData;
    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}