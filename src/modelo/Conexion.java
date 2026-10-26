/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.*;
/**
 *
 * @author J
 */
public class Conexion {
    Connection con;
    
    public Connection conexion(){
        try {
            String myBD = "jdbc:mysql://localhost:3306/data";
            con = DriverManager.getConnection(myBD,"root","");
            return con;
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return null;
    }
}

