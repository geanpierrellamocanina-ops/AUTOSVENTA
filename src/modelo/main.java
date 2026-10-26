package modelo;

import controlador.InicioControlador;
import controlador.LoginControlador;
import javax.swing.SwingUtilities;
import vista.clientesVISTA;
import vista.inicioVISTA;
import vista.loginVISTA;

/**
 *
 * @author J 
 */
public class main {

    public static void main(String[] args) {
        loginVISTA loginVista = new loginVISTA();
        LoginControlador loginControlador = new LoginControlador(loginVista);
        loginVista.setVisible(true);
    }
}
