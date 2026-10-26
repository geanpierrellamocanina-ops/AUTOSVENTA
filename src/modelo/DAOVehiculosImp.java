/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import com.mysql.cj.jdbc.exceptions.PacketTooBigException;
import interfaces.DAOVehiculos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JE
 */
public class DAOVehiculosImp implements DAOVehiculos{
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();


    /**
     * Graba un vehículo en la BD.
     * @return null si tuvo éxito, o el mensaje de error si falló.
     */
    public String grabar(DTOVehiculo oVehiculo) {
        String sql = "INSERT INTO vehiculo (placa, idMarca, modelo, color, categoria, año, costo, imagenVehiculo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, oVehiculo.getCodigovehiculo());
            ps.setInt(2, oVehiculo.getMarca());
            ps.setString(3, oVehiculo.getModelo());
            ps.setString(4, oVehiculo.getColor());
            ps.setString(5, oVehiculo.getCategoria());
            ps.setString(6, oVehiculo.getAño());
            ps.setFloat(7, oVehiculo.getCosto());

            byte[] img = oVehiculo.getImagenVehiculo();
            if (img != null) {
                ps.setBytes(8, img);
            } else {
                ps.setNull(8, Types.BLOB);
            }
            ps.executeUpdate();
            return null; // éxito
        } catch (PacketTooBigException e) {
            e.printStackTrace();
            return "La imagen supera el límite permitido por el servidor MySQL (max_allowed_packet).\n"
                 + "Seleccione una imagen más pequeña (maximo 1 MB).";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error SQL: " + e.getMessage();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

        @Override
    public boolean actualizar(DTOVehiculo oVehiculo) {
        String sql = "UPDATE vehiculo SET placa = ?, idMarca = ?, modelo = ?, color = ?, categoria = ?, año = ?, costo = ? WHERE idVehiculo = ?";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, oVehiculo.getCodigovehiculo());
            ps.setInt(2, oVehiculo.getMarca());
            ps.setString(3, oVehiculo.getModelo());
            ps.setString(4, oVehiculo.getColor());
            ps.setString(5, oVehiculo.getCategoria());
            ps.setString(6, oVehiculo.getAño());
            ps.setFloat(7, oVehiculo.getCosto());
            ps.setInt(8, oVehiculo.getIdvehiculo());
            ps.executeUpdate();
            return true;  
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public List<DTOVehiculo> leer() {
        List<DTOVehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT * FROM vehiculo";
        
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                DTOVehiculo vehiculo = new DTOVehiculo();
                vehiculo.setIdvehiculo(rs.getInt("idVehiculo"));
                vehiculo.setCodigovehiculo(rs.getString("placa"));
                vehiculo.setMarca(rs.getInt("idMarca"));
                vehiculo.setModelo(rs.getString("modelo"));
                vehiculo.setColor(rs.getString("color"));
                vehiculo.setCategoria(rs.getString("categoria"));
                vehiculo.setAño(rs.getString("año"));
                vehiculo.setCosto(rs.getFloat("costo"));
                vehiculo.setImagenVehiculo(rs.getBytes("imagenVehiculo")); // Obtener la imagen del vehículo
                vehiculos.add(vehiculo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return vehiculos;
    }
    @Override
    public boolean borrar(DTOVehiculo oVehiculo) {
        String sql = "DELETE FROM vehiculo WHERE idVehiculo = ?";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, oVehiculo.getIdvehiculo());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public DTOVehiculo verificarVehiculo(DTOVehiculo oVehiculo) {
        String sql = "SELECT idVehiculo, modelo, idMarca, color, placa, costo FROM vehiculo WHERE placa = ?";
        DTOVehiculo vehiculoEncontrado = null;
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, oVehiculo.getCodigovehiculo());
            rs = ps.executeQuery();
            if (rs.next()) {
                vehiculoEncontrado = new DTOVehiculo();
                vehiculoEncontrado.setIdvehiculo(rs.getInt("idVehiculo"));
                vehiculoEncontrado.setModelo(rs.getString("modelo"));
                vehiculoEncontrado.setMarca(rs.getInt("idMarca"));
                vehiculoEncontrado.setColor(rs.getString("color"));
                vehiculoEncontrado.setCodigovehiculo(rs.getString("placa"));
                vehiculoEncontrado.setCosto(rs.getFloat("costo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return vehiculoEncontrado; 
    }
    
    public DTOVehiculo verificarVehiculoPorId(int idVehiculo) {
    String sql = "SELECT idVehiculo, modelo, idMarca, color, placa, costo FROM vehiculo WHERE idVehiculo = ?";
    DTOVehiculo vehiculoEncontrado = null;
    try {
        con = cn.conexion();
        ps = con.prepareStatement(sql);
        ps.setInt(1, idVehiculo);
        rs = ps.executeQuery();
        if (rs.next()) {
            vehiculoEncontrado = new DTOVehiculo();
            vehiculoEncontrado.setIdvehiculo(rs.getInt("idVehiculo"));
            vehiculoEncontrado.setModelo(rs.getString("modelo"));
            vehiculoEncontrado.setMarca(rs.getInt("idMarca"));
            vehiculoEncontrado.setColor(rs.getString("color"));
            vehiculoEncontrado.setCodigovehiculo(rs.getString("placa"));
            vehiculoEncontrado.setCosto(rs.getFloat("costo"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return vehiculoEncontrado; 
} 
}

