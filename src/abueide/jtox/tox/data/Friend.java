package abueide.jtox.tox.data;

/**
 * Created by gratin on 5/20/16.
 */
public class Friend {

    int uid = -1;
    String name = "Unknown";
    String alias = "";
    String status = "";
    String public_key = "";


    public Friend(){}

    public Friend(int uid, String name, String alias, String status, String public_key){
        this.uid = uid;
        this.name = name;
        this.alias = alias;
        this.status = status;
        this.public_key = public_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublicKey() {
        return public_key;
    }

    public void setPublicKey(String public_key) {
        this.public_key = public_key;
    }

    public int getUid(){
        return uid;
    }
}
