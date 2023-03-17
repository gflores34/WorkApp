package app.workapp;

import java.sql.*;
import java.util.ArrayList;

public class tsDatabase {
    String jdbcURL, databaseName;

    public tsDatabase(String databaseName) {
        this.databaseName = databaseName;
        jdbcURL = "jdbc:derby:" + databaseName + ";create=true";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL);
            //System.out.println(databaseName + " database created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(String tableName) {

        try {
            Connection connection = DriverManager.getConnection(jdbcURL);
            String sql = "Create Table " + tableName + " (id int not null generated always as identity,"
                    + "refNumber varchar(128), truesourcenum varchar(128), sitenum varchar(128))";

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            //System.out.println("Table " + productName + " created");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertRow(String tableName, String refNum, String tsNum, String siteNum) {
        try {
            Connection connection = DriverManager.getConnection(jdbcURL);
            String sql = "Insert into " + tableName + "(refNumber, truesourcenum, siteNum) values ('" + refNum + "','"
                    + tsNum + "','" + siteNum + "')";

            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(String tableName, String refNum) {
        try {
            Connection connection = DriverManager.getConnection(jdbcURL);
            String sql = "DELETE FROM " + tableName + " WHERE refNumber = " + refNum;

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            //System.out.println(partNum + " deleted from table " + tableName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean tableExists(String tableName) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL);

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, new String[]{"TABLE"});

        return resultSet.next();
    }


}