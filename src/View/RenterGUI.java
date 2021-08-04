package View;

import Helper.Helper;
import Model.Price;
import Model.Renter;
import Model.Reservation;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class RenterGUI extends JFrame {

    private JPanel wrapper;
    private JLabel lbl_name;
    private JPanel pnl_search;
    private JPanel pnl_reservations;
    private JTable tbl_search;
    private JScrollPane scrl_search;
    private JTable tbl_reservations;
    private JScrollPane scrl_reservations;
    private JComboBox cmb_cities;
    private JTextField fld_type;
    private JDateChooser date_chooser_first;
    private JDateChooser date_chooser_second;
    private JButton btn_search;
    private JButton btn_rent;
    private JLabel lbl_day;
    private JButton btn_exit;
    private JButton btn_cancel;
    private DefaultTableModel mdl_prices;
    private Object[] row_prices;
    private DefaultTableModel mdl_reservations;
    private Object[] row_reservations;

    public RenterGUI(Renter renter)
    {

        Helper.setPage(this, wrapper, 1200, 400);

        lbl_name.setText(renter.getName());

        for (String city : Helper.cities)
        {
            cmb_cities.addItem(city);
        }

        // price table
        mdl_prices = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        Object[] col_prices = {"ID", "Firma ID", "Marka", "Model", "Yıl", "Başlangıç", "Bitiş", "Ücret"};
        mdl_prices.setColumnIdentifiers(col_prices);
        row_prices = new Object[col_prices.length];
        tbl_search.setModel(mdl_prices);
        tbl_search.getTableHeader().setReorderingAllowed(false);
        tbl_search.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(30);

        // reservation table

        mdl_reservations = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        Object[] col_reservations = {"ID", "Araba ID", "Firma ID", "Marka", "Model", "Yıl", "Başlangıç", "Bitiş", "Ücret"};
        mdl_reservations.setColumnIdentifiers(col_reservations);
        row_reservations = new Object[col_reservations.length];
        tbl_reservations.setModel(mdl_reservations);
        tbl_reservations.getTableHeader().setReorderingAllowed(false);


        updateReservations(renter.getId());

        btn_search.addActionListener(e -> {

            Date first = date_chooser_first.getDate();
            Date last = date_chooser_second.getDate();

            if (first == null || last == null)
            {
                Helper.showMessage("Tarih seçiniz", "Hata!");return;
            }

            first = Helper.getDateWithoutTimeUsingCalendar(Helper.dateToCalendar(first));
            last = Helper.getDateWithoutTimeUsingCalendar(Helper.dateToCalendar(last));


            if (first.after(last))
            {
                Helper.showMessage("Uygunsuz tarihler seçtiniz.", "Hata");return;
            }

            int days = (int) (Helper.getDifferenceDays(first,last) + 1);

            lbl_day.setText("Gün Sayısı: " + days);

            updateDates(first, last, cmb_cities.getSelectedIndex(), fld_type.getText().trim(), days);
        });

        // rent a car
        btn_rent.addActionListener(e -> {
            int selected_row = tbl_search.getSelectedRow();
            if (selected_row == -1) return;

            Date first = date_chooser_first.getDate();
            Date last = date_chooser_second.getDate();

            if (first == null || last == null)
            {
                Helper.showMessage("Tarih seçiniz", "Hata!");return;
            }

            first = Helper.getDateWithoutTimeUsingCalendar(Helper.dateToCalendar(first));
            last = Helper.getDateWithoutTimeUsingCalendar(Helper.dateToCalendar(last));


            if (first.after(last))
            {
                Helper.showMessage("Uygunsuz tarihler seçtiniz.", "Hata");return;
            }


            int firm_id = (int) tbl_search.getValueAt(selected_row,1);
            int car_id = (int) tbl_search.getValueAt(selected_row,0);
            int price = (int) tbl_search.getValueAt(selected_row,7);

            int days = (int) (Helper.getDifferenceDays(first,last) + 1);

            if (Reservation.add(firm_id, car_id, renter.getId(), first, last, price)) {
                mdl_prices.removeRow(selected_row);
                updateReservations(renter.getId());
            }
            else
                Helper.fail();
        });

        btn_exit.addActionListener(e -> {
            new LoginGUI();
            dispose();
        });

        btn_cancel.addActionListener(e -> {
            int selected_row = tbl_reservations.getSelectedRow();
            if (selected_row == -1) return;

            String stringDate = (String) tbl_reservations.getValueAt(selected_row,6);
            int reservation_id = (int) tbl_reservations.getValueAt(selected_row,0);

            Date date = Helper.stringToDate(stringDate);
            date = Helper.getDateWithoutTimeUsingCalendar(Helper.dateToCalendar(date));

            if (Helper.getDifferenceDays(new Date(),date) <= 0)
                Helper.showMessage("En erken 24 saat önceden iptal edebilirsiniz.", "İptal işlemi gerçekleşmedi.");
            else
            {
                if (Reservation.delete(reservation_id))
                    mdl_reservations.removeRow(selected_row);
                else
                    Helper.fail();
            }
        });
    }



    public void updateReservations(int user_id)
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_reservations.getModel();
        clearModel.setRowCount(0);

        for (Reservation reservation : Reservation.getList(user_id))
        {
            int i = 0;
            row_reservations[i++] = reservation.getId();
            row_reservations[i++] = reservation.getCar_id();
            row_reservations[i++] = reservation.getFirm_id();
            row_reservations[i++] = reservation.getBrand();
            row_reservations[i++] = reservation.getModel();
            row_reservations[i++] = reservation.getType();
            row_reservations[i++] = Helper.formatDate(reservation.getStart());
            row_reservations[i++] = Helper.formatDate(reservation.getEnd());
            row_reservations[i++] = reservation.getPrice();
            mdl_reservations.addRow(row_reservations);
        }

    }

    public void updateDates(Date first, Date last, int city, String type, int days)
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_search.getModel();
        clearModel.setRowCount(0);

        for (Price price : Price.search(first, last, city, type))
        {
            int i = 0;
            row_prices[i++] = price.getCar_id();
            row_prices[i++] = price.getFirm_id();
            row_prices[i++] = price.getBrand();
            row_prices[i++] = price.getModel();
            row_prices[i++] = price.getType();
            row_prices[i++] = Helper.formatDate(price.getStart());
            row_prices[i++] = Helper.formatDate(price.getEnd());
            row_prices[i++] = price.getPrice() * days;
            mdl_prices.addRow(row_prices);
        }
    }

    void createUIComponents()
    {
        date_chooser_first = new JDateChooser();
        date_chooser_second = new JDateChooser();
    }

}
