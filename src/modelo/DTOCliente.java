
package modelo;


public class DTOCliente {
    int idCli;
    String tipoCliente;
    String direccion;
    String correo;

    public DTOCliente() {
    }

    public DTOCliente(int idCli, String tipoCliente, String direccion, String correo) {
        this.idCli = idCli;
        this.tipoCliente = tipoCliente;
        this.direccion = direccion;
        this.correo = correo;
    }

    public int getIdCli() {
        return idCli;
    }

    public void setIdCli(int idCli) {
        this.idCli = idCli;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    
}
