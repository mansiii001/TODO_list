import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MySQLConnect {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public Map<Integer, String> readDataBase() throws Exception {
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

    private Map<Integer, String> allResults(ResultSet resultSet) throws Exception {
        Map<Integer, String> allData = new HashMap<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String task = resultSet.getString("task");
            allData.put(id, task);
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