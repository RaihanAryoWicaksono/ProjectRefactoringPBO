/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.toko_tubes;

import com.mysql.cj.callback.UsernameCallback;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DEWATA
 */
public class Penjualan extends javax.swing.JPanel {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private int IDPetugas;

    private void setHargaBarang() {
        try {
         

            String query = "SELECT harga_satuan FROM barang WHERE nama_barang = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, txNamaBarang.getSelectedItem().toString());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                txHargaBarang.setText(resultSet.getString("harga_satuan"));
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        } 
    }

    private void txNamaBarangItemChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            setHargaBarang();
        }
    }

    /**
     * Creates new form Penjualan
     */
    public Penjualan(int IDPetugas) {
        initComponents();
        this.IDPetugas = IDPetugas;
        try {
          connection = ConnectionDB.BukaKoneksi();
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

            // Eksekusi query
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
    
    public void tambahTransaksi() {
    double jumlah, harga, total;

    jumlah = Double.parseDouble(satuan.getText());
    harga = Double.parseDouble(txHargaBarang.getText());
    total = jumlah * harga;

    loadData(jumlah, harga, total);
    totalBiaya();
    clear2();
}

public void loadData(double jumlah, double harga, double total) {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.addRow(new Object[]{
        txPembeli.getText(),
        txNamaBarang.getSelectedItem().toString(),
        txHargaBarang.getText(),
        jumlah,
        total
    });
}

public void clear2() {
    txNamaBarang.setSelectedIndex(0);
    txHargaBarang.setText("");
    satuan.setText("");
}

 public void totalBiaya() {
    int jumlahBaris = jTable1.getRowCount();
    double totalBiaya = 0;

    for (int i = 0; i < jumlahBaris; i++) {
        double jumlahBarang = Double.parseDouble(jTable1.getValueAt(i, 3).toString());
        double hargaBarang = Double.parseDouble(jTable1.getValueAt(i, 2).toString());
        
        double total = (jumlahBarang * hargaBarang);
        jTable1.setValueAt(total, i, 4);

        totalBiaya += total;
    }
    txTotalHarga.setText(String.valueOf(totalBiaya));
}

 
    private void DataTransaksi(int IDPetugas) throws SQLException {
        connection.setAutoCommit(false);

        
        double totalPenjualan = TotalPenjualan();
        
        String insertTransaksiQuery = "INSERT INTO transaksi_penjualan (tanggal_transaksi, total_penjualan, id_petugas) VALUES(NOW(), ?, ?)";
        PreparedStatement insertTransaksiStatement = connection.prepareStatement(insertTransaksiQuery, Statement.RETURN_GENERATED_KEYS);
        insertTransaksiStatement.setDouble(1, totalPenjualan);
        insertTransaksiStatement.setInt(2, IDPetugas);
        insertTransaksiStatement.executeUpdate();
        
        ResultSet generatedKeys = insertTransaksiStatement.getGeneratedKeys();
        int id_transaksi_penjualan = -1;
        if (generatedKeys.next()) {
            id_transaksi_penjualan = generatedKeys.getInt(1);
        } else {
            throw new SQLException("Creating transaction failed, no ID obtained.");
        }
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            String namaBarang = jTable1.getValueAt(i, 1).toString();
            double hargaBarang = Double.parseDouble(jTable1.getValueAt(i, 2).toString());
            double jumlahBarang = Double.parseDouble(jTable1.getValueAt(i, 3).toString());
            double subtotal = Double.parseDouble(jTable1.getValueAt(i, 4).toString());
        
        int id_barang = getIdBarang(namaBarang);
        InsertDetailTransaksi(id_transaksi_penjualan, id_barang, jumlahBarang, subtotal);
        updateStokBarang(id_barang, jumlahBarang);
    }
    connection.commit();
}
    
    private double TotalPenjualan() {
    double total = 0.0;
    for (int i = 0; i < jTable1.getRowCount(); i++) {
        double subtotal = Double.parseDouble(jTable1.getValueAt(i, 4).toString());
        total += subtotal;
    }
    return total;
}
    
       
private int getIdBarang(String namaBarang) throws SQLException{
    String idBarangQuery = "SELECT id_barang FROM barang WHERE nama_barang = ?";
    PreparedStatement idBarangStatement = connection.prepareStatement(idBarangQuery);
    idBarangStatement.setString(1, namaBarang);
    ResultSet rs = idBarangStatement.executeQuery();

    if (rs.next()) {
        return rs.getInt("id_barang");
    } else {
        return -1;
    }
}

private void InsertDetailTransaksi(int id_transaksi_penjualan, int id_barang, double jumlahBarang, double subtotal) throws  SQLException{
     String query = "INSERT INTO detail_transaksi_penjualan (id_transaksi_penjualan, id_barang, jumlah_barang, subtotal) VALUES (?, ?, ?,?)";
           PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id_transaksi_penjualan);
            preparedStatement.setInt(2, id_barang);
            preparedStatement.setDouble(3, jumlahBarang);
            preparedStatement.setDouble(4, subtotal);
            preparedStatement.executeUpdate();
            System.out.println("Insert Berhasil");
}
       
