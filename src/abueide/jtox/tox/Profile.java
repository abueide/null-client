package abueide.jtox.tox;

public class Profile {

    private int uid = -1;
    private String name = "";
    private String encryptionKey = "";

    public Profile() {
    }

    public Profile(int uid, String name, String encryptionKey) {
        this.uid = uid;
        this.name = name;
        this.encryptionKey = encryptionKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public boolean isEncrypted() {
        return !encryptionKey.isEmpty();
    }

    public int getuid() {
        return uid;
    }


}
