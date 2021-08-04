package Model;

import Helper.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Car {

    private String type;
    private String model;
    private String brand;
    private int year;
    private int id;

    public Car(String type, String model, String brand, int year, int id) {
        this.type = type;
        this.model = model;
        this.brand= brand;
        this.id = id;

        Date now = new Date();
        int currentYear = Integer.parseInt(now.toInstant().toString().substring(0,4));
        if (year < 1908) this.year = 1908;
        else this.year = Math.min(year, currentYear);

    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {

        Date now = new Date();
        int currentYear = Integer.parseInt(now.toInstant().toString().substring(0,4));
        if (year < 1908) this.year = 1908;
        else this.year = Math.min(year, currentYear);

    }

    public static boolean add(String type, String model, String brand, int year, int firm_id)
    {
        // check valid year
        Date now = new Date();
        int currentYear = Integer.parseInt(now.toInstant().toString().substring(0,4));
        if (year < 1908) year = 1908;
        else year = Math.min(year, currentYear);
        // ### check valid year

        String query = "INSERT INTO Car (firm_id, type, model, brand, year) VALUES (?,?,?,?,?)";

        boolean isSuccess = false;

        PreparedStatement pr = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1,firm_id);
            pr.setString(2,type);
            pr.setString(3,model);
            pr.setString(4, brand);
            pr.setInt(5,year);
            if (pr.executeUpdate() == 0) return false;
            isSuccess = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (pr != null) {
                try {
                    pr.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    return isSuccess;
    }

    public static boolean delete(int car_id)
    {
        String query = "DELETE FROM Reservation WHERE car_id = ?";

        Connection connection = null;
        PreparedStatement pr = null;
        boolean isSuccess = false;
        boolean deleted = false;

        try {
            connection = DBConnector.getConnection();
            connection.setAutoCommit(false);
            pr = connection.prepareStatement(query);
            pr.setInt(1,car_id);
            pr.executeUpdate();

            query = "DELETE FROM Price WHERE car_id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,car_id);
            pr.executeUpdate();

            query = "DELETE FROM Car WHERE id = ?";
            pr = connection.prepareStatement(query);
            pr.setInt(1,car_id);
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

    public static ArrayList<Car> getList(int firm_id)
    {
        String query = "SELECT * FROM Car WHERE firm_id = ?";

        ArrayList<Car> cars = new ArrayList<>();
        Car car = null;

        PreparedStatement pr = null;
        ResultSet rs = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1, firm_id);
            rs = pr.executeQuery();
            while (rs.next())
            {
                car = new Car(rs.getString("type"), rs.getString("model"), rs.getString("brand"), rs.getInt("year"), rs.getInt("id"));
                cars.add(car);
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
        return cars;
    }

    public static boolean update(String type, String model, String brand, int year, int car_id)
    {
        // check valid year
        Date now = new Date();
        int currentYear = Integer.parseInt(now.toInstant().toString().substring(0,4));
        if (year < 1908) year = 1908;
        else year = Math.min(year, currentYear);
        // ### check valid year

        String query = "UPDATE Car SET type = ?, model = ?, brand = ?, year = ? WHERE id = ?";

        boolean isSuccess = false;

        PreparedStatement pr = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setString(1, type);
            pr.setString(2, model);
            pr.setString(3, brand);
            pr.setInt(4, year);
            pr.setInt(5, car_id);
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
