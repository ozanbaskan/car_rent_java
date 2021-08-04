package View;

import Helper.Helper;
import Helper.Config;
import Model.FirmUser;
import Model.Renter;
import Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {

    private JPanel wrapper;
    private JTextField fld_username;
    private JButton btn_login;
    private JButton btn_register;
    private JPasswordField fld_password;

    public LoginGUI()
    {
        Helper.setPage(this, wrapper, 400, 300);

        // register button
        btn_register.addActionListener(e -> {
            new RegisterGUI();
        });

        // login button
        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_password) || Helper.isFieldEmpty(fld_username))
            {
                Helper.fill();
            }
            else
            {
                User user = User.get(fld_username.getText().trim(), String.valueOf(fld_password.getPassword()));
                if (user == null) Helper.fail();
                else
                {
                    switch (user.getType())
                    {
                        case "renter" -> {
                            new RenterGUI((Renter) user);
                            dispose();
                        }
                        case "firmUser" -> {
                            new FirmGUI((FirmUser) user);
                            dispose();
                        }
                    }
                }
            }
        });
    }


}
