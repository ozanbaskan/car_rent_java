package Model;

import Helper.DBConnector;
import Helper.Helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Reservation extends DateInterval{

    private int firm_id;
    private int car_id;
    private String model;
    private String brand;
    private String type;
    private int id;

    public Reservation(Calendar start, Calendar end, int price, int id) {
        super(start, end, price, id);
    }

    public Reservation(Calendar start, Calendar end, int price, int id, int firm_id, int car_id, String model, String brand, String type) {
        super(start, end, price, id);
        this.model = model;
        this.brand = brand;
        this.type = type;
        this.firm_id = firm_id;
        this.car_id = car_id;
        this.id = id;
    }

    public int getFirm_id() {
        return firm_id;
    }

    public void setFirm_id(int firm_id) {
        this.firm_id = firm_id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static boolean add(int firm_id, int car_id, int user_id, Date first, Date last, int price)
    {
        String query = "SELECT first,last FROM Reservation WHERE car_id = ?";

        java.sql.Date sqlFirst = new java.sql.Date(first.getTime());
        java.sql.Date sqlLast = new java.sql.Date(last.getTime());

        Connection connection = null;
        PreparedStatement pr = null;
        ResultSet rs = null;

        boolean invalidDate = false;
        boolean isSuccess = false;

        try {
            connection = DBConnector.getConnection();
            pr = connection.prepareStatement(query);
            pr.setInt(1,car_id);
            rs = pr.executeQuery();
            while (rs.next())
            {
                Date rsSqlFirst = rs.getDate("first");
                Date rsSqlLast = rs.getDate("last");

                if
                (
                        (rsSqlFirst.compareTo(first) <= 0 && rsSqlLast.compareTo(first) >= 0)
                                ||
                                (rsSqlFirst.compareTo(last) <= 0 && rsSqlLast.compareTo(last) >= 0)
                                ||
                                (rsSqlFirst.compareTo(first) >= 0 && rsSqlLast.compareTo(last) <= 0)
                )
                {
                    invalidDate = true;
                    break;
                }
            }

            if (!invalidDate)
            {
                query = "INSERT INTO Reservation (firm_id, car_id, user_id, first, last, price) VALUES (?,?,?,?,?,?)";

                pr = connection.prepareStatement(query);
                pr.setInt(1, firm_id);
                pr.setInt(2, car_id);
                pr.setInt(3, user_id);
                pr.setDate(4, sqlFirst);
                pr.setDate(5, sqlLast);
                pr.setInt(6, price);
                isSuccess = pr.executeUpdate() != 0;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
                if (pr != null) pr.close();
                if (rs != null) rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return isSuccess;
    }

    public static boolean delete(int reservation_id)
    {
        String query = "DELETE FROM Reservation WHERE id = ?";

        PreparedStatement pr = null;
        boolean isSuccess = false;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1, reservation_id);
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

    public static ArrayList<Reservation> getList(int user_id)
    {
        String query = "SELECT * FROM Reservation JOIN Car ON Reservation.car_id = Car.id JOIN Firm ON Firm.id = Car.Firm_id WHERE Reservation.user_id = ?";

        PreparedStatement pr = null;
        ResultSet rs = null;

        ArrayList<Reservation> reservations = new ArrayList<>();
        Reservation reservation = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1, user_id);
            rs = pr.executeQuery();
            while (rs.next())
            {
                reservation = new Reservation(Helper.dateToCalendar(rs.getDate("first")), Helper.dateToCalendar(rs.getDate("last")), rs.getInt("price"), rs.getInt("id"), rs.getInt("firm_id"), rs.getInt("car_id"), rs.getString("model"), rs.getString("brand"), rs.getString("type"));
                reservations.add(reservation);
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

        return reservations;
    }

    public static ArrayList<Reservation> getList(int car_id, int empty)
    {
        String query = "SELECT * FROM Reservation WHERE car_id = ?";

        PreparedStatement pr = null;
        ResultSet rs = null;

        ArrayList<Reservation> reservations = new ArrayList<>();
        Reservation reservation = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1, car_id);
            rs = pr.executeQuery();
            while (rs.next())
            {
                reservation = new Reservation(Helper.dateToCalendar(rs.getDate("first")), Helper.dateToCalendar(rs.getDate("last")), rs.getInt("price"), rs.getInt("id"));
                reservations.add(reservation);
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

        return reservations;
    }
}
