package modelo;

import interfaces.DAOCliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOClienteImpl implements DAOCliente {
    
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Conexion cn = new Conexion();

    @Override
    public void grabar(DTOCliente oCliente) {
        String sqlCliente = "INSERT INTO cliente (tipo_cliente, direccion, correo) VALUES (?, ?, ?)";
        try {
            con = cn.conexion();
            con.setAutoCommit(false);
            ps = con.prepareStatement(sqlCliente, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, oCliente.getTipoCliente());
            ps.setString(2, oCliente.getDireccion());
            ps.setString(3, oCliente.getCorreo());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idCli = generatedKeys.getInt(1);

                if (oCliente instanceof DTOClienteNatural) {
                    DTOClienteNatural natural = (DTOClienteNatural) oCliente;
                    String sqlNatural = "INSERT INTO cliente_natural (idCli, nombre, apellidos, dni) VALUES (?, ?, ?, ?)";
                    ps = con.prepareStatement(sqlNatural);
                    ps.setInt(1, idCli);
                    ps.setString(2, natural.getNombre());
                    ps.setString(3, natural.getApellidos());
                    ps.setString(4, natural.getDni());
                    ps.executeUpdate();
                } else if (oCliente instanceof DTOClienteEmpresa) {
                    DTOClienteEmpresa empresa = (DTOClienteEmpresa) oCliente;
                    String sqlEmpresa = "INSERT INTO cliente_empresa (idCli, ruc, razon_social) VALUES (?, ?, ?)";
                    ps = con.prepareStatement(sqlEmpresa);
                    ps.setInt(1, idCli);
                    ps.setString(2, empresa.getRuc());
                    ps.setString(3, empresa.getRazonSocial());
                    ps.executeUpdate();
                }
            }

            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   @Override
    public List<DTOCliente> leer() {
        List<DTOCliente> lista = new ArrayList<>();
        String sqlCliente = "SELECT * FROM cliente";
        try {
            con = cn.conexion();
            ps = con.prepareStatement(sqlCliente);
            rs = ps.executeQuery();

            while (rs.next()) {
                int idCli = rs.getInt("idCli");
                String tipoCliente = rs.getString("tipo_cliente");
                String direccion = rs.getString("direccion");
                String correo = rs.getString("correo");

                if (tipoCliente.equalsIgnoreCase("natural")) {
                    String sqlNatural = "SELECT * FROM cliente_natural WHERE idCli = ?";
                    try (PreparedStatement psNatural = con.prepareStatement(sqlNatural)) {
                        psNatural.setInt(1, idCli);
                        try (ResultSet rsNatural = psNatural.executeQuery()) {
                            if (rsNatural.next()) {
                                String nombre = rsNatural.getString("nombre");
                                String apellidos = rsNatural.getString("apellidos");
                                String dni = rsNatural.getString("dni");
                                DTOClienteNatural natural = new DTOClienteNatural(nombre, apellidos, dni, idCli, tipoCliente, direccion, correo);
                                lista.add(natural);
                            }
                        }
                    }
                } else if (tipoCliente.equalsIgnoreCase("empresa")) {
                    String sqlEmpresa = "SELECT * FROM cliente_empresa WHERE idCli = ?";
                    try (PreparedStatement psEmpresa = con.prepareStatement(sqlEmpresa)) {
                        psEmpresa.setInt(1, idCli);
                        try (ResultSet rsEmpresa = psEmpresa.executeQuery()) {
                            if (rsEmpresa.next()) {
                                String ruc = rsEmpresa.getString("ruc");
                                String razonSocial = rsEmpresa.getString("razon_social");
                                DTOClienteEmpresa empresa = new DTOClienteEmpresa(ruc, razonSocial, idCli, tipoCliente, direccion, correo);
                                lista.add(empresa);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    @Override
    public void borrar(DTOCliente oCliente) {

        String sqlNatural = "DELETE FROM cliente_natural WHERE idCli = ?";
        String sqlEmpresa = "DELETE FROM cliente_empresa WHERE idCli = ?";
        String sqlCliente = "DELETE FROM cliente WHERE idCli = ?";
        try {
            con = cn.conexion();
            con.setAutoCommit(false);

            if (oCliente instanceof DTOClienteNatural) {
                ps = con.prepareStatement(sqlNatural);
                ps.setInt(1, oCliente.getIdCli());
                ps.executeUpdate();
                ps.close(); 
            } else if (oCliente instanceof DTOClienteEmpresa) {
                ps = con.prepareStatement(sqlEmpresa);
                ps.setInt(1, oCliente.getIdCli());
                ps.executeUpdate();
                ps.close(); 
            }

            ps = con.prepareStatement(sqlCliente);
            ps.setInt(1, oCliente.getIdCli());
            ps.executeUpdate();

            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actualizar(DTOCliente oCliente) {
        String sqlCliente = "UPDATE cliente SET tipo_cliente = ?, direccion = ?, correo = ? WHERE idCli = ?";
        try {
            con = cn.conexion();
            con.setAutoCommit(false);
            ps = con.prepareStatement(sqlCliente);
            ps.setString(1, oCliente.getTipoCliente());
            ps.setString(2, oCliente.getDireccion());
            ps.setString(3, oCliente.getCorreo());
            ps.setInt(4, oCliente.getIdCli());
            ps.executeUpdate();

            if (oCliente instanceof DTOClienteNatural) {
                DTOClienteNatural natural = (DTOClienteNatural) oCliente;
                String sqlNatural = "UPDATE cliente_natural SET nombre = ?, apellidos = ?, dni = ? WHERE idCli = ?";
                ps = con.prepareStatement(sqlNatural);
                ps.setString(1, natural.getNombre());
                ps.setString(2, natural.getApellidos());
                ps.setString(3, natural.getDni());
                ps.setInt(4, natural.getIdCli());
                ps.executeUpdate();
            } else if (oCliente instanceof DTOClienteEmpresa) {
                DTOClienteEmpresa empresa = (DTOClienteEmpresa) oCliente;
                String sqlEmpresa = "UPDATE cliente_empresa SET ruc = ?, razon_social = ? WHERE idCli = ?";
                ps = con.prepareStatement(sqlEmpresa);
                ps.setString(1, empresa.getRuc());
                ps.setString(2, empresa.getRazonSocial());
                ps.setInt(3, empresa.getIdCli());
                ps.executeUpdate();
            }

            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public DTOCliente verificarCliente(String dniOrRuc) {
        String sqlNatural = "SELECT c.idCli, n.nombre, c.direccion, c.correo FROM cliente_natural n JOIN cliente c ON n.idCli = c.idCli WHERE n.dni = ?";
        String sqlEmpresa = "SELECT c.idCli, e.razon_social, c.direccion, c.correo FROM cliente_empresa e JOIN cliente c ON e.idCli = c.idCli WHERE e.ruc = ?";
        DTOCliente cliente = null;
        
        try {
            con = cn.conexion();
            if (dniOrRuc != null && !dniOrRuc.isEmpty()) {
                // Verificar como cliente natural
                ps = con.prepareStatement(sqlNatural);
                ps.setString(1, dniOrRuc);
                rs = ps.executeQuery();
                if (rs.next()) {
                    int idCli = rs.getInt("idCli");
                    String nombre = rs.getString("nombre");
                    String direccion = rs.getString("direccion");
                    String correo = rs.getString("correo");
                    cliente = new DTOClienteNatural(nombre, "", dniOrRuc, idCli, "natural", direccion, correo);
                }
                
                rs.close();
                ps.close();
                if (cliente == null) {
                    ps = con.prepareStatement(sqlEmpresa);
                    ps.setString(1, dniOrRuc);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        int idCli = rs.getInt("idCli");
                        String razonSocial = rs.getString("razon_social");
                        String direccion = rs.getString("direccion");
                        String correo = rs.getString("correo");
                        cliente = new DTOClienteEmpresa(dniOrRuc, razonSocial, idCli, "empresa", direccion, correo);
                    }
                }
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
        return cliente;
    }
}

