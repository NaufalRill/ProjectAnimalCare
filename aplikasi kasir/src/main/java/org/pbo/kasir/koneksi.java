package org.pbo.kasir;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class koneksi {
    private Connection con;

    public void connect() {
        try {
            String url = "jdbc:mysql://localhost/db_cafe";
            String username_xampp = "root";
            String password_xampp = "";
            con = DriverManager.getConnection(url, username_xampp, password_xampp);
            System.out.println("Koneksi Berhasil");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public Connection getCon() {
        return con;
    }
}
