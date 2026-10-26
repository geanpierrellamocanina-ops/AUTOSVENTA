
package interfaces;

import modelo.DTOCliente;
import java.util.List;

/**
 *
 * @author JE
 */
public interface DAOCliente {
    public void grabar(DTOCliente oCliente);
    public void actualizar(DTOCliente oCliente);
    public List<DTOCliente> leer();
    public void borrar(DTOCliente oCliente);
    public DTOCliente verificarCliente(String dniOrRuc) ;
}
