package Model;

public class FirmUser extends User{

    private final String type = "firmUser";

    public FirmUser(String username, String password, String name, int id) {
        super(username, password, name, id);
    }

    public String getType() {
        return type;
    }
}
