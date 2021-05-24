package model;

import util.UtilMethods;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;
import java.util.stream.Collectors;

import static source.Constant.DB_PROPERTIES_PATH;

public class DBConnection {

    private String url;
    private String driverName;
    private String user;
    private String password;

    private static DBConnection instance;

    public synchronized static DBConnection getInstance(){
        if (instance == null) instance = new DBConnection();
        return instance;
    }

    private DBConnection(){
    }

    public void start() throws ClassNotFoundException, IOException {
        Properties properties = UtilMethods.readProperties(DB_PROPERTIES_PATH);
        url = properties.getProperty("url");
        driverName = properties.getProperty("driver_name");
        user = properties.getProperty("user");
        password = properties.getProperty("password");

        System.out.println("Database start.");

    }

    public void init(String dbInitScriptPath) throws ClassNotFoundException {
        Class.forName(driverName);
        try {
            String sql = Files.lines(Paths.get(dbInitScriptPath)).collect(Collectors.joining(" "));
            getConnection().createStatement().executeUpdate(sql);
            System.out.println("Database init.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }

}
