package abueide.nullclient.data;

import abueide.nullclient.util.Globals;
import abueide.nullclient.util.Util;
import abueide.nullclient.util.database.DataBase;
import com.github.theholywaffle.lolchatapi.ChatServer;
import com.github.theholywaffle.lolchatapi.FriendRequestPolicy;
import com.github.theholywaffle.lolchatapi.LolChat;
import com.github.theholywaffle.lolchatapi.wrapper.Friend;
import com.gvaneyck.rtmp.LoLRTMPSClient;
import com.gvaneyck.rtmp.ServerInfo;
import com.gvaneyck.rtmp.encoding.TypedObject;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Andrew Bueide on 5/20/16.
 */
public class Profile {

    private DataBase database;

    private boolean loggedInClient = false;
    private boolean loggedInChat = false;

    private LoLRTMPSClient client = null;
    private LolChat lolChat = null;
    private TypedObject summoner = null;


    public Profile(DataBase database) {
        this.database = database;
        client = new LoLRTMPSClient(this.getRegion(), Globals.LEAGUE_CLIENT_VERSION, this.getName(), this.getPassword());
        //Feels hacky but I don't feel like modifying lolchat to use the ServerInfo object
        for (ChatServer chatServer : ChatServer.values()) {
            if (chatServer.name().equalsIgnoreCase(this.getRegion().platform)) {
                lolChat = new LolChat(chatServer, FriendRequestPolicy.ACCEPT_ALL);
            }
        }
    }

    public Profile(String name, String region) {
        this(name, "", "Using Nullclient!", region);
    }

    public Profile(String name, String status, String region) {
        this(name, status, "", region);
    }

    public Profile(String name, String password, String status, String region) {
        this(Util.createProfile(name, password, status, region));
    }

    public void connectAndLogin() {
        try {
            client.connectAndLogin();
            System.out.println("Logged into login server!");
            int id = client.invoke("clientFacadeService",
                    "getLoginDataPacketForUser", new Object[]{});
            summoner = client.getResult(id).getTO("data").getTO("body");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lolChat.login(this.getName(), this.getPassword())) {
            System.out.println("Logged into chat server!");
        }
    }

    public void disconnect() {
        client.close();
        lolChat.disconnect();
    }

    public void delete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Null Client Delete Profile");
        alert.setHeaderText("Delete profile " + this.getName() + " ?");
        alert.setContentText("Are you sure you want to delete this profile?\n\nAll relevant profile data (Keys, Contacts, Messages)\nwill be erased, and might not be recoverable.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            File db = new File(database.getDatabaseDir());
            if (db.delete()) {
                System.out.println("Profile deleted successfully");
            } else {
                System.out.println("Failed to delete profile.");
            }
        }
    }

    public void saveMessage(Message message) {
        try {
            PreparedStatement stmt = database.getConnection().prepareStatement("insert into messages (sender, receiver, message, sent) values(?, ?, ?, ?)");
            stmt.setString(1, message.getSender());
            stmt.setString(2, message.getReceiver());
            stmt.setString(3, message.getMessage());
            stmt.setInt(4, 1); // If message is sent; not used. 1 = true.
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try {
            PreparedStatement stmt = database.getConnection().prepareStatement("update profile set name = ?");
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try {
            PreparedStatement stmt = database.getConnection().prepareStatement("update profile set password = ?");
            stmt.setString(1, password);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try {
            PreparedStatement stmt = database.getConnection().prepareStatement("update profile set region = ?");
            stmt.setString(1, region);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try {
            PreparedStatement stmt = database.getConnection().prepareStatement("update profile set status = ?");
            stmt.setString(1, status);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LoLRTMPSClient getClient() {
        return client;
    }

    public LolChat getChatClient() {
        return lolChat;
    }

    public TypedObject getSummoner() {
        return summoner;
    }
}
