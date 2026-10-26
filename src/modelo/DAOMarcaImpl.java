package modelo;

import interfaces.DAOMarca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOMarcaImpl implements DAOMarca {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    @Override
    public boolean grabar(DTOMarca oMarca) {
        String sql = "INSERT INTO marca (nombre) VALUES (?)";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, oMarca.getMarca());
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
    public boolean actualizar(DTOMarca oMarca) {
        String sql = "UPDATE marca SET nombre = ? WHERE idMarca = ?";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, oMarca.getMarca());
            ps.setInt(2, oMarca.getIdMarca());
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
    public List<DTOMarca> leer() {
        List<DTOMarca> lista = new ArrayList<>();
        String sql = "SELECT * FROM marca";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                DTOMarca marca = new DTOMarca();
                marca.setIdMarca(rs.getInt("idMarca"));
                marca.setMarca(rs.getString("nombre"));
                lista.add(marca);
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
        return lista;
    }

    @Override
    public boolean borrar(DTOMarca oMarca) {
        String sql = "DELETE FROM marca WHERE idMarca = ?";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, oMarca.getIdMarca());
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
    
    public DTOMarca buscarPorId(DTOMarca oMarca) {
        DTOMarca marca = null;
        String sql = "SELECT * FROM marca WHERE idMarca = ?";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, oMarca.getIdMarca());
            rs = ps.executeQuery();
            if (rs.next()) {
                marca = new DTOMarca();
                marca.setIdMarca(rs.getInt("idMarca"));
                marca.setMarca(rs.getString("nombre"));
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
        return marca;
    }
    
}
