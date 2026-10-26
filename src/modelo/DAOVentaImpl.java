package modelo;

import interfaces.DAOVenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @autor JE
 */
public class DAOVentaImpl implements DAOVenta {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();
    
    @Override
    public int registrarVenta(DTOVenta venta) {
        String sqlComprobante = "INSERT INTO comprobante (idCli, idTipo, idEmpleado, fecha, importe_total, igv, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detallecomprobante (idComprobante, idVehiculo, cantidadVehiculos, subtotal) VALUES (?, ?, ?, ?)";
        int idComprobante = 0;
        
        try {
            con = cn.conexion();
            con.setAutoCommit(false);

            ps = con.prepareStatement(sqlComprobante, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, venta.getIdCliente());
            ps.setInt(2, venta.getIdTipo());
            ps.setInt(3, venta.getIdEmpleado());
            ps.setTimestamp(4, new java.sql.Timestamp(venta.getFecha().getTime()));
            ps.setFloat(5, venta.getImporteTotal());
            ps.setFloat(6, venta.getIgv());
            ps.setFloat(7, venta.getTotal());
            ps.executeUpdate();
            
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                idComprobante = generatedKeys.getInt(1);
            }
            generatedKeys.close();
            ps.close();

            ps = con.prepareStatement(sqlDetalle);
            for (DTODetalleComprobante detalle : venta.getDetalles()) {
                ps.setInt(1, idComprobante);
                ps.setInt(2, detalle.getIdVehiculo());
                ps.setInt(3, detalle.getCantidadVehiculos());
                ps.setFloat(4, detalle.getSubtotal());
                ps.addBatch();
            }
            ps.executeBatch();
            
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return idComprobante;
    }
    
    public int buscarUltimoIDComprobante() {
        String sql = "SELECT MAX(idComprobante) AS idComprobante FROM comprobante";
        int idComprobante = -1;  // Valor por defecto en caso de no encontrar ningún comprobante
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                idComprobante = rs.getInt("idComprobante");
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
        return idComprobante; 
    }
}
