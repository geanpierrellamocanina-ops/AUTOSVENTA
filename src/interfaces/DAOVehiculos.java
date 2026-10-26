/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import modelo.DTOVehiculo;

/**
 *
 * @author JE
 */
public interface DAOVehiculos {
    /**
     * Graba un vehículo. Retorna null si fue exitoso, o el mensaje de error si falló.
     */
    public String grabar(DTOVehiculo oVehiculo);
    public boolean actualizar(DTOVehiculo oVehiculo);
    public List<DTOVehiculo> leer();
    public boolean borrar(DTOVehiculo oVehiculo);
    public DTOVehiculo verificarVehiculo(DTOVehiculo oVehiculo);
}
