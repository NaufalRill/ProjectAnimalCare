package org.pbo.kasir;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class menu_minuman extends JFrame {
    koneksi k = new koneksi();
    private JButton btn_transaksi;
    private JButton btn_logout;
    private JTextField text_id_minuman;
    private JTextField text_nama_minuman;
    private JTextField text_harga;
    private JComboBox combo_status_minuman;
    private JButton btn_input;
    private JButton btn_registrasi;
    private JButton btn_update;
    private JButton btn_delete;
    private JTable table_minuman;
    private JPanel MenuMinuman;
    private DefaultTableModel model = null;
    private PreparedStatement stat;

    public menu_minuman() {
        setContentPane(MenuMinuman);
        setTitle("Menu Minuman");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        btn_input.addActionListener(a -> {
            try {
                Minuman minuman = new Minuman();
                menu_minuman.this.stat = k.getCon().prepareStatement("insert into minuman values(?,?,?,?)");
                stat.setInt(1, 0);
                stat.setString(2, minuman.nama_minuman);
                stat.setInt(3, minuman.harga);
                stat.setString(4, minuman.status);
                stat.executeUpdate();
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });
        table_minuman.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                text_id_minuman.setText(model.getValueAt(table_minuman.getSelectedRow(), 0).toString());
                text_nama_minuman.setText(model.getValueAt(table_minuman.getSelectedRow(), 1).toString());
                text_harga.setText(model.getValueAt(table_minuman.getSelectedRow(), 2).toString());

            }
        });
        btn_update.addActionListener(b -> {
            try {
                Minuman minuman = new Minuman();
                menu_minuman.this.stat = k.getCon().prepareStatement("update minuman set nama_minuman=?, "
                        + "harga=?, status=? where id_minuman=?");
                stat.setString(1, minuman.nama_minuman);
                stat.setInt(2, minuman.harga);
                stat.setString(3, minuman.status);
                stat.setInt(4, minuman.id_minuman);
                stat.setInt(4, Integer.parseInt(text_id_minuman.getText()));
                stat.executeUpdate();
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });
        btn_delete.addActionListener(c -> {
            try {
                menu_minuman.this.stat = k.getCon().prepareStatement("delete from minuman where id_minuman=?");
                stat.setInt(1, Integer.parseInt(text_id_minuman.getText()));
                stat.executeUpdate();
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        k.connect();
        refreshTable();
        btn_registrasi.addActionListener(d -> {
            menu_registrasi reg = new menu_registrasi();
            reg.setVisible(true);
            menu_minuman.this.setVisible(false);
        });
        btn_transaksi.addActionListener(f -> {
            menu_transaksi tran = new menu_transaksi();
            tran.setVisible(true);
            menu_minuman.this.setVisible(false);
        });
        btn_logout.addActionListener(g -> {
            Login login = new Login();
            login.setVisible(true);
            menu_minuman.this.setVisible(false);
        });
    }

    public void setBtn_logout(boolean enabled) {
        btn_logout.setEnabled(enabled);
    }

    public void setBtn_transaksi(boolean enabled) {
        btn_transaksi.setEnabled(enabled);
    }

    public void setBtn_input(boolean enabled) {
        btn_input.setEnabled(enabled);
    }

    public void setBtn_registrasi(boolean enabled) {
        btn_registrasi.setEnabled(enabled);
    }

    public void setBtn_update(boolean enabled) {
        btn_update.setEnabled(enabled);
    }

    public void setBtn_delete(boolean enabled) {
        btn_delete.setEnabled(enabled);
    }

    public void setCombo_status_minuman(boolean enabled) {
        combo_status_minuman.setEnabled(enabled);
    }

    public void refreshTable() {
        model = new DefaultTableModel();
        model.addColumn("ID Minuman");
        model.addColumn("Nama Minuman");
        model.addColumn("Harga");
        model.addColumn("Status Minuman");
        table_minuman.setModel(model);
        try {
            this.stat = k.getCon().prepareStatement("select * from minuman");
            ResultSet rs = this.stat.executeQuery();
            while (rs.next()) {
                Object[] data = {
                        rs.getInt("id_minuman"),
                        rs.getString("nama_minuman"),
                        rs.getInt("harga"),
                        rs.getString("status")
                };
                model.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
        text_id_minuman.setText("");
        text_nama_minuman.setText("");
        text_harga.setText("");

    }

    class Minuman extends menu_minuman {
        int id_minuman, harga;
        String nama_minuman, status;

        public Minuman() {
            this.nama_minuman = text_nama_minuman.getText();
            this.harga = Integer.parseInt(text_harga.getText());
            this.status = combo_status_minuman.getSelectedItem().toString();

        }
    }

}
