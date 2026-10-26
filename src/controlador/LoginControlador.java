package controlador;

import modelo.DAOEmpleadoImpl;
import modelo.DTOEmpleado;
import vista.loginVISTA;
import vista.inicioVISTA;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class LoginControlador {
    private loginVISTA vista;
    private DAOEmpleadoImpl empleadoDAO;

    public LoginControlador(loginVISTA vista) {
        this.vista = vista;
        this.empleadoDAO = new DAOEmpleadoImpl();
        
        // Inicializar controladores
        this.vista.getBtnIngresar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarCredenciales();
            }
        });
    }

    private void validarCredenciales() {
        String correo = vista.getTxtCorreo().getText().trim();
        String contrasena = new String(vista.getTxtPass().getPassword()).trim();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Todos los campos son requeridos!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DTOEmpleado empleado = new DTOEmpleado();
        empleado.setCorreo(correo);
        empleado.setPass(contrasena);

        boolean validado = empleadoDAO.IniciarSesionEmpleado(empleado);
        if (validado) {
            vista.setVisible(false);
            inicioVISTA inicioVista = new inicioVISTA();
            InicioControlador inicioControlador = new InicioControlador(inicioVista);
            inicioVista.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(vista, "Informacion incorrectas!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
