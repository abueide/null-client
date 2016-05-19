package abueide.jtox.util.database;

import abueide.jtox.tox.Profile;
import abueide.jtox.util.Globals;
import abueide.jtox.tox.Profile;
import abueide.jtox.util.Globals;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        //System.out.println("Opened database successfully");
            executeStatement(Globals.createTableStatement());
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection = null;
    }

    public void executeStatement(String s) {
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(s);
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    /*public List<Profile> getProfiles() {
        List<Profile> profiles = new ArrayList<Profile>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from profiles;");
            while (rs.next()) {
                profiles.add(new Profile(rs.getInt("id"), rs.getString("name"), rs.getString("encryption_key")));
            }
            return profiles;
        } catch (SQLException e) {
            System.out.println("Retrieving profiles from database failed");
            e.printStackTrace();
            return null;
        }


    }*/

    public String getDatabaseDir() {
        return databaseDir;
    }

    public void setDatabaseDir(String databaseDir) {
        this.databaseDir = databaseDir;
    }

}
