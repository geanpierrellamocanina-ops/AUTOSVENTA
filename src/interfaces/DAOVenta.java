/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import modelo.DTOCliente;
import modelo.DTOVehiculo;
import modelo.DTOVenta;

/**
 *
 * @author JE
 */
public interface DAOVenta {
    public int registrarVenta(DTOVenta venta);
}
