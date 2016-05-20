package abueide.jtox.tox.data;

/**
 * Created by gratin on 5/20/16.
 */
public class Message {

    private String time = "";
    private String sender = "";
    private String receiver = "";
    private String message = "";
    private boolean sent = false;

    public Message(String sender, String receiver, String message){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Message(String sender, String receiver, String message, boolean sent){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.sent = sent;
    }

    public Message(String time, String sender, String receiver, String message, boolean sent){
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.sent = sent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
