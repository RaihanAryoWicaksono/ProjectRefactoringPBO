/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.toko_tubes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class StokBarang extends javax.swing.JPanel {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    private void setHargaBarang() {
        try {
         

            String query = "SELECT harga_satuan FROM barang WHERE nama_barang = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, txNamaBarang.getSelectedItem().toString());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                txHargaBarang1.setText(resultSet.getString("harga_satuan"));
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        } 
    }

    private void txNamaBarangItemChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            setHargaBarang();
            getIDbarang();
        }
    }
    
//    private void txNamaBarangIDchange(java.awt.event.ItemEvent evt) {
//        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
//            getIDbarang();
//        }
//    }
    
    private void getIDbarang() {
        try {
         

            String query = "SELECT id_barang FROM barang WHERE nama_barang = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, txNamaBarang.getSelectedItem().toString());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                txIDbarang.setText(resultSet.getString("id_barang"));
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        } 
    }
    
    
    
    public StokBarang() {
        initComponents();
        try { //Constructor untuk koneksi
          connection = DBStock.BukaKoneksi();
        } catch (Exception e) {
        }
        BarangComboBox(txNamaBarang);
        
        txNamaBarang.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            txNamaBarangItemChanged(evt);
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        refreshComboBox();
    }
});
    }
    
    
    private void BarangComboBox(JComboBox comboBox) {
        try {
            String query = "SELECT nama_barang FROM barang";

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            DefaultComboBoxModel model = new DefaultComboBoxModel();

            while (resultSet.next()) {
                model.addElement(resultSet.getString("nama_barang"));
            }
            comboBox.setModel(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } 

    }
    
    public void loadData() {
        DefaultTableModel model = (DefaultTableModel) TableEditHarga.getModel();
        model.addRow(new Object[]{
        txIDbarang.getText(),
        txNamaBarang.getSelectedItem().toString(),
        txHargaBarang1.getText(),
        txEditHarga.getText()
    });
    }
    
    private void updateHarga() throws SQLException {
        for (int i = 0; i < TableEditHarga.getRowCount(); i++) {
        int idBarang = Integer.parseInt(TableEditHarga.getValueAt(i, 0).toString()); // Ambil ID_barang dari kolom pertama
        String namaBarang = TableEditHarga.getValueAt(i, 1).toString(); // Ambil nama_barang dari kolom kedua
        double hargaLama = Double.parseDouble(TableEditHarga.getValueAt(i, 2).toString()); // Ambil harga_lama dari kolom ketiga
        double hargaBaru = Double.parseDouble(TableEditHarga.getValueAt(i, 3).toString()); // Ambil harga_baru dari kolom keempat
        
        updateHargaDatabase(idBarang, hargaBaru);
    }
    System.out.println("Harga barang berhasil diperbarui!");
    }
    
    private void updateHargaDatabase(int idBarang, double hargaBaru) throws SQLException{
        String sql = "UPDATE barang SET harga_satuan =? WHERE id_barang = ?";
        PreparedStatement hargaStatement = connection.prepareStatement(sql);
        hargaStatement.setDouble(1, hargaBaru);
        hargaStatement.setInt(2, idBarang);
        hargaStatement.executeUpdate();
        JOptionPane.showMessageDialog(this, "Harga barang dengan ID " + idBarang + " berhasil diupdate");
    }
    
    private void refreshComboBox() {
    try {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        String query = "SELECT nama_barang FROM barang";
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            model.addElement(resultSet.getString("nama_barang"));
        }

        txNamaBarang.setModel(model);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SBpanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        SBtable = new javax.swing.JTable();
        tampilkan = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txNamaBarang = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txIDbarang = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txEditHarga = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        Enter = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableEditHarga = new javax.swing.JTable();
        ubahHarga = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txHargaBarang1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1008, 726));

        SBpanel.setBackground(new java.awt.Color(0, 51, 153));
        SBpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Edit  Harga Barang");
        SBpanel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 170, -1));

        SBtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Barang", "Nama Barang", "Harga/pcs", "Stok"
            }
        ));
        jScrollPane1.setViewportView(SBtable);

        SBpanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(462, 100, 520, 480));

        tampilkan.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        tampilkan.setText("TAMPILKAN");
        tampilkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tampilkanActionPerformed(evt);
            }
        });
        SBpanel.add(tampilkan, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 640, -1, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Stok Barang");
        SBpanel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, -1, -1));

        txNamaBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txNamaBarangActionPerformed(evt);
            }
        });
        SBpanel.add(txNamaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 180, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ID :");
        SBpanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 100, -1, -1));

        txIDbarang.setEnabled(false);
        txIDbarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txIDbarangActionPerformed(evt);
            }
        });
        SBpanel.add(txIDbarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, 20, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Harga Barang :");
        SBpanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, -1, -1));

        txEditHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txEditHargaActionPerformed(evt);
            }
        });
        SBpanel.add(txEditHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 180, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Edit Harga      :");
        SBpanel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, -1, -1));

        Enter.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Enter.setText("ENTER");
        Enter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EnterMouseClicked(evt);
            }
        });
        Enter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnterActionPerformed(evt);
            }
        });
        SBpanel.add(Enter, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, -1, -1));

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setText("RESET");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        SBpanel.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 230, -1, -1));

        TableEditHarga.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Barang", "Nama Barang", "Harga Lama", "Harga Baru"
            }
        ));
        jScrollPane2.setViewportView(TableEditHarga);

        SBpanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 360, 290));

        ubahHarga.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        ubahHarga.setText("UBAH");
        ubahHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubahHargaActionPerformed(evt);
            }
        });
        SBpanel.add(ubahHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 640, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nama Barang :");
        SBpanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, -1));

        txHargaBarang1.setEnabled(false);
        txHargaBarang1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txHargaBarang1ActionPerformed(evt);
            }
        });
        SBpanel.add(txHargaBarang1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 180, -1));

        jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton1.setText("DELETE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        SBpanel.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 640, -1, -1));

        refreshButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\DEWATA\\Documents\\NetBeansProjects\\TOKO_TUBES\\src\\383083_refresh_reload_icon (1).png")); // NOI18N
        SBpanel.add(refreshButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 640, 50, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SBpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SBpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tampilkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tampilkanActionPerformed
        try {
            String query = "SELECT id_barang, nama_barang, harga_satuan, stok FROM barang";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            // Buat model tabel baru
            DefaultTableModel model = (DefaultTableModel) SBtable.getModel();
            model.setRowCount(0); // Kosongkan tabel sebelum menambahkan data baru

            // Masukkan data dari hasil query ke dalam model tabel
            while (resultSet.next()) {
                int idBarang = resultSet.getInt("id_barang");
                String namaBarang = resultSet.getString("nama_barang");
                double hargaSatuan = resultSet.getDouble("harga_satuan");
                int stok = resultSet.getInt("stok");

                model.addRow(new Object[]{idBarang, namaBarang, hargaSatuan, stok});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_tampilkanActionPerformed

    private void txNamaBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txNamaBarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txNamaBarangActionPerformed

    private void txIDbarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txIDbarangActionPerformed
        //
    }//GEN-LAST:event_txIDbarangActionPerformed

    private void txEditHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txEditHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txEditHargaActionPerformed

    private void EnterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EnterMouseClicked
//        tambahTransaksi();
    }//GEN-LAST:event_EnterMouseClicked

    private void EnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnterActionPerformed
        loadData();
    }//GEN-LAST:event_EnterActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        txNamaBarang.setSelectedItem(null);
        txIDbarang.setText("");
        txEditHarga.setText("");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void ubahHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubahHargaActionPerformed
        try {
            updateHarga();
        } catch (SQLException ex) {
            Logger.getLogger(StokBarang.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ubahHargaActionPerformed

    private void txHargaBarang1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txHargaBarang1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txHargaBarang1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    DefaultTableModel model = (DefaultTableModel)TableEditHarga.getModel();
        int row = TableEditHarga.getSelectedRow();
        model.removeRow(row);
    }//GEN-LAST:event_jButton1ActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Enter;
    private javax.swing.JPanel SBpanel;
    private javax.swing.JTable SBtable;
    private javax.swing.JTable TableEditHarga;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton tampilkan;
    private javax.swing.JTextField txEditHarga;
    private javax.swing.JTextField txHargaBarang1;
    private javax.swing.JTextField txIDbarang;
    private javax.swing.JComboBox<String> txNamaBarang;
    private javax.swing.JButton ubahHarga;
    // End of variables declaration//GEN-END:variables
}
