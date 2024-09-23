import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MySQLConnect {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/todo_list", "root", "12345678");
    }

    public ArrayList<newTask> readAll() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = getConnection();
            statement = connect.createStatement();
            resultSet = statement.executeQuery("select * from todos");

            return returnResultSet(resultSet);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            close();
        }
    }

    private ArrayList<newTask> returnResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<newTask> tasks = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String taskName = resultSet.getString("task");
            boolean isDone = resultSet.getBoolean("isCompleted");
            tasks.add(new newTask(id, taskName, isDone));
        }
        return tasks;
    }

    public void createNewTask(String task) throws Exception {
        try {
            connect = getConnection();
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
            connect = getConnection();
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
            connect = getConnection();
            PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM todos where id = (?)");
            preparedStatement.setInt(1, taskID);
            resultSet = preparedStatement.executeQuery();
            ArrayList<newTask> taskArrayList = returnResultSet(resultSet);
            return taskArrayList.get(0).taskName;

        } catch (Exception e){
            throw new Exception(e.getMessage());
        } finally {
            close();
        }
    }

    public void editTask(Integer taskID, String taskName) {
        try {
            connect = getConnection();
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

    public void markCompleted(Integer taskID, Boolean isCompleted) {
        try{
            connect = getConnection();
            PreparedStatement prepareStatement = connect.prepareStatement("UPDATE todos set isCompleted = (?) where id = (?)");
            prepareStatement.setBoolean(1, !isCompleted );
            prepareStatement.setInt(2, taskID);
            prepareStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            close();
        }
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