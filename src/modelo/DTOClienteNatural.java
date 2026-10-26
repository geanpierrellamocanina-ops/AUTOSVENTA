package modelo;

/**
 *
 * @author JE
 */
public class DTOClienteNatural extends DTOCliente {
    String nombre;
    String apellidos;
    String dni;

    public DTOClienteNatural() {
    }

    public DTOClienteNatural(String nombre, String apellidos, String dni, int idCli, String tipoCliente, String direccion, String correo) {
        super(idCli, tipoCliente, direccion, correo);
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
    
    
    
}
