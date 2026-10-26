/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import modelo.DTOMarca;

/**
 *
 * @author JE
 */
public interface DAOMarca {
    public boolean grabar(DTOMarca oMarca);
    public boolean actualizar(DTOMarca oMarca);
    public List<DTOMarca> leer();
    public boolean borrar(DTOMarca oMarca);
    public DTOMarca buscarPorId(DTOMarca oMarca);
}
