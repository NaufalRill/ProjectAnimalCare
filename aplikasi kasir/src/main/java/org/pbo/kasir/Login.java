package org.pbo.kasir;


import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame {
    koneksi k = new koneksi();
    private JTextField text_username;
    private JPasswordField text_password;
    private JButton btn_login;
    private JPanel menuLogin;
    private JButton RegistrasiButton;

    private PreparedStatement stat;
    private ResultSet rs;

    public Login() {
        k.connect();
        setContentPane(menuLogin);
        setLocationRelativeTo(null);
        setSize(250, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        btn_login.addActionListener(a -> {
            user u = new user();
            try {
                Login.this.stat = k.getCon().prepareStatement("select * from user where " +
                        "username='" + u.username + "' and password='" + u.password + "';");
                Login.this.rs = Login.this.stat.executeQuery();
                while (rs.next()) {
                    u.id_level = rs.getInt("id_level");
                }
                if (u.id_level == 0) {
                    JOptionPane.showMessageDialog(null, "AKUN TIDAK DITEMUKAN");
                } else {
                    switch (u.id_level) {
                        case 1 -> {
                            menu_transaksi transaksi1 = new menu_transaksi();
                            transaksi1.setVisible(true);
                            Login.this.setVisible(false);
                            transaksi1.setBtn_cetak_laporan(true);
                        }
                        case 2 -> {
                            menu_transaksi transaksi = new menu_transaksi();
                            transaksi.setVisible(true);
                            Login.this.setVisible(false);
                        }

                        case 4 -> {
                            menu_minuman minuman = new menu_minuman();
                            minuman.setVisible(true);
                            Login.this.setVisible(false);
                            minuman.setBtn_logout(true);
                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });
        RegistrasiButton.addActionListener(a -> {
            menu_registrasi registrasi = new menu_registrasi();
            registrasi.setVisible(true);
            Login.this.setVisible(false);
        });
    }

    class user {
        int id_user, id_level;
        String username, password, nama_user;

        public user() {
            this.id_user = 0;
            this.username = text_username.getText();
            this.password = text_password.getText();
            this.nama_user = "";
            this.id_level = 0;
        }
    }
}
