package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @autor JE
 */
public class DAOReporte {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    public List<Object[]> listarReporte() {
        List<Object[]> reporte = new ArrayList<>();
        String sqlComprobante = "SELECT " +
                "com.idComprobante, " +
                "com.idCli, " +
                "tc.descripcion AS TIPO, " +
                "com.fecha AS FECHA, " +
                "v.modelo AS MODELODECARRO, " +
                "com.total AS TOTAL " +
                "FROM comprobante com " +
                "JOIN cliente c ON com.idCli = c.idCli " +
                "JOIN detallecomprobante dc ON dc.idComprobante = com.idComprobante " +
                "JOIN vehiculo v ON v.idVehiculo = dc.idVehiculo " +
                "JOIN tipo_comprobante tc ON com.idTipo = tc.idTipo";

        try {
            con = cn.conexion();
            ps = con.prepareStatement(sqlComprobante);
            rs = ps.executeQuery();

            while (rs.next()) {
                int idCli = rs.getInt("idCli");
                String tipo = rs.getString("TIPO");
                java.sql.Timestamp fecha = rs.getTimestamp("FECHA");
                String modeloDeCarro = rs.getString("MODELODECARRO");
                float total = rs.getFloat("TOTAL");
                String cliente = obtenerNombreCliente(idCli);

                Object[] row = {tipo, cliente, fecha, modeloDeCarro, total};
                reporte.add(row);
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
        return reporte;
    }

    private String obtenerNombreCliente(int idCli) {
        String nombreCliente = null;
        String sqlNatural = "SELECT nombre FROM cliente_natural WHERE idCli = ?";
        String sqlEmpresa = "SELECT razon_social AS nombre FROM cliente_empresa WHERE idCli = ?";

        Connection conLocal = null;
        PreparedStatement psLocal = null;
        ResultSet rsLocal = null;

        try {
            conLocal = cn.conexion();
            psLocal = conLocal.prepareStatement(sqlNatural);
            psLocal.setInt(1, idCli);
            rsLocal = psLocal.executeQuery();

            if (rsLocal.next()) {
                nombreCliente = rsLocal.getString("nombre");
            } else {
                if (rsLocal != null) rsLocal.close();
                if (psLocal != null) psLocal.close();

                psLocal = conLocal.prepareStatement(sqlEmpresa);
                psLocal.setInt(1, idCli);
                rsLocal = psLocal.executeQuery();

                if (rsLocal.next()) {
                    nombreCliente = rsLocal.getString("nombre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsLocal != null) rsLocal.close();
                if (psLocal != null) psLocal.close();
                if (conLocal != null) conLocal.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return nombreCliente;
    }
}
