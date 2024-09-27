package org.example.database;

import org.example.application.Task;

import java.sql.*;
import java.util.ArrayList;

public class MySQLConnect {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/todo_list", "root", "12345678");
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
            prepareStatement.setBoolean(1, isCompleted );
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