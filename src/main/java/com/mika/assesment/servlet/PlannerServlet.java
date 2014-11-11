package com.mika.assesment.servlet;

import com.google.gson.Gson;
import com.mika.assesment.model.ReportEntry;
import com.mika.assesment.service.PlannerService;
import org.joda.time.DateTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

/**
 * Created by ТСД on 26.10.2014.
 */
public class PlannerServlet extends DatabaseServlet {

    private PlannerService plannerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        plannerService = new PlannerService(connection);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // TODO: validate parameters and show errors
        Long startDate = Long.parseLong((String) req.getParameter("start")) / 1000;
        Long endDate   = Long.parseLong((String) req.getParameter("end")) / 1000;

        System.out.println("Start date: " + startDate );
        System.out.println("End date: " + endDate );

        List<ReportEntry> reportEntries = plannerService.getReport(startDate, endDate);

        resp.getOutputStream().write(new Gson().toJson(reportEntries).getBytes());
    }

}
