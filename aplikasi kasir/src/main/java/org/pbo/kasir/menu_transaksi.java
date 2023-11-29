package org.pbo.kasir;

import com.lowagie.text.DocumentException;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class menu_transaksi extends JFrame {
    koneksi k = new koneksi();
    private JButton btn_logout;
    private JTextField text_id_transaksi;
    private JTextField text_nama_pelanggan;
    private JComboBox<String> combo_id_minuman;
    private JButton btn_menu_minuman;
    private JTextField text_jml_beli;
    private JTextField text_total_bayar;
    private JButton btn_input;
    private JButton btn_update;
    private JButton btn_delete;
    private JButton btn_cetak_laporan;
    private JDateChooser text_tanggal;
    private JTable table_transaksi;
    private JPanel menuTransaksi;
    private DefaultTableModel model = null;
    private PreparedStatement stat;
    private ResultSet rs;

    public menu_transaksi() {
        setContentPane(menuTransaksi);
        setTitle("Menu Transaksi");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        k.connect();
        refreshTable();
        refreshCombo();

        btn_input.addActionListener(a -> {
            try {
                transaksi tran = new transaksi();
                text_total_bayar.setText(String.valueOf(tran.total_bayar));
                menu_transaksi.this.stat = k.getCon().prepareStatement("insert into transaksi (nama_pelanggan, id_minuman, tanggal, nama_minuman, harga, jumlah_beli, total_bayar) values (?,?,?,?,?,?,?)");
                menu_transaksi.this.stat.setString(1, tran.nama_pelanggan);
                menu_transaksi.this.stat.setInt(2, tran.id_minuman);
                menu_transaksi.this.stat.setString(3, tran.tanggal);
                menu_transaksi.this.stat.setString(4, tran.nama_minuman);
                menu_transaksi.this.stat.setInt(5, tran.harga);
                menu_transaksi.this.stat.setInt(6, tran.jumlah_beli);
                menu_transaksi.this.stat.setInt(7, tran.total_bayar);
                int pilihan = JOptionPane.showConfirmDialog(null,
                        "Tanggal: " + tran.tanggal +
                                "\nNama Pelanggan: " + tran.nama_pelanggan +
                                "\nPembelian: " + tran.jumlah_beli + " " + tran.nama_minuman +
                                "\nTotal Bayar: " + tran.total_bayar + "\n",
                        "Tambahkan Transaksi?",
                        JOptionPane.YES_NO_OPTION);
                if (pilihan == JOptionPane.YES_OPTION) {
                    menu_transaksi.this.stat.executeUpdate();
                    refreshTable();
                } else if (pilihan == JOptionPane.NO_OPTION) {
                    refreshTable();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        btn_update.addActionListener(a -> {
            try {
                transaksi tran = new transaksi();
                tran.id_transaksi = Integer.parseInt(text_id_transaksi.getText());
                menu_transaksi.this.stat = k.getCon().prepareStatement("update transaksi set nama_pelanggan=?, id_minuman=?, tanggal=?, nama_minuman=?, harga=?, jumlah_beli=?, total_bayar=? where id_transaksi=?");
                menu_transaksi.this.stat.setString(1, tran.nama_pelanggan);
                menu_transaksi.this.stat.setInt(2, tran.id_minuman);
                menu_transaksi.this.stat.setString(3, tran.tanggal);
                menu_transaksi.this.stat.setString(4, tran.nama_minuman);
                menu_transaksi.this.stat.setInt(5, tran.harga);
                menu_transaksi.this.stat.setInt(6, tran.jumlah_beli);
                menu_transaksi.this.stat.setInt(7, tran.total_bayar);
                menu_transaksi.this.stat.setInt(8, tran.id_transaksi);
                menu_transaksi.this.stat.executeUpdate();
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });
        table_transaksi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                text_id_transaksi.setText(model.getValueAt(table_transaksi.getSelectedRow(), 0).toString());
                text_nama_pelanggan.setText(model.getValueAt(table_transaksi.getSelectedRow(), 1).toString());
                text_tanggal.setDate(java.sql.Date.valueOf(model.getValueAt(table_transaksi.getSelectedRow(), 3).toString()));
                text_jml_beli.setText(model.getValueAt(table_transaksi.getSelectedRow(), 6).toString());
                text_total_bayar.setText(model.getValueAt(table_transaksi.getSelectedRow(), 7).toString());
            }
        });
        btn_delete.addActionListener(a -> {
            try {
                transaksi tran = new transaksi();
                tran.id_transaksi = Integer.parseInt(text_id_transaksi.getText());
                menu_transaksi.this.stat = k.getCon().prepareStatement("delete from transaksi " +
                        "where id_transaksi=?");
                menu_transaksi.this.stat.setInt(1, tran.id_transaksi);
                menu_transaksi.this.stat.executeUpdate();
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });
        btn_menu_minuman.addActionListener(e -> {

        });
        btn_logout.addActionListener(e -> {
            Login login = new Login();
            login.setVisible(true);
            menu_transaksi.this.setVisible(false);
        });
        btn_menu_minuman.addActionListener(e -> {
            menu_minuman minuman = new menu_minuman();
            minuman.setVisible(true);
            menu_transaksi.this.setVisible(false);
            minuman.setBtn_transaksi(true);
            minuman.setCombo_status_minuman(true);
            minuman.setBtn_input(true);
            minuman.setBtn_update(true);
            minuman.setBtn_delete(true);
            minuman.setBtn_registrasi(true);
        });
        btn_cetak_laporan.addActionListener(e -> {
            try {
                PDFReport.generatePDFReport();
                JOptionPane.showMessageDialog(null, "PDF report generated successfully.");
            } catch (ClassNotFoundException | SQLException | DocumentException | IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to generate PDF report: " + ex.getMessage());
            }
        });
    }

    public void setBtn_cetak_laporan(boolean enabled) {
        btn_cetak_laporan.setEnabled(enabled);
    }

    private void createUIComponents() {
        text_tanggal = new JDateChooser();
        text_tanggal.setDateFormatString("yyyy-MM-dd");
    }

    public void refreshTable() {
        model = new DefaultTableModel();
        model.addColumn("ID Transaksi");
        model.addColumn("Nama Pelanggan");
        model.addColumn("ID minuman");
        model.addColumn("Tanggal");
        model.addColumn("Nama minuman");
        model.addColumn("Harga");
        model.addColumn("Jumlah Beli");
        model.addColumn("Total Bayar");
        table_transaksi.setModel(model);
        try {
            this.stat = k.getCon().prepareStatement("Select * from transaksi");
            this.rs = this.stat.executeQuery();
            while (rs.next()) {
                Object[] data = {
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8)
                };
                model.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        text_id_transaksi.setText("");
        text_nama_pelanggan.setText("");
        text_jml_beli.setText("");
        text_total_bayar.setText("");
    }

    public void refreshCombo() {
        try {
            this.stat = k.getCon().prepareStatement("select * from minuman " + "where status='Tersedia'");
            this.rs = this.stat.executeQuery();
            while (rs.next()) {
                combo_id_minuman.addItem(rs.getString("id_minuman") + ":" + rs.getString("nama_minuman") + ":" + rs.getString("harga"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public class transaksi {
        int id_transaksi, id_minuman, harga, jumlah_beli, total_bayar;
        String nama_pelanggan, tanggal, nama_minuman;

        public transaksi() {
            this.nama_pelanggan = text_nama_pelanggan.getText();
            String combo = combo_id_minuman.getSelectedItem().toString();
            String[] arr = combo.split(":");
            this.id_minuman = Integer.parseInt(arr[0]);
            try {
                Date date = text_tanggal.getDate();
                DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                this.tanggal = dateformat.format(date);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            this.nama_minuman = arr[1];
            this.harga = Integer.parseInt(arr[2]);
            this.jumlah_beli = Integer.parseInt(text_jml_beli.getText());
            this.total_bayar = this.harga * this.jumlah_beli;
        }
    }
}
