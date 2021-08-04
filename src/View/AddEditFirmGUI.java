package View;

import Helper.Helper;
import Helper.Config;
import Model.Firm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEditFirmGUI extends JFrame {

    private JPanel wrapper;
    private JLabel lbl_firm;
    private JTextField fld_firm_name;
    private JComboBox<String> cmb_city;
    private JButton btn_save;

    // add
    public AddEditFirmGUI(int user_id)
    {
        Helper.setPage(this, wrapper, 400, 400);

        lbl_firm.setText("Yeni Firma Ekle");

        for (String city : Helper.cities)
        {
            cmb_city.addItem(city);
        }


        btn_save.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_firm_name))
            {
                Helper.fill();
            }
            else
            {
                if (!Firm.add(cmb_city.getSelectedIndex(), user_id, fld_firm_name.getText().trim())) Helper.fail();
                else dispose();
            }
        });
    }

    // edit
    public AddEditFirmGUI(Firm firm)
    {
        Helper.setPage(this, wrapper, 400, 400);

        int firm_id = firm.getId();
        lbl_firm.setText(firm.getName());
        fld_firm_name.setText(firm.getName());
        System.out.println(firm.getCity());


        for (String city : Helper.cities)
        {
            cmb_city.addItem(city);
        }

        cmb_city.setSelectedItem(firm.getCity());

        btn_save.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_firm_name))
            {
                Helper.fill();
            }
            else
            {
                if (!Firm.update(cmb_city.getSelectedIndex(), firm_id, fld_firm_name.getText().trim())) Helper.fail();
                else dispose();
            }
        });



    }


}
