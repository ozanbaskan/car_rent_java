package View;

import Helper.Helper;
import Helper.Config;
import Model.Car;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class AddEditCarGUI extends JFrame {
    private JPanel wrapper;
    private JLabel lbl_car;
    private JTextField fld_brand;
    private JTextField fld_model;
    private JTextField fld_type;
    private JButton btn_save;
    private JComboBox<Integer> cmb_year;


    // add
    public AddEditCarGUI(int firm_id)
    {
        Helper.setPage(this, wrapper, 400, 400);

        int year = Integer.parseInt(new Date().toInstant().toString().substring(0,4));

        for (int i = year; i >= 1908; i--)
        {
            cmb_year.addItem(i);
        }


        btn_save.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_brand) || Helper.isFieldEmpty(fld_model) || Helper.isFieldEmpty(fld_type))
                Helper.fill();
            else
            {
                boolean isSuccess = Car.add(fld_type.getText().trim(),fld_model.getText().trim(),fld_brand.getText().trim(),(int) cmb_year.getSelectedItem(), firm_id);
                if (isSuccess) dispose();
                else Helper.fail();
            }
        });
    }

    // edit
    public AddEditCarGUI(Car car)
    {
        Helper.setPage(this, wrapper, 400, 400);

        int year = Integer.parseInt(new Date().toInstant().toString().substring(0,4));

        for (int i = year; i >= 1908; i--)
        {
            cmb_year.addItem(i);
        }

        fld_type.setText(car.getType());
        fld_model.setText(car.getModel());
        fld_brand.setText(car.getBrand());
        cmb_year.setSelectedItem(car.getYear());

        btn_save.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_brand) || Helper.isFieldEmpty(fld_model) || Helper.isFieldEmpty(fld_type))
                Helper.fill();
            else
            {
                boolean isSuccess = Car.update(fld_type.getText().trim(),fld_model.getText().trim(),fld_brand.getText().trim(),(int) cmb_year.getSelectedItem(), car.getId());

                if (isSuccess) dispose();
                else Helper.fail();
            }
        });
    }




}
