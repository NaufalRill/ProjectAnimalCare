package org.pbo.kasir;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class menu_registrasi extends JFrame {
    private final koneksi k = new koneksi();
    private DefaultTableModel model = null;
    private PreparedStatement stat;
    private JButton btn_logout;
    private JTextField text_id_user;
    private JComboBox<String> combo_id_level;
    private JButton btn_input;
    private JTable table_registrasi;
    private JTextField text_username;
    private JTextField text_password;
    private JTextField text_nama_user;
    private JButton btn_update;
    private JButton btn_delete;
    private JButton btn_menu_minuman;
    private JPanel menuRegistrasi;

    public menu_registrasi() {
        k.connect();
        User user = new User();
        user.refreshTable();
        setContentPane(menuRegistrasi);
        setTitle("Menu Registrasi");
        setLocationRelativeTo(null);
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        btn_logout.addActionListener(e -> {
            Login login = new Login();
            login.setVisible(true);
            menu_registrasi.this.setVisible(false);
        });
        btn_input.addActionListener(a -> {
            try {
                User user1 = new User();
                stat = k.getCon().prepareStatement("INSERT INTO user VALUES(?,?,?,?,?)");
                stat.setInt(1, 0);
                stat.setString(2, user1.getUsername());
                stat.setString(3, user1.getPassword());
                stat.setString(4, user1.getNamaUser());
                stat.setInt(5, user1.getIdLevel());
                stat.executeUpdate();
                user1.refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });
        table_registrasi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                text_id_user.setText(model.getValueAt(table_registrasi.getSelectedRow(), 0).toString());
                text_username.setText(model.getValueAt(table_registrasi.getSelectedRow(), 1).toString());
                text_password.setText(model.getValueAt(table_registrasi.getSelectedRow(), 2).toString());
                text_nama_user.setText(model.getValueAt(table_registrasi.getSelectedRow(), 3).toString());
            }
        });
        btn_update.addActionListener(a -> {
            try {
                User u = new User();
                stat = k.getCon().prepareStatement("update user set username=?,"
                        + "password=?,nama_user=?,id_level=? where id_user=?");
                stat.setString(1, u.getUsername());
                stat.setString(2, u.getPassword());
                stat.setString(3, u.getNamaUser());
                stat.setInt(4, u.getIdLevel());
                stat.setString(5, text_id_user.getText());
                stat.executeUpdate();
                u.refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });
        btn_delete.addActionListener(a -> {
            try {
                User u = new User();
                stat = k.getCon().prepareStatement("delete from user where id_user=?");
                stat.setString(1, text_id_user.getText());
                stat.executeUpdate();
                u.refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });
        btn_menu_minuman.addActionListener(e -> {
            menu_minuman minuman = new menu_minuman();
            minuman.setVisible(true);
            menu_registrasi.this.setVisible(false);
            minuman.setBtn_transaksi(true);
            minuman.setCombo_status_minuman(true);
            minuman.setBtn_input(true);
            minuman.setBtn_update(true);
            minuman.setBtn_delete(true);
            minuman.setBtn_registrasi(true);
        });
    }

    class User {
        private final int id_level;
        private final String username;
        private final String password;
        private final String nama_user;

        public User() {
            username = text_username.getText();
            password = text_password.getText();
            nama_user = text_nama_user.getText();
            id_level = Integer.parseInt(combo_id_level.getSelectedItem().toString());
        }

        public void refreshTable() {
            model = new DefaultTableModel();
            model.addColumn("ID User");
            model.addColumn("Username");
            model.addColumn("Password");
            model.addColumn("Nama User");
            model.addColumn("ID Level");

            text_id_user.setText("");
            text_username.setText("");
            text_password.setText("");
            text_nama_user.setText("");
        }

        public int getIdLevel() {
            return id_level;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getNamaUser() {
            return nama_user;
        }

    }
}
