package Model;

import Helper.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Renter extends User {

    private final String type = "renter";

    public Renter(String username, String password, String name, int id) {
        super(username, password, name, id);
    }

    public String getType() {
        return type;
    }
}
