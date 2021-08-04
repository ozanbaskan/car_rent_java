import Helper.Helper;
import View.AddPriceGUI;
import View.LoginGUI;

public class Main {

    public static void main(String[] args) {

        Helper.createTables();
        //new FirmGUI(new FirmUser("username", "şifre", "name", 1));
        new LoginGUI();

    }
}
