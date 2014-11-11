package com.mika.assesment.service;

import com.mika.assesment.model.ReportEntry;
import org.joda.time.DateTime;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ТСД on 26.10.2014.
 */
public class PlannerService {

    private Connection connection;

    private PreparedStatement preparedStatement;

    public PlannerService(Connection connection) {
        this.connection = connection;
    }

    public List<ReportEntry> getReport(Long from, Long to) {
        // TODO : handling exceptions and release resources
        ResultSet results = null, rows = null;
        try {
            if (preparedStatement == null) {
                preparedStatement = connection.prepareStatement("SELECT * from generate_report(?, ?)");
            }

            connection.setAutoCommit(false);
            preparedStatement.setLong(1, from);
            preparedStatement.setLong(2, to);

            results = (ResultSet) preparedStatement.executeQuery();
            long t1 = System.currentTimeMillis();
            List<ReportEntry> reportEntries = new LinkedList<ReportEntry>();
            while (results.next()) {
                //rows = (ResultSet) results.getObject(1);
                //while (rows.next()) {
                    ReportEntry reportEntry = new ReportEntry();
                    //reportEntry.setEmployeeId(rows.getLong(1));
                    reportEntry.setEmployeeId(results.getLong(1));
                    reportEntry.setWorkingHours(results.getDouble(2));
                    reportEntries.add(reportEntry);
                //}
            }

            long t2 = System.currentTimeMillis();

            long diff = t2 - t1;
            System.out.println("diff = " + diff);
            return reportEntries;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (results != null) {
                    results.close();
                }

                if (rows != null) {
                    rows.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

}
