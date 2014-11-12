package com.mika.assesment.servlet;

import com.mika.assesment.model.ReportType;
import com.mika.assesment.service.ReportService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Servlet generates report documents in different format (e.g. pdf, csv).
 *
 * @author Mikhail Gushinets
 * @since 07/11/14
 */
public class ExportReportServlet extends DatabaseServlet {


    private ReportService plannerService;

    private static final String JASPER_TEMPLATE_FILE = "jasper.template";

    private static int MS_PER_SEC = 1000;

    private String jasperReportTemplate;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        plannerService = new ReportService(connection);

        jasperReportTemplate = config.getInitParameter(JASPER_TEMPLATE_FILE);

        if (jasperReportTemplate == null) {
            throw new IllegalArgumentException("Jasper template cannot be null.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String reportDir = getServletContext().getRealPath("/WEB-INF/classes");
        String templatePath = reportDir + File.separator + jasperReportTemplate;

        String reportType = request.getParameter("reportType");
        ReportType type = reportType != null && reportType.equalsIgnoreCase("PDF") ? ReportType.PDF : ReportType.CSV;

        Long startDate = Long.parseLong(request.getParameter("start")) / MS_PER_SEC;
        Long endDate = Long.parseLong(request.getParameter("end")) / MS_PER_SEC;

        byte[] report = plannerService.generateReport(templatePath, type, startDate, endDate);

        response.setContentType(String.format("application/%s", type == ReportType.CSV ? "csv" : "pdf"));
        response.addHeader("Content-Disposition", "attachment; filename=report."
                + (type == ReportType.CSV ? "csv" : "pdf"));
        response.setContentLength(report.length);
        OutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(report);
    }
}