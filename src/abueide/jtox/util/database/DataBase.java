package abueide.jtox.util.database;

import abueide.jtox.tox.Profile;
import abueide.jtox.util.Globals;
import abueide.jtox.tox.Profile;
import abueide.jtox.util.Globals;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

    String databaseDir;
    Connection connection = null;
    Statement statement = null;

    public DataBase(String databaseDir) {
        this.databaseDir = databaseDir;
        open();
    }

    public void open() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseDir);
            System.out.println(databaseDir);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //System.out.println("Opened database successfully");
        executeStatement(Globals.createTableStatement(), "Created table profiles successfully");
        executeStatement("VACUUM", "Vacuumed database");
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        connection = null;
        Globals.jtoxdb = null;
    }

    public void executeStatement(String s, String success) {
        if (connection != null) {
            try {
                statement = connection.createStatement();
                statement.executeUpdate(s);
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(success);
        }
    }

    public List<Profile> getProfiles() {
        List<Profile> profiles = new ArrayList<Profile>();
        try {
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


    }

    public String getDatabase() {
        return databaseDir;
    }

    public void setDatabase(String database) {
        this.databaseDir = database;
    }

}
