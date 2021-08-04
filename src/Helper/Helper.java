package Helper;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Helper {

    public static final String[] cities = {"Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin", "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta", "İçel (Mersin)", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"};

    public static void setPage(JFrame jFrame, JPanel wrapper, int width, int height){
        jFrame.add(wrapper);
        jFrame.setSize(width, height);
        jFrame.setLocation(Helper.centerScreen("x", jFrame.getSize()), Helper.centerScreen("y", jFrame.getSize()));
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setTitle(Config.PROJECT_TITLE);
        jFrame.setVisible(true);
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static String formatDate(Date date)
    {
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static void createTables()
    {
        String carTable = "CREATE TABLE IF NOT EXISTS Car (id SERIAL NOT NULL, firm_id INTEGER NOT NULL, type VARCHAR(55) NOT NULL, model VARCHAR(55) NOT NULL, brand VARCHAR(55) NOT NULL, year INTEGER NOT NULL, PRIMARY KEY(id), FOREIGN KEY(firm_id) REFERENCES Firm(id))";
        String firmTable = "CREATE TABLE IF NOT EXISTS Firm (id SERIAL NOT NULL, user_id INTEGER NOT NULL, name VARCHAR(55) NOT NULL, city INTEGER NOT NULL, PRIMARY KEY(id), FOREIGN KEY(user_id) REFERENCES Users(id))";
        String serviceTable = "CREATE TABLE IF NOT EXISTS Service (id SERIAL NOT NULL, firm_id INTEGER NOT NULL, name VARCHAR(55) NOT NULL, description VARCHAR(512) NOT NULL, cost INTEGER NOT NULL, PRIMARY KEY(id), FOREIGN KEY(firm_id) REFERENCES Firm(id))";
        String priceTable = "CREATE TABLE IF NOT EXISTS Price (id SERIAL NOT NULL, firm_id INTEGER NOT NULL, car_id INTEGER NOT NULL, first DATE NOT NULL, last DATE NOT NULL, price INTEGER NOT NULL, PRIMARY KEY(id), FOREIGN KEY(firm_id) REFERENCES Firm(id), FOREIGN KEY(car_id) REFERENCES Car(id))";
        String reservationTable = "CREATE TABLE IF NOT EXISTS Reservation (id SERIAL NOT NULL, firm_id INTEGER NOT NULL, car_id INTEGER NOT NULL, user_id INTEGER NOT NULL, first DATE NOT NULL, last DATE NOT NULL, price INTEGER NOT NULL, PRIMARY KEY(id), FOREIGN KEY(firm_id) REFERENCES Firm(id), FOREIGN KEY(car_id) REFERENCES Car(id), FOREIGN KEY(user_id) REFERENCES Users(id))";
        String userTable = "CREATE TABLE IF NOT EXISTS Users (id SERIAL NOT NULL, name VARCHAR(55) NOT NULL, username VARCHAR(55) NOT NULL UNIQUE, password VARCHAR(55) NOT NULL, type VARCHAR(55) NOT NULL, PRIMARY KEY(id))";

        Statement st = null;

        try {
            st = DBConnector.getConnection().createStatement();
            st.executeUpdate(userTable + ";" + firmTable + ";" + carTable + ";" + serviceTable + ";" + priceTable + ";" + reservationTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public static int centerScreen(String axis, Dimension size)
    {
        int returnValue = 0;
        switch (axis)
        {
            case "x" -> {
                returnValue = (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
            }
            case "y" -> {
                returnValue = (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
            }
        }
        return returnValue;
    }

    public static <T extends JTextComponent> boolean isFieldEmpty (T field)
    {
        return field.getText().trim().isEmpty();
    }

    public static <T extends JTextComponent> boolean isFieldInt (T field)
    {
        try
        {
            Integer.parseInt(field.getText().trim());
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static void showMessage(String message, String title)
    {
        optionPageTR();
        JOptionPane.showMessageDialog(null,message,title,JOptionPane.INFORMATION_MESSAGE);
    }

    public static void fill()
    {
        optionPageTR();
        JOptionPane.showMessageDialog(null,"Zorunlu alanları doldurmadınız.","İşlem tamamlanmadı!",JOptionPane.INFORMATION_MESSAGE);
    }
    public static void fail()
    {
        optionPageTR();
        JOptionPane.showMessageDialog(null,"İşlem başarısız.","İşlem tamamlanmadı!",JOptionPane.INFORMATION_MESSAGE);
    }

    public static void optionPageTR()
    {
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");
        UIManager.put("OptionPane.okButtonText", "Tamam");
    }

    public static Date getDateWithoutTimeUsingCalendar(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date stringToDate(String string)
    {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
