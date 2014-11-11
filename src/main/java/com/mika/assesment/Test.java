package com.mika.assesment;

import com.mika.assesment.model.ReportEntry;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Created by ТСД on 26.10.2014.
 */
public class Test {


    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost/postgres";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","password");

        Connection conn = DriverManager.getConnection(url, props);
        List<ReportEntry> reportEntries = executeStoredProcedure(conn, 1210633200l , 1213786800l);
    }

    private static List<ReportEntry> executeStoredProcedure(Connection conn, Long from, Long to) throws SQLException {
        // make sure autocommit is off
        conn.setAutoCommit(true);
        //conn.setAutoCommit(true);   // set true to fill report table
        // Procedure call.
       //PreparedStatement stmt = conn.prepareStatement("select iterateemployees(?, ?)");
       PreparedStatement stmt = conn.prepareStatement("select employee, workingHours from tst()");

        //PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employee ORDER BY employee_id");

        ResultSet results = (ResultSet)stmt.executeQuery();

//        while (results.next()) {
//            long empId = results.getLong(1);
//            System.out.print("Generating report for employee id : [" + empId + "] ...");
//            PreparedStatement statement1 = conn.prepareStatement("SELECT calculate( " + empId + " )");
//            statement1.executeQuery();
//            System.out.println(" | Successfully generated!");
//        }

        List<ReportEntry> reportEntries = new LinkedList<ReportEntry>();
        while (results.next()) {
            ResultSet rows = results;
//            while (rows.next()){

                System.out.println("Employee id : [" + rows.getLong(1) + "] " +
                        "working hours: [" + rows.getLong(2) + "]");
                ReportEntry reportEntry = new ReportEntry();
                reportEntry.setEmployeeId(rows.getLong(1));
                reportEntry.setWorkingHours(rows.getDouble(2));

                reportEntries.add(reportEntry);
//            }
//            rows.close();
        }
        results.close();

        return reportEntries;
    }


}
