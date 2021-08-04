package View;

import Helper.Helper;
import Model.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddServiceGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_service_name;
    private JTextArea fld_service_description;
    private JButton btn_save;
    private JTextField fld_cost;

    public AddServiceGUI(int firm_id)
    {
        Helper.setPage(this, wrapper, 400, 400);


        btn_save.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_service_description) || Helper.isFieldEmpty(fld_service_name))
                Helper.fill();
            else if (!Helper.isFieldInt(fld_cost))
                Helper.showMessage("Ücret olarak sayı giriniz.", "Hatalı giriş.");
            else
            {
                if (!Service.add(firm_id, fld_service_name.getText().trim(), fld_service_description.getText().trim(), Integer.parseInt(fld_cost.getText().trim())))
                    Helper.fail();
                else
                    dispose();
            }
        });
    }
}
