package org.pbo.kasir;

import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class PDFReport {
    public static void generatePDFReport() throws ClassNotFoundException, SQLException, DocumentException, IOException {
        int rowno = 0;
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/db_cafe", "root", "");
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet result = st.executeQuery("SELECT * FROM transaksi");
        ResultSetMetaData meta = result.getMetaData();
        int colno = meta.getColumnCount();
        while (result.next()) {
            rowno = rowno + 1;
        }
        result.first();

        String fileName = "report.pdf";
        File file = new File(fileName);
        int count = 1;
        while (file.exists()) {
            fileName = "report" + count + ".pdf";
            file = new File(fileName);
            count++;
        }

        Document d = new Document();
        PdfWriter.getInstance(d, new FileOutputStream(fileName));
        d.open();

        // Judul Laporan
        Font fontJudul = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(0, 0, 255));
        Paragraph judul = new Paragraph("Informatics Coffee Shop", fontJudul);
        Paragraph kota = new Paragraph("Malang Jawa Timur");
        Paragraph no = new Paragraph("0823123");
        Paragraph email = new Paragraph("ifcoffeeshop@webmail.umm.ac.id");
        judul.setAlignment(Element.ALIGN_CENTER);
        kota.setAlignment(Element.ALIGN_CENTER);
        no.setAlignment(Element.ALIGN_CENTER);
        email.setAlignment(Element.ALIGN_CENTER);
        d.add(judul);
        d.add(kota);
        d.add(no);
        d.add(email);
        Image img = Image.getInstance("foto.png");
        img.scaleAbsolute(50f, 50f);
        img.setAlignment(Element.ALIGN_CENTER);
        d.add(img);

        d.add(new Paragraph("\n"));

        // Tabel
        PdfPTable pt = new PdfPTable(colno);
        pt.setWidthPercentage(100);
        pt.setSpacingBefore(10f);
        pt.setSpacingAfter(10f);

        // Header Tabel
        Font fontHeader = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(255, 255, 255));
        PdfPCell headerCell;
        for (int i = 1; i <= colno; i++) {
            headerCell = new PdfPCell(new Phrase(meta.getColumnName(i), fontHeader));
            headerCell.setBackgroundColor(new Color(0, 0, 255));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pt.addCell(headerCell);
        }

        // Isi Tabel
        Font fontIsi = new Font(Font.HELVETICA, 10);
        PdfPCell isiCell;
        do {
            for (int j = 1; j <= colno; j++) {
                isiCell = new PdfPCell(new Phrase(result.getString(j), fontIsi));
                isiCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                isiCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pt.addCell(isiCell);
            }
        } while (result.next());

        // Tambahkan Tabel ke Dokumen
        d.add(pt);
        d.close();
    }
}