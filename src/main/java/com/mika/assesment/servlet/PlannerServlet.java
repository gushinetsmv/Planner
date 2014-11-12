package com.mika.assesment.servlet;

import com.google.gson.Gson;
import com.mika.assesment.model.ReportEntry;
import com.mika.assesment.service.ReportService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Planner servlet generates report data and response it as json.
 * It expects two request parameters 'start' and 'end' as report period in ms.
 *
 * @author Mikhail Gushinets
 * @since 07/11/14
 */
public class PlannerServlet extends DatabaseServlet {

    private ReportService plannerService;

    private static int MS_PER_SEC = 1000;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        plannerService = new ReportService(connection);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long startDate = Long.parseLong(req.getParameter("start")) / MS_PER_SEC;
        Long endDate = Long.parseLong(req.getParameter("end")) / MS_PER_SEC;

        List<ReportEntry> reportEntries = plannerService.getReport(startDate, endDate);
        resp.getOutputStream().write(new Gson().toJson(reportEntries).getBytes());
    }

}