private void updateStokBarang(int id_barang, double  jumlahTerjual) throws  SQLException{
    String stokQuery = "SELECT stok FROM barang WHERE id_barang = ?";
    PreparedStatement stokStatement = connection.prepareStatement(stokQuery);
    stokStatement.setInt(1, id_barang);
    ResultSet rs = stokStatement.executeQuery();
    
    if(rs.next()){
        double stokSaatIni = rs.getDouble("stok");
        if(stokSaatIni >= jumlahTerjual){
            String updateQuery = "UPDATE barang SET stok = stok - ? WHERE id_barang = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setDouble(1, jumlahTerjual);
            updateStatement.setInt(2, id_barang);
            updateStatement.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Transaction data has been saved successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Stok barang tidak mencukupi untuk transaksi ini!");
        }
    } 
    
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

        PanelPenjualan = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txPembeli = new javax.swing.JTextField();
        satuan = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        SubmitButton = new javax.swing.JButton();
        txTotalHarga = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txHargaBarang = new javax.swing.JTextField();
        Enter = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        txNamaBarang = new javax.swing.JComboBox<>();
        refreshButton = new javax.swing.JButton();

        PanelPenjualan.setBackground(new java.awt.Color(0, 51, 153));
        PanelPenjualan.setForeground(new java.awt.Color(255, 255, 255));
        PanelPenjualan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Keranjang Penjualan");
        PanelPenjualan.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Satuan :");
        PanelPenjualan.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 240, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nama Pembeli :");
        PanelPenjualan.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, -1, -1));

        txPembeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txPembeliActionPerformed(evt);
            }
        });
        PanelPenjualan.add(txPembeli, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 180, -1));

        satuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                satuanActionPerformed(evt);
            }
        });
        PanelPenjualan.add(satuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 240, 180, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama Pembeli", "Nama Barang", "Harga Barang", "Jumlah", "Total"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        SubmitButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        SubmitButton.setText("SUBMIT");
        SubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubmitButtonActionPerformed(evt);
            }
        });

        txTotalHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txTotalHargaActionPerformed(evt);
            }
        });

        jLabel9.setText("Total Harga :");

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.setText("DELETE");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(SubmitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(34, 34, 34))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(SubmitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        PanelPenjualan.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 80, 530, 620));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Dashboard Penjualan");
        PanelPenjualan.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Nama Barang  :");
        PanelPenjualan.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Harga Barang  :");
        PanelPenjualan.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, -1, -1));

        txHargaBarang.setEnabled(false);
        txHargaBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txHargaBarangActionPerformed(evt);
            }
        });
        PanelPenjualan.add(txHargaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 180, -1));

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
        PanelPenjualan.add(Enter, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, -1, -1));

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setText("RESET");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        PanelPenjualan.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 290, -1, -1));

        txNamaBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txNamaBarangActionPerformed(evt);
            }
        });
        PanelPenjualan.add(txNamaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, 180, -1));

        refreshButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\DEWATA\\Documents\\NetBeansProjects\\TOKO_TUBES\\src\\383083_refresh_reload_icon (1).png")); // NOI18N
        PanelPenjualan.add(refreshButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 640, 50, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1008, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(PanelPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 726, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(PanelPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 6, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txPembeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txPembeliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txPembeliActionPerformed

    private void satuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_satuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_satuanActionPerformed

    private void SubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubmitButtonActionPerformed
        try {
            tampilkanStrukBelanja();
            DataTransaksi(IDPetugas);
        } catch (SQLException ex) {
            Logger.getLogger(Penjualan.class.getName()).log(Level.SEVERE, null, ex);
        }
        clearJTable();
    }//GEN-LAST:event_SubmitButtonActionPerformed

    private void txTotalHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txTotalHargaActionPerformed

    }//GEN-LAST:event_txTotalHargaActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
        int row = jTable1.getSelectedRow();
        model.removeRow(row);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void txHargaBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txHargaBarangActionPerformed
        //
    }//GEN-LAST:event_txHargaBarangActionPerformed

    private void EnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnterActionPerformed
        
    }//GEN-LAST:event_EnterActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        txPembeli.setText("");
        txNamaBarang.setSelectedItem(null);
        txHargaBarang.setText("");
        satuan.setText("");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void EnterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EnterMouseClicked
        tambahTransaksi();
    }//GEN-LAST:event_EnterMouseClicked

    private void txNamaBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txNamaBarangActionPerformed
        
    }//GEN-LAST:event_txNamaBarangActionPerformed

    private void clearJTable() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); // Clear all rows from the table
}
    
    private void clearFields() {
        txPembeli.setText("");
        txNamaBarang.setSelectedIndex(0);
        txHargaBarang.setText("");
        satuan.setText("");
    }

    public void tampilkanStrukBelanja() {
    StringBuilder struk = new StringBuilder();
    double totalBelanja = 0.0;

    
    struk.append("===== Struk Belanja =====\n");
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    for (int i = 0; i < model.getRowCount(); i++) {
        String namaBarang = model.getValueAt(i, 1).toString();
        double hargaSatuan = Double.parseDouble(model.getValueAt(i, 2).toString());
        double jumlahBarang = Double.parseDouble(model.getValueAt(i, 3).toString());
        double total = Double.parseDouble(model.getValueAt(i, 4).toString());
        
        struk.append("Nama Barang: ").append(namaBarang).append("\n");
        struk.append("Harga Satuan: ").append(hargaSatuan).append("\n");
        struk.append("Jumlah Barang: ").append(jumlahBarang).append("\n");
        struk.append("Total Harga: ").append(total).append("\n");
        struk.append("\n");

        totalBelanja += total;
    }

    struk.append("Total Belanja: ").append(totalBelanja).append("\n");
    JOptionPane.showMessageDialog(this, struk.toString(), "Struk Belanja", JOptionPane.INFORMATION_MESSAGE);
}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Enter;
    private javax.swing.JPanel PanelPenjualan;
    private javax.swing.JButton SubmitButton;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTextField satuan;
    private javax.swing.JTextField txHargaBarang;
    private javax.swing.JComboBox<String> txNamaBarang;
    private javax.swing.JTextField txPembeli;
    private javax.swing.JTextField txTotalHarga;
    // End of variables declaration//GEN-END:variables
    
}