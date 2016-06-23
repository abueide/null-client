package abueide.nullclient.util.database;

import java.sql.*;

import abueide.nullclient.util.Globals;

public class DataBase {

    private String databaseDir;
    private Connection connection = null;

    public DataBase(String databaseDir) {
        this.databaseDir = databaseDir;
        open();
    }

    public void open() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseDir);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
            PreparedStatement stmt = connection.prepareStatement(Globals.createTableStatement());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection = null;
    }

    public ResultSet executeQuery(String query){
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            System.out.println("Query Failed");
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public String getDatabaseDir() {
        return databaseDir;
    }

    public void setDatabaseDir(String databaseDir) {
        this.databaseDir = databaseDir;
    }

}
