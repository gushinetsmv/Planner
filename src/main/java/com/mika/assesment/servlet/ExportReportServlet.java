package com.mika.assesment.servlet;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.repo.OutputStreamResource;

import java.sql.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExportReportServlet extends DatabaseServlet {

    private static final int BYTES_DOWNLOAD = 1024;

    private enum ReportType {
        CSV,
        PDF
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        performTask(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
	performTask(request, response);
    }

    /*private byte[] getReportOs(String templatePath, JRDataSource resultSetDataSource, ReportType type) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {

            Map parameters = new HashMap();
            parameters.put("ReportTitle", "Capacity Per Employee");

            JasperPrint jasperPrint = JasperFillManager.fillReport(templatePath, parameters, resultSetDataSource);
            if (type == ReportType.PDF) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, os);
            } else {

                // This is the part to export to a CSV

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
            throw new RuntimeException(e);
        }

    }


    private JRDataSource getReportDataSource (Long start, Long end) {
        String sql = String.format("SELECT employeeid, workinghours from generate_report(%s, %s)", start, end);

        ResultSet results = null;
        Statement stmt = null;

        try {
            stmt = connection.createStatement();
            results = (ResultSet) stmt.executeQuery(sql);
            // to do change on JRBeanCollectionDataSource impl
            JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(results);
            return resultSetDataSource;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
//            try {
//                if (results != null) {
//                    results.close();
//                }
//
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }
        }
    }*/

    private byte[] getOutputContent(String templatePath, Long start, Long end, ReportType type ) {

        String sql = String.format("SELECT employeeid, workinghours from generate_report(%s, %s)", start, end);

        ResultSet results = null;
        Statement stmt = null;

        try {
            stmt = connection.createStatement();
            results = (ResultSet) stmt.executeQuery(sql);

            JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(results);

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            try {

                Map parameters = new HashMap();
                parameters.put("ReportTitle", "Capacity Per Employee");

                JasperPrint jasperPrint = JasperFillManager.fillReport(templatePath, parameters, resultSetDataSource);
                if (type == ReportType.PDF) {
                    JasperExportManager.exportReportToPdfStream(jasperPrint, os);
                } else {

                    // This is the part to export to a CSV

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
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

    }


    private void performTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Path to your .jasper file in your package
        String reportDir = getServletContext().getRealPath("/WEB-INF/classes");
        String sourceFileName = "jasper_report_template.jasper";
        String templatePath = reportDir + File.separator + sourceFileName;

        Long startDate = Long.parseLong((String) request.getParameter("start")) / 1000;
        Long endDate = Long.parseLong((String) request.getParameter("end")) / 1000;
        String reportType = request.getParameter("reportType");

        ReportType type = reportType != null && reportType.equalsIgnoreCase("PDF") ? ReportType.PDF : ReportType.CSV;

        String contextPath = getServletContext().getRealPath(File.separator);

        byte[] exportBytes = getOutputContent( templatePath, startDate, endDate, type );

//        JRDataSource reportDataSource = getReportDataSource(startDate, endDate);
//        byte[] exportBytes = getReportOs(reportDir + File.separator + sourceFileName, reportDataSource, type);


        response.setContentType(String.format("application/%s", type == ReportType.CSV ? "csv" : "pdf"));
        response.addHeader("Content-Disposition", "attachment; filename=report."
                + (type == ReportType.CSV ? "csv" : "pdf"));
        response.setContentLength(exportBytes.length);
        OutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(exportBytes);
    }
}