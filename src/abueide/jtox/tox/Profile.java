package abueide.jtox.tox;

import abueide.jtox.util.Globals;
import abueide.jtox.util.Util;
import abueide.jtox.util.database.DataBase;

import java.io.File;

public class Profile {

    private DataBase database;

    public Profile(DataBase database){
        this.database = database;
    }

    public Profile(String name) {
        String description = "Using jTox";
        String public_key = "";
        String private_key = "";
        database = Util.createDataBase(Globals.PREF.get(Globals.PROFILE_DIR, null), name, Globals.DB_EXT);
        database.executeStatement(
                "insert into profile (name, description, public_key, private_key) values('" +
                        name + "','" +
                        description + "','" +
                        public_key + "','" +
                        private_key+ "');"
        );
    }

    public Profile(String name, String description) {
        String public_key = "";
        String private_key = "";
        database = Util.createDataBase(Globals.PREF.get(Globals.PROFILE_DIR, null), name, Globals.DB_EXT);
        database.executeStatement(
                "insert into profile (name, description, public_key, private_key) values('" +
                        name + "','" +
                        description + "','" +
                        public_key + "','" +
                        private_key+ "');"
        );
    }

    public Profile(String name, String description, String public_key, String private_key) {
        database = Util.createDataBase(Globals.PREF.get(Globals.PROFILE_DIR, null), name, Globals.DB_EXT);
        database.executeStatement(
                "insert into profile (name, description, public_key, private_key) values('" +
                        name + "','" +
                        description + "','" +
                        public_key + "','" +
                        private_key+ "');"
        );
    }

    public String getName() {
        try{
            return database.executeQuery("select * from profile").getString("name");
        }catch(Exception e){
            System.out.println("Unable to query profile");
            return null;
        }
    }

    public String getDescription() {
        try{
            return database.executeQuery("select * from profile").getString("description");
        }catch(Exception e){
            System.out.println("Unable to query profile");
            return null;
        }
    }

    public void setName(String name) {
        database.executeStatement("update profile set name = '" + name + "';");
    }

    public void setDescription(String description) {
        database.executeStatement("update profile set description = '" + description + "';");
    }


    public void delete(){
        //TODO: Confirmation popup box
        File db = new File(database.getDatabaseDir());
        db.delete();
    }

}
