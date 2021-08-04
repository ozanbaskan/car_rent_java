package View;

import Helper.*;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FirmGUI extends JFrame {

    private JPanel wrapper;

    private JLabel lbl_firm_name;

    private JTable tbl_firms;
    private JScrollPane scrl_firms;
    private JButton btn_firm_add;
    private JTable tbl_cars;
    private JScrollPane scrl_cars;
    private JButton btn_car_add;
    private JButton btn_car_edit;
    private JButton btn_car_delete;
    private JButton btn_firm_edit;
    private JButton btn_firm_delete;
    private JTable tbl_services;
    private JScrollPane scrl_services;
    private JButton btn_add_service;
    private JButton btn_delete_service;
    private JButton btn_exit;
    private JTable tbl_dates;
    private JButton btn_add_date;
    private JButton btn_delete_date;
    private JScrollPane scrl_dates;
    private DefaultTableModel mdl_firms;
    private Object[] row_firms;
    private DefaultTableModel mdl_cars;
    private Object[] row_cars;
    private DefaultTableModel mdl_services;
    private Object[] row_services;
    private DefaultTableModel mdl_dates;
    private Object[] row_dates;


    private FirmUser firmUser;

    public FirmGUI(FirmUser firmUser)
    {
        // Get firm data and set labels
        this.firmUser = firmUser;
        lbl_firm_name.setText(firmUser.getName());
        // ### Get firm data and set labels

        Helper.setPage(this, wrapper, 800, 800);

        // firm table
        mdl_firms = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        Object[] col_firms = {"ID", "Şehir", "İsim"};
        mdl_firms.setColumnIdentifiers(col_firms);
        row_firms = new Object[col_firms.length];
        tbl_firms.setModel(mdl_firms);
        tbl_firms.getTableHeader().setReorderingAllowed(false);
        tbl_firms.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(40);

        updateFirms();
        // ### firm table

        // car table

        mdl_cars = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        Object[] col_cars = {"ID", "Marka", "Model", "Yıl", "Araç Tipi"};
        mdl_cars.setColumnIdentifiers(col_cars);
        row_cars = new Object[col_cars.length];
        tbl_cars.setModel(mdl_cars);
        tbl_cars.getTableHeader().setReorderingAllowed(false);
        tbl_cars.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(40);

        // ### car table

        // service table

        mdl_services = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        Object[] col_services = {"ID", "Servis", "Tanımı", "Ücret"};
        mdl_services.setColumnIdentifiers(col_services);
        row_services = new Object[col_services.length];
        tbl_services.setModel(mdl_services);
        tbl_services.getTableHeader().setReorderingAllowed(false);
        tbl_services.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(30);

        // ### service table

        // date-price table

        mdl_dates = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        Object[] col_dates = {"ID", "Başlangıç", "Bitiş", "Ücret"};
        mdl_dates.setColumnIdentifiers(col_dates);
        row_dates = new Object[col_dates.length];
        tbl_dates.setModel(mdl_dates);
        tbl_dates.getTableHeader().setReorderingAllowed(false);
        tbl_dates.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(30);


        // ### date-price table

        // exit button
        btn_exit.addActionListener(e -> {
            new LoginGUI();
            dispose();
        });

        // add firm
        btn_firm_add.addActionListener(e -> {
            AddEditFirmGUI addFirmGUI = new AddEditFirmGUI(firmUser.getId());
            addFirmGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateFirms();
                }
            });
        });

        // edit firm
        btn_firm_edit.addActionListener(e -> {
            int selected_row = tbl_firms.getSelectedRow();
            if (selected_row == -1) return;

            AddEditFirmGUI editFirmGUI = new AddEditFirmGUI(new Firm((String) tbl_firms.getValueAt(selected_row,1), (String) tbl_firms.getValueAt(selected_row,2), (int) tbl_firms.getValueAt(selected_row,0)));
            editFirmGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateFirms();
                }
            });
        });

        // delete firm
        btn_firm_delete.addActionListener(e -> {
            int selected_row = tbl_firms.getSelectedRow();
            if (selected_row == -1) return;

            if (Firm.delete((Integer) tbl_firms.getValueAt(selected_row,0))) updateFirms();
            else Helper.fail();
        });

        // update cars on firm selection
        tbl_firms.getSelectionModel().addListSelectionListener(e -> {
            int selected_row = tbl_firms.getSelectedRow();
            if (selected_row == -1) return;

            int id = (int) tbl_firms.getValueAt(selected_row,0);

            clearPrices();
            updateServices(id);
            updateCars(id);
        });

        // update prices on car selection

        tbl_cars.getSelectionModel().addListSelectionListener(e -> {
            int selected_row = tbl_cars.getSelectedRow();
            if (selected_row == -1) return;

            int id = (int) tbl_cars.getValueAt(selected_row,0);
            updatePrices(id);
        });

        // add car
        btn_car_add.addActionListener(e -> {
            int firm_row = tbl_firms.getSelectedRow();
            if (firm_row == -1) return;

            int firm_id = (int) tbl_firms.getValueAt(firm_row,0);

            AddEditCarGUI addCarGUI = new AddEditCarGUI(firm_id);
            addCarGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateCars(firm_id);
                }
            });
        });

        // edit car
        btn_car_edit.addActionListener(e -> {
            int firm_row = tbl_firms.getSelectedRow();
            int selected_row = tbl_cars.getSelectedRow();
            if (selected_row == -1 || firm_row == -1) return;

            int firm_id = (int) tbl_firms.getValueAt(firm_row,0);

            AddEditCarGUI editCarGUI = new AddEditCarGUI(new Car((String) tbl_cars.getValueAt(selected_row,4),(String) tbl_cars.getValueAt(selected_row,2),(String) tbl_cars.getValueAt(selected_row,1), (int) tbl_cars.getValueAt(selected_row,3), (int) tbl_cars.getValueAt(selected_row,0)));
            editCarGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateCars(firm_id);
                }
            });
        });

        // delete car
        btn_car_delete.addActionListener(e -> {
            int firm_row = tbl_firms.getSelectedRow();
            int selected_row = tbl_cars.getSelectedRow();
            if (selected_row == -1 || firm_row == -1) return;

            int firm_id = (int) tbl_firms.getValueAt(firm_row,0);

            if (!Car.delete((int) tbl_cars.getValueAt(selected_row,0)))
                Helper.fail();
            else
                updateCars(firm_id);
        });

        // add service
        btn_add_service.addActionListener(e -> {
            int firm_row = tbl_firms.getSelectedRow();
            if (firm_row == -1) return;

            int firm_id = (int) tbl_firms.getValueAt(firm_row,0);

            AddServiceGUI addServiceGUI = new AddServiceGUI(firm_id);
            addServiceGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateServices(firm_id);
                }
            });

        });

        // delete service
        btn_delete_service.addActionListener(e -> {
            int firm_row = tbl_firms.getSelectedRow();
            int service_row = tbl_services.getSelectedRow();
            if (service_row == -1 || firm_row == -1) return;

            int firm_id = (int) tbl_firms.getValueAt(firm_row,0);
            int service_id = (int) tbl_services.getValueAt(service_row,0);

            if (Service.delete(service_id))
                updateServices(firm_id);
            else
                Helper.fail();
        });

        // add price interval date
        btn_add_date.addActionListener(e -> {
            int firm_row = tbl_firms.getSelectedRow();
            int car_row = tbl_cars.getSelectedRow();
            if (car_row == -1 || firm_row == -1) return;

            int firm_id = (int) tbl_firms.getValueAt(firm_row, 0);
            int car_id = (int) tbl_cars.getValueAt(car_row, 0);

            AddPriceGUI addPriceGUI = new AddPriceGUI(firm_id, car_id);
            addPriceGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updatePrices(car_id);
                }
            });

        });

        // delete price
        btn_delete_date.addActionListener(e -> {
            int price_row = tbl_dates.getSelectedRow();
            int car_row = tbl_cars.getSelectedRow();
            if (car_row == -1 || price_row == -1) return;

            int price_id = (int) tbl_dates.getValueAt(price_row, 0);
            int car_id = (int) tbl_cars.getValueAt(car_row, 0);

            if (!Price.delete(price_id))
                Helper.fail();
            else
                updatePrices(car_id);
        });
    }

    public void updateFirms()
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_firms.getModel();
        clearModel.setRowCount(0);

        for (Firm firm : Firm.getUserList(firmUser.getId()))
        {
            int i = 0;
            row_firms[i++] = firm.getId();
            row_firms[i++] = firm.getCity();
            row_firms[i++] = firm.getName();
            mdl_firms.addRow(row_firms);
        }
    }

    public void updateCars(int firm_id)
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_cars.getModel();
        clearModel.setRowCount(0);

        for (Car car : Car.getList(firm_id))
        {
            int i = 0;
            row_cars[i++] = car.getId();
            row_cars[i++] = car.getBrand();
            row_cars[i++] = car.getModel();
            row_cars[i++] = car.getYear();
            row_cars[i++] = car.getType();
            mdl_cars.addRow(row_cars);
        }

    }

    public void updateServices(int firm_id)
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_services.getModel();
        clearModel.setRowCount(0);

        for (Service service : Service.getList(firm_id))
        {
            int i = 0;
            row_services[i++] = service.getId();
            row_services[i++] = service.getName();
            row_services[i++] = service.getDescription();
            row_services[i++] = service.getCost();
            mdl_services.addRow(row_services);
        }

    }

    public void updatePrices(int car_id)
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_dates.getModel();
        clearModel.setRowCount(0);

        for (Price price : Price.getList(car_id))
        {
            int i = 0;
            row_dates[i++] = price.getId();
            row_dates[i++] = Helper.formatDate(price.getStart());
            row_dates[i++] = Helper.formatDate(price.getEnd());
            row_dates[i++] = price.getPrice();
            mdl_dates.addRow(row_dates);
        }
    }

    public void clearPrices()
    {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_dates.getModel();
        clearModel.setRowCount(0);
    }
}
