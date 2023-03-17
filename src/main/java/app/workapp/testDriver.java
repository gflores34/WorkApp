package app.workapp;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class testDriver {
    public static void main(String[] args) throws SQLException {

        tsDatabase test = new tsDatabase("tsdb");


        //test.createTable("eax2500");
        //test.createTable("calls");


        //test.insertRow("eax2500","102661", "EAX-2500 Board", "Circuit board replacement for an EAX-2500");
        test.insertRow("eax2500","102660", "EAX-2500 cam assembly", "Cam assembly for an EAX-2500");
        //test.insertRow("V40", "104358-4", "EB fillerplate", "Fillerplate EB assembly for a V40 36\"");

        //test.deleteRow("eax2500", "102661");
        //test.deleteRow("eax2500", "102619");
        //test.deleteRow("V40", "104358-4");

        //ArrayList<Part> eax2500Parts = test.read("eax2500");

        //ArrayList<Part> parts = test.read("v40");

        //String testString = "104358-3";

        //System.out.println(!test.tableExists("v40"));
    }
}
