package com.mika.assesment.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Simple database Servlet keeps persistent database connection to work with.
 * Derived classes should use it to perform persistent operations (e.g. stored procedure invocations).
 *
 * @author Mikhail Gushinets
 * @since 07/11/14
 */
public abstract class DatabaseServlet extends HttpServlet{

    protected Connection connection;

    private Properties databaseProperties;

    private static final String CONFIG_FILE_PARAMETER = "config.name";

    private static final String POSTGRES_URL_PATTERN = "jdbc.url=jdbc:postgresql://%s:%s/%s";

    private static final String POSTGRES_DRIVER_CLASS = "org.postgresql.Driver";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String applicationPath = getServletContext().getRealPath("/WEB-INF/classes");

        String fileName = config.getInitParameter(CONFIG_FILE_PARAMETER);

        databaseProperties = new Properties();
        String configurationPath = applicationPath + File.separator + fileName;
        try {
            FileInputStream input = new FileInputStream(configurationPath);
            databaseProperties.load(input);
        } catch (FileNotFoundException e) {
            throw new ServletException("Application config file is not found by following path " + configurationPath);
        } catch (IOException e) {
            throw new ServletException("Cannot load configuration properties");
        }

        try {
            Class.forName(POSTGRES_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            throw new ServletException("Postgres driver class is not found. Please check if it in the Class path.");
        }


        String host = databaseProperties.getProperty("bbdd.host");

        if (host == null) {
            host = "127.0.0.1";
        }

        String port = databaseProperties.getProperty("bbdd.port");

        if (port == null) {
            port = "5432";
        }

        String sid = databaseProperties.getProperty("bbdd.sid");
        if (sid == null) {
            throw new IllegalArgumentException("Database name property (bbdd.sid) can not be null");
        }

        String user = databaseProperties.getProperty("bbdd.user");
        if (user == null) {
            throw new IllegalArgumentException("Database username property (bbdd.user) can not be null");
        }

        String password = databaseProperties.getProperty("bbdd.password");
        if (password == null) {
            throw new IllegalArgumentException("Database password property (bbdd.password) can not be null");
        }

        String url = String.format(POSTGRES_URL_PATTERN, host, port, sid);

        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);

        try {
            connection = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect to database", e);
        }
    }
}
