package com.mika.assesment.service;

import com.mika.assesment.model.ReportEntry;
import com.mika.assesment.model.ReportType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;

import javax.servlet.ServletException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Service provides report operations.
 *
 * @author Mikhail Gushinets
 * @since 07/11/14
 */
public class ReportService {

    private static String REPORT_QUERY = "SELECT employeeid, workinghours FROM generate_report(?,?)";

    private Connection connection;

    private PreparedStatement preparedStatement;

    /**
     * Service constructor.
     *
     * @param connection - database connection
     */
    public ReportService(Connection connection) {
        this.connection = connection;
    }

    /**
     * Get Report entries for all workers for specified time period.
     *
     * @param from report time start
     * @param to   report time end
     * @return list of report entries
     */
    public List<ReportEntry> getReport(Long from, Long to) {

        if (from == null) {
            throw new IllegalArgumentException("report start date cannot be null");
        }

        if (to == null) {
            throw new IllegalArgumentException("report end date cannot be null");
        }

        if (to < from) {
            throw new IllegalArgumentException("Report end date must be after start date");
        }

        ResultSet results = null;
        try {

            if (preparedStatement == null) {
                preparedStatement = connection.prepareStatement(REPORT_QUERY);
            }

            connection.setAutoCommit(false);

            preparedStatement.setLong(1, from);
            preparedStatement.setLong(2, to);

            results = preparedStatement.executeQuery();
            long t1 = System.currentTimeMillis();
            List<ReportEntry> reportEntries = new LinkedList<ReportEntry>();
            while (results.next()) {
                ReportEntry reportEntry = new ReportEntry();
                reportEntry.setEmployeeId(results.getLong(1));
                reportEntry.setWorkingHours(results.getDouble(2));
                reportEntries.add(reportEntry);
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }

    /**
     * Generate report document for specified period.
     *
     * @param templatePath path to compiled jasper report
     * @param type         type for generated report document
     * @param from         report start time
     * @param to           report end time
     * @return report byte array
     * @throws ServletException in case of
     * @throws IOException
     */
    public byte[] generateReport(String templatePath, ReportType type, Long from, Long to) throws ServletException, IOException {

        if (from == null) {
            throw new IllegalArgumentException("report start date cannot be null");
        }

        if (to == null) {
            throw new IllegalArgumentException("report end date cannot be null");
        }

        if (to < from) {
            throw new IllegalArgumentException("Report end date must be after start date");
        }

        ResultSet results = null;

        try {

            if (preparedStatement == null) {
                preparedStatement = connection.prepareStatement(REPORT_QUERY);
            }

            connection.setAutoCommit(false);

            preparedStatement.setLong(1, from);
            preparedStatement.setLong(2, to);

            results = preparedStatement.executeQuery();

            JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(results);
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            try {

                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("ReportTitle", "Capacity Per Employee");

                JasperPrint jasperPrint = JasperFillManager.fillReport(templatePath, parameters, resultSetDataSource);
                if (type == ReportType.PDF) {
                    JasperExportManager.exportReportToPdfStream(jasperPrint, os);
                } else {

                    JRCsvExporter csvExporter = new JRCsvExporter();
                    csvExporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, ",");
                    csvExporter.setParameter(JRCsvExporterParameter.RECORD_DELIMITER, "\n");
                    csvExporter.setParameter(JRCsvExporterParameter.JASPER_PRINT, jasperPrint);
                    csvExporter.setParameter(JRCsvExporterParameter.IGNORE_PAGE_MARGINS, true);
                    csvExporter.setParameter(JRCsvExporterParameter.OUTPUT_STREAM, os);
                    csvExporter.exportReport();

                }

                return os.toByteArray();

            } catch (JRException e) {
                e.printStackTrace();
                throw new RuntimeException("Error occurred in report generation", e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


}
