package Model;

import Helper.DBConnector;
import Helper.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Firm {

    private String city;
    private String name;
    private int id;

    public Firm(String city, String name, int id) {
        this.city = city;
        this.name = name;
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static ArrayList<Firm> getUserList(int user_id)
    {
        String query = "SELECT * FROM Firm WHERE user_id = ?";

        PreparedStatement pr = null;
        ResultSet rs = null;

        ArrayList<Firm> firms = new ArrayList<>();
        Firm firm = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1, user_id);
            rs = pr.executeQuery();
            while (rs.next())
            {
                firm = new Firm(Helper.cities[rs.getInt("city")], rs.getString("name"), rs.getInt("id"));
                firms.add(firm);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return firms;
    }

    public static boolean delete(int firm_id)
    {
        String query = "DELETE FROM Reservation WHERE firm_id = ?";

        Connection connection = null;
        PreparedStatement pr = null;
        boolean deleted = false;
        boolean isSuccess = false;

        try {
            connection = DBConnector.getConnection();
            connection.setAutoCommit(false);
            pr = connection.prepareStatement(query);
            pr.setInt(1, firm_id);
            pr.executeUpdate();

            query = "DELETE FROM Car WHERE firm_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1, firm_id);
            pr.executeUpdate();

            query = "DELETE FROM Price WHERE firm_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1, firm_id);
            pr.executeUpdate();

            query = "DELETE FROM Service WHERE firm_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1, firm_id);
            pr.executeUpdate();

            query = "DELETE FROM Firm WHERE id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1, firm_id);
            if (pr.executeUpdate() != 0) deleted = true;
            connection.commit();
            isSuccess = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
                if (pr != null) pr.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return isSuccess && deleted;
    }

    public static boolean update(int city, int firm_id, String name)
    {
        String query = "UPDATE Firm SET city = ?, name = ? WHERE id = ?";

        PreparedStatement pr = null;
        boolean isSuccess = false;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,city);
            pr.setString(2, name);
            pr.setInt(3, firm_id);
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

    public static boolean add(int city, int user_id, String name)
    {
        String query = "INSERT INTO Firm (city, name, user_id) VALUES (?,?,?)";

        PreparedStatement pr = null;
        boolean isSuccess = false;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,city);
            pr.setString(2, name);
            pr.setInt(3, user_id);
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

}
