/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import modelo.DTOEmpleado;

/**
 *
 * @author JE
 */
public interface DAOEmpleado {
    public int grabar(DTOEmpleado obj);

    public int actualizar(DTOEmpleado obj);

    public List<DTOEmpleado> leer();

    public int borrar(int id);
    
    public boolean IniciarSesionEmpleado(DTOEmpleado obj);
    
}
