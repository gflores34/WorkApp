package app.workapp;

import java.sql.*;
import java.util.ArrayList;

public class partsDatabase {
    String jdbcURL, databaseName;

    public partsDatabase(String databaseName){
        this.databaseName = databaseName;
        jdbcURL = "jdbc:derby:"+ databaseName + ";create=true";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL);
            //System.out.println(databaseName + " database created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(String productName){

        try {
            Connection connection = DriverManager.getConnection(jdbcURL);
            String sql = "Create Table " + productName + " (id int not null generated always as identity,"
                        + "partNum varchar(128), partName varchar(128), description varchar(128))";

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            //System.out.println("Table " + productName + " created");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertRow(String table, String partNum, String partName, String desc){
        try {
            Connection connection = DriverManager.getConnection(jdbcURL);
            String sql = "Insert into " + table + " (partNum, partName, description) values ('" + partNum + "','"
                    + partName + "','" + desc + "')";

            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(String tableName, String partNum){
        try {
            Connection connection = DriverManager.getConnection(jdbcURL);
            String sql = "DELETE FROM " + tableName + " WHERE partNum = '" + partNum + "'";

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            //System.out.println(partNum + " deleted from table " + tableName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> readAllProducts(){
        ArrayList<String> products = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(jdbcURL);

            DatabaseMetaData metaData = connection.getMetaData();
            String[] types = {"TABLE"};
            //Retrieving the columns in the database
            ResultSet tables = metaData.getTables(null, null, "%", types);
            while (tables.next()) {
                products.add(tables.getString("TABLE_NAME"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    boolean tableExists(String tableName) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL);

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, new String[] {"TABLE"});

        return resultSet.next();
    }

    public ArrayList<Part> read(String tableName){
        ArrayList<Part> rows = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(jdbcURL);
            String sql = "SELECT * FROM " + tableName;


            Statement statement = connection.createStatement();

            ResultSet results = statement.executeQuery(sql);

            while(results.next()) {

                int id = results.getInt("id");
                String partNum = results.getString("partNum");
                String partName = results.getString("partName");
                String description = results.getString("description");

                Part temp = new Part(tableName, partNum, partName, description);
                rows.add(temp);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

}
