package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import Helper.Helper;
import Helper.DBConnector;

public class Price extends DateInterval {


    private int firm_id;
    private int car_id;
    private String model;
    private String brand;
    private String type;

    public Price(Calendar start, Calendar end, int price, int id) {
        super(start, end, price, id);
    }

    public Price(Calendar start, Calendar end, int price, int id, int firm_id, int car_id, String model, String brand, String type) {
        super(start, end, price, id);
        this.model = model;
        this.brand = brand;
        this.type = type;
        this.firm_id = firm_id;
        this.car_id = car_id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public int getFirm_id() {
        return firm_id;
    }

    public void setFirm_id(int firm_id) {
        this.firm_id = firm_id;
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

    public static boolean add(int firm_id, int car_id, Date first, Date last, int price)
    {
        String query = "SELECT first,last FROM Price WHERE car_id = ?";

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
                query = "INSERT INTO Price (firm_id, car_id, first, last, price) VALUES (?,?,?,?,?)";

                pr = connection.prepareStatement(query);
                pr.setInt(1, firm_id);
                pr.setInt(2, car_id);
                pr.setDate(3, sqlFirst);
                pr.setDate(4, sqlLast);
                pr.setInt(5, price);
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


    public static boolean delete(int price_id)
    {
        String query = "DELETE FROM Price WHERE id = ?";

        PreparedStatement pr = null;
        boolean isSuccess = false;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1, price_id);
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

    public static ArrayList<Price> getList(int car_id)
    {
        String query = "SELECT * FROM Price WHERE car_id = ?";

        PreparedStatement pr = null;
        ResultSet rs = null;

        ArrayList<Price> reservations = new ArrayList<>();
        Price reservation = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1, car_id);
            rs = pr.executeQuery();
            while (rs.next())
            {
                reservation = new Price(Helper.dateToCalendar(rs.getDate("first")), Helper.dateToCalendar(rs.getDate("last")), rs.getInt("price"), rs.getInt("id"));
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

    public static ArrayList<Price> getList()
    {
        String query = "SELECT * FROM Price";

        PreparedStatement pr = null;
        ResultSet rs = null;

        ArrayList<Price> prices = new ArrayList<>();
        Price price = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            rs = pr.executeQuery();
            while (rs.next())
            {
                price = new Price(Helper.dateToCalendar(rs.getDate("first")), Helper.dateToCalendar(rs.getDate("last")), rs.getInt("price"), rs.getInt("id"));
                prices.add(price);
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

        return prices;
    }

    public static ArrayList<Price> search(Date first, Date last, int city, String type)
    {
        String query = "SELECT * FROM Price JOIN Car ON Price.car_id = Car.id JOIN Firm ON Firm.id = Car.Firm_id WHERE Firm.city = ? AND Car.type ILIKE ?";

        java.sql.Date sqlFirst = new java.sql.Date(first.getTime());
        java.sql.Date sqlLast = new java.sql.Date(last.getTime());



        PreparedStatement pr = null;
        ResultSet rs = null;



        ArrayList<Price> prices = new ArrayList<>();
        Price price = null;

        try {
            pr = DBConnector.getConnection().prepareStatement(query);
            pr.setInt(1, city);
            pr.setString(2, "%" + type + "%");
            rs = pr.executeQuery();
            while (rs.next())
            {
                boolean invalidDate = false;
                ArrayList<Reservation> allReservations = Reservation.getList(rs.getInt("car_id"), 0);

                Date rsSqlFirst = rs.getDate("first");
                Date rsSqlLast = rs.getDate("last");

                if
                (
                        (rsSqlFirst.compareTo(first) <= 0 && rsSqlLast.compareTo(first) >= 0)
                                &&
                        (rsSqlFirst.compareTo(last) <= 0 && rsSqlLast.compareTo(last) >= 0)
                )
                {

                    for (Reservation reservation : allReservations)
                    {
                        if
                        (
                                (reservation.getStart().compareTo(first) <= 0 && reservation.getEnd().compareTo(first) >= 0)
                                        ||
                                (reservation.getStart().compareTo(last) <= 0 && reservation.getEnd().compareTo(last) >= 0)
                                        ||
                                (reservation.getStart().compareTo(first) >= 0 && reservation.getEnd().compareTo(last) <= 0)
                        )
                        {
                            invalidDate = true;
                            break;
                        }
                    }
                    if (!invalidDate)
                    {
                        price = new Price(Helper.dateToCalendar(rs.getDate("first")), Helper.dateToCalendar(rs.getDate("last")), rs.getInt("price"), rs.getInt("id"), rs.getInt("firm_id"), rs.getInt("car_id"), rs.getString("model"), rs.getString("brand"), rs.getString("type"));
                        prices.add(price);
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

        return prices;
    }

}
