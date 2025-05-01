/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.toko_tubes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author DEWATA
 */
public class DBStock {
    private static Connection conn;
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_NAME = "tokokelontong";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/tokokelontong" ;
    private static final String DB_UNAME = "root";
    private static final String DB_PASS = "";
    
    public static Connection BukaKoneksi(){
        if(conn== null){
            try{
                Class.forName(DB_DRIVER);
                conn = DriverManager.getConnection(DB_URL, DB_UNAME, DB_PASS);
                System.out.println("Koneksi Berhasil!");
            } catch (ClassNotFoundException e){
                System.out.println(e);
                System.err.format("Class not found");
            } catch (SQLException e){
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return conn;
    }
}