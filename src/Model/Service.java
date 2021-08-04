package Model;

import Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Service {

    private String description;
    private String name;
    private int cost;
    private int id;

    public Service(String description, String name, int cost, int id) {
        this.name = name;
        this.description = description;
        this.cost = Math.max(0, cost);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = Math.max(0, cost);
    }

    public static boolean add(int firm_id, String name, String description, int cost)
    {
        String query = "INSERT INTO Service (firm_id, name, description, cost) VALUES (?,?,?,?)";

        PreparedStatement pr = null;
        boolean isSuccess = false;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1, firm_id);
            pr.setString(2, name);
            pr.setString(3, description);
            pr.setInt(4, cost);
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

    public static boolean delete(int service_id)
    {
        String query = "DELETE FROM Service WHERE id = ?";

        PreparedStatement pr = null;
        boolean isSuccess = false;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1, service_id);
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

    public static ArrayList<Service> getList(int firm_id)
    {
        String query = "SELECT * FROM Service WHERE firm_id = ?";

        PreparedStatement pr = null;
        ResultSet rs = null;

        ArrayList<Service> services = new ArrayList<>();
        Service service = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1, firm_id);
            rs = pr.executeQuery();
            while (rs.next())
            {
                service = new Service(rs.getString("description"), rs.getString("name"), rs.getInt("cost"), rs.getInt("id"));
                services.add(service);
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
        return services;
    }

}
