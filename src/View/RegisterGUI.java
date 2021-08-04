package View;

import javax.swing.*;
import Helper.Helper;
import Helper.Config;
import Model.Renter;
import Model.FirmUser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterGUI extends JFrame {

    private JPanel wrapper;
    private JTextField fld_username;
    private JTextField fld_name;
    private JPasswordField fld_password;
    private JComboBox cmb_type;
    private JButton btn_save;
    private JLabel lbl_username;
    private JLabel lbl_name;
    private JLabel lbl_password;

    public RegisterGUI()
    {
        Helper.setPage(this, wrapper, 400, 400);


        cmb_type.addActionListener(e -> {
            if (cmb_type.getSelectedIndex() == 0)
                lbl_name.setText("İsim, Soyisim");
            else
                lbl_name.setText("Firma Adı");
        });


        btn_save.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_username) || Helper.isFieldEmpty(fld_name) || Helper.isFieldEmpty(fld_password))
                Helper.fill();
            else
            {
                boolean isSuccess = false;
                if (cmb_type.getSelectedIndex() == 0) isSuccess = Renter.add(fld_username.getText().trim(), String.valueOf(fld_password.getPassword()), fld_name.getText().trim(), String.valueOf(cmb_type.getSelectedItem()));
                else if (cmb_type.getSelectedIndex() == 1) isSuccess = FirmUser.add(fld_username.getText().trim(), String.valueOf(fld_password.getPassword()), fld_name.getText().trim(), String.valueOf(cmb_type.getSelectedItem()));

                if (!isSuccess)
                    Helper.fail();
                else
                    dispose();

            }
        });
    }

}
