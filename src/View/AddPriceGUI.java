package View;

import javax.swing.*;

import Helper.Helper;
import Model.Car;
import Model.Price;
import com.toedter.calendar.JDateChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class AddPriceGUI extends JFrame {
    private JPanel wrapper;
    private JDateChooser date_chooser_first;
    private JDateChooser date_chooser_second;
    private JTextField fld_price;
    private JButton btn_save;

    public AddPriceGUI(int firm_id, int car_id)
    {
        Helper.setPage(this, wrapper, 400, 400);


        btn_save.addActionListener(e -> {
            if (!Helper.isFieldInt(fld_price))
            {
                Helper.showMessage("Fiyat olarak sayı giriniz.", "Hata!");return;
            }

            Date first = date_chooser_first.getDate();
            Date last = date_chooser_second.getDate();

            if (first == null || last == null)
            {
                Helper.showMessage("Tarih seçiniz", "Hata!");return;
            }

            if (first.after(last))
            {
                Helper.showMessage("Uygunsuz tarihler seçtiniz.", "Hata");return;
            }

            if (!Price.add(firm_id, car_id, first, last, Integer.parseInt(fld_price.getText().trim()))) Helper.fail();
            else dispose();

        });
    }


    void createUIComponents()
    {
        date_chooser_first = new JDateChooser();
        date_chooser_second = new JDateChooser();
    }
}
