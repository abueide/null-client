package abueide.nullclient.data;

import com.gvaneyck.rtmp.ServerInfo;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import abueide.nullclient.util.Globals;
import abueide.nullclient.util.Util;
import abueide.nullclient.util.database.DataBase;

public class Profile {

    private DataBase database;

    public Profile(DataBase database) {
        this.database = database;
    }

    public Profile(String name, String region) {
        String status = "Using Null Client";
        String password = "";
        database = Util.createDataBase(Globals.PREF.get(Globals.PROFILE_DIR, null), name);
        database.executeStatement(String.format("insert into profile (name, password, status, region) values('%s', '%s', '%s', '%s');", name, password, status, region));
    }

    public Profile(String name, String status, String region) {
        String password = "";
        database = Util.createDataBase(Globals.PREF.get(Globals.PROFILE_DIR, null), name);
        database.executeStatement(String.format("insert into profile (name, password, status, region) values('%s', '%s', '%s', '%s');", name, password, status, region));
    }

    public Profile(String name, String password, String status, String region) {
        database = Util.createDataBase(Globals.PREF.get(Globals.PROFILE_DIR, null), name);
        database.executeStatement(String.format("insert into profile (name, password, status, region) values('%s', '%s', '%s', '%s');", name, password, status, region));
    }

    public void delete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Null Client Delete Profile");
        alert.setHeaderText("Delete profile " + this.getName() + " ?");
        alert.setContentText("Are you sure you want to delete this profile?\n\nAll relevant profile data (Keys, Contacts, Messages)\nwill be erased, and might not be recoverable.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            File db = new File(database.getDatabaseDir());
            if(db.delete()){
                System.out.println("Profile deleted successfully");
            }else{
                System.out.println("Failed to delete profile.");
            }
        }
    }

    public void sendMessage(Message message) {
        if (!message.isSent()) {
            database.executeStatement(String.format("insert into messages (sender, receiver, message, sent) values('%s','%s','%s',%d);",
                    message.getSender(), message.getReceiver(), message.getMessage(), 0));
        } else {
            database.executeStatement(String.format("insert into messages (sender, receiver, message, sent) values('%s','%s','%s',%d);",
                    message.getSender(), message.getReceiver(), message.getMessage(), 1));
        }
        //TODO: Attempt to send message if online and wait for reply. If no reply, or offline mark message as unsent;
    }

    public List<Message> getMessages(Friend friend) {
        List<Message> chatlog = new ArrayList<>();
        ResultSet resultSet = database.executeQuery("select * from messages");
        try {
            while (resultSet.next()) {
                        if (resultSet.getString("sender").equals(friend.getName()) || resultSet.getString("receiver").equals(friend.getName()))
                            chatlog.add(new Message(resultSet.getString("timestamp"), resultSet.getString("sender"), resultSet.getString("receiver"), resultSet.getString("message"), false));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatlog;
    }

    public String getName() {
        try {
            return database.executeQuery("select * from profile").getString("name");
        } catch (Exception e) {
            System.out.println("Unable to query profile");
            return null;
        }
    }

    public void setName(String name) {
        database.executeStatement("update profile set name = '" + name + "';");
    }

    public String getPassword() {
        try {
            return database.executeQuery("select * from profile").getString("password");
        } catch (Exception e) {
            System.out.println("Unable to query profile");
            return null;
        }
    }

    public void setPassword(String password) {
        database.executeStatement("update profile set password = '" + password + "';");
    }

    public ServerInfo getRegion() {
        try {
            return ServerInfo.fromString(database.executeQuery("select * from profile").getString("region"));
        } catch (Exception e) {
            System.out.println("Unable to query profile");
            return null;
        }
    }

    public void setRegion(String region) {
        database.executeStatement("update profile set region = '" + region + "';");
    }

    public String getStatus() {
        try {
            return database.executeQuery("select * from profile").getString("status");
        } catch (Exception e) {
            System.out.println("Unable to query profile");
            return null;
        }
    }

    public void setStatus(String status) {
        database.executeStatement("update profile set status = '" + status + "';");
    }


    public void addFriend(Friend friend) {
        database.executeStatement(String.format("insert into friends (name, alias, status, public_key) values('%s','%s','%s','%s');",
                friend.getName(), friend.getAlias(), friend.getStatus()));
    }

    public void updateFriend(Friend friend) {
        database.executeStatement(String.format("update friends set name = '%s', alias = '%s', status = '%s', public_key = '%s' where id = %d;", friend.getName(), friend.getAlias(), friend.getStatus(), friend.getUid()));
    }

    public void deleteFriend(Friend friend) {
        database.executeStatement(String.format("delete from messages where sender = %s or receiver = %s;", friend.getName(), friend.getName()));
        database.executeStatement(String.format("delete from friends where id = %s;", friend.getUid()));
    }

    public List<Friend> getFriends() {
        List<Friend> friends = new ArrayList<>();
        ResultSet rs = database.executeQuery("select * from friends;");

        try {
            while (rs.next()) {
                friends.add(new Friend(rs.getInt("id"), rs.getString("name"), rs.getString("alias"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friends;
    }

}