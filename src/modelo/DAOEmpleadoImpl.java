package modelo;

import interfaces.DAOEmpleado;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import modelo.Conexion;

public class DAOEmpleadoImpl implements DAOEmpleado{

    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    @Override
    public int grabar(DTOEmpleado obj) {
        int resultado = 0;
        String sql = "INSERT INTO empleado (nombre, telefono, apellidos, usuario, dni,contrasena) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getTelefono());
            ps.setString(3, obj.getApellido());
            ps.setString(4, obj.getCorreo());
            ps.setString(5, obj.getDni());
            ps.setString(6, obj.getDni());
            resultado = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultado;
    }

    @Override
    public int actualizar(DTOEmpleado obj) {
        int resultado = 0;
        String sql = "UPDATE empleado SET nombre=?, telefono=?, apellidos=?, usuario=?, dni=? WHERE idEmpleado=?";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getTelefono());
            ps.setString(3, obj.getApellido());
            ps.setString(4, obj.getCorreo());
            ps.setString(5, obj.getDni());
            ps.setInt(6, obj.getId());
            resultado = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultado;
    }

    @Override
    public List<DTOEmpleado> leer() {
        List<DTOEmpleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleado";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                DTOEmpleado usuario = new DTOEmpleado();
                usuario.setId(rs.getInt("idEmpleado"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setApellido(rs.getString("apellidos"));
                usuario.setCorreo(rs.getString("usuario"));
                usuario.setDni(rs.getString("dni"));
                lista.add(usuario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    @Override
    public int borrar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM empleado WHERE idEmpleado=?";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            resultado = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultado;
    }
    @Override
    public boolean IniciarSesionEmpleado(DTOEmpleado obj) {
        boolean validado = false;
        String sql = "SELECT * FROM empleado WHERE usuario=? AND contrasena=?";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, obj.getCorreo());
            ps.setString(2, obj.getPass());
            rs = ps.executeQuery();
            if (rs.next()) {
                validado = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return validado;
    }

}
