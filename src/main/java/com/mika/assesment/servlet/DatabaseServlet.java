package com.mika.assesment.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by ТСД on 10.11.2014.
 */
public abstract class DatabaseServlet extends HttpServlet{

    protected Connection connection;
    private Properties p;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String reportDir = getServletContext().getRealPath("/WEB-INF/classes");
        String fileName = "confing.properties";

        FileInputStream input = null;
        p = new Properties();
        try {
            input = new FileInputStream( reportDir + File.separator + fileName );
            p.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url1 = p.getProperty("jdbc.url");
        String dbname = p.getProperty("bbdd.sid");
        String user = p.getProperty("bbdd.user");
        String paswd = p.getProperty("bbdd.password");
        String url = url1 + dbname;

        Properties props = new Properties();
        props.setProperty("user",user);
        props.setProperty("password",paswd);

        try {
            connection = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
