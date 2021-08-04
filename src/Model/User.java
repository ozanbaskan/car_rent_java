package Model;

import Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class User {

    private String username;
    private String password;
    private String name;
    private int id;
    private String type;

    public User(String username, String password, String name, int id) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static boolean add(String username, String password, String name, String type)
    {
        String query = "INSERT INTO Users (username, name, password, type) VALUES (?,?,?,?)";
        boolean isSuccess = false;

        PreparedStatement pr = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2, name);
            pr.setString(3,password);
            pr.setString(4,type);
            isSuccess = pr.executeUpdate() != 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (pr != null) pr.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static boolean update(int user_id, String username, String password, String name, String type)
    {
        String query = "UPDATE Users SET username = ?, password = ?, name = ?, type = ? WHERE id = ?";
        boolean isSuccess = false;

        PreparedStatement pr = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2,password);
            pr.setString(3, name);
            pr.setString(4,type);
            pr.setInt(5, user_id);
            isSuccess = pr.executeUpdate() != 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (pr != null) pr.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static User get(String username, String password)
    {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";

        User user = null;

        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1,username);
            pr.setString(2,password);
            rs = pr.executeQuery();
            if (rs.next())
            {
                String type = rs.getString("type");
                switch (type)
                {
                    case "Müşteri" -> {
                        user = new Renter(rs.getString("username"), rs.getString("password"), rs.getString("name"), rs.getInt("id"));
                    }
                    case "Firma" -> {
                        user = new FirmUser(rs.getString("username"), rs.getString("password"), rs.getString("name"), rs.getInt("id"));
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (pr != null) pr.close();
                if (rs != null) rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    return user;
    }


}
