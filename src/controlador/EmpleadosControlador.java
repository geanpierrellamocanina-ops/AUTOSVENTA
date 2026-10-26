package controlador;

import modelo.DAOEmpleadoImpl;
import modelo.DTOEmpleado;
import vista.empleadosVISTA;
import vista.inicioVISTA;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class EmpleadosControlador {
    private empleadosVISTA view;
    private DAOEmpleadoImpl usuarioDAO;
    private DefaultTableModel modelo;
    private int id;

    public EmpleadosControlador(empleadosVISTA view) {
        this.view = view;
        this.usuarioDAO = new DAOEmpleadoImpl();
        this.modelo = new DefaultTableModel();
        this.id = 0;
        initController();
    }
    
    private void initController() {
        this.view.getJbtListar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarUsuarios();
            }
        });

        this.view.getJbtGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grabarUsuario();
            }
        });

        this.view.getJbtEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrarUsuario();
            }
        });

        this.view.getJbtModificar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarUsuario();
            }
        });

        this.view.getButtonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresarInicio();
            }
        });

        this.view.getTablaEmpleados().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEmpleadosMouseClicked(evt);
            }
        });
    }

    private void listarUsuarios() {
        List<DTOEmpleado> lista = usuarioDAO.leer();
        modelo = (DefaultTableModel) view.getTablaEmpleados().getModel();
        modelo.setRowCount(0);

        Object[] ob = new Object[6];
        for (DTOEmpleado usuario : lista) {
            ob[0] = usuario.getId();
            ob[1] = usuario.getNombre();
            ob[2] = usuario.getApellido();
            ob[3] = usuario.getDni();
            ob[4] = usuario.getTelefono();
            ob[5] = usuario.getCorreo();
            modelo.addRow(ob);
        }
        view.getTablaEmpleados().setModel(modelo);
    }

    private void grabarUsuario() {
        if (validarCampos()) {
            DTOEmpleado dto = new DTOEmpleado();
            dto.setNombre(view.getTxtNombre().getText().trim());
            dto.setApellido(view.getTxtApellido().getText().trim());
            dto.setDni(view.getTxtDni().getText().trim());
            dto.setCorreo(view.getTxtCorreo().getText().trim());
            dto.setTelefono(view.getTxtTelefono().getText().trim());

            if (usuarioDAO.grabar(dto) > 0) {
                JOptionPane.showMessageDialog(view, "Usuario registrado!");
                limpiarCasillas();
                listarUsuarios();
            } else {
                JOptionPane.showMessageDialog(view, "No se pudo registrar usuario!");
            }
        }
    }

    private void borrarUsuario() {
        int filaSeleccionada = view.getTablaEmpleados().getSelectedRow();
        if (filaSeleccionada != -1) {
            int id = (int) modelo.getValueAt(filaSeleccionada, 0);
            if (usuarioDAO.borrar(id) > 0) {
                JOptionPane.showMessageDialog(view, "Usuario eliminado!");
                listarUsuarios();
            } else {
                JOptionPane.showMessageDialog(view, "No se pudo eliminar usuario!");
            }
        } else {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una fila a eliminar!");
        }
    }

    private void actualizarUsuario() {
        int filaSeleccionada = view.getTablaEmpleados().getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una fila a actualizar!");
            return;
        }

        if (validarCampos()) {
            DTOEmpleado dto = new DTOEmpleado();
            dto.setId(id);
            dto.setNombre(view.getTxtNombre().getText().trim());
            dto.setApellido(view.getTxtApellido().getText().trim());
            dto.setDni(view.getTxtDni().getText().trim());
            dto.setCorreo(view.getTxtCorreo().getText().trim());
            dto.setTelefono(view.getTxtTelefono().getText().trim());

            if (usuarioDAO.actualizar(dto) > 0) {
                JOptionPane.showMessageDialog(view, "Usuario actualizado!");
                limpiarCasillas();
                listarUsuarios();
            } else {
                JOptionPane.showMessageDialog(view, "No se pudo actualizar usuario!");
            }
        }
    }

    private void tablaEmpleadosMouseClicked(java.awt.event.MouseEvent evt) {
        int filaSeleccionada = view.getTablaEmpleados().getSelectedRow();
        if (filaSeleccionada >= 0) {
            id = Integer.parseInt(view.getTablaEmpleados().getValueAt(filaSeleccionada, 0).toString());
            view.getTxtNombre().setText(view.getTablaEmpleados().getValueAt(filaSeleccionada, 1).toString());
            view.getTxtApellido().setText(view.getTablaEmpleados().getValueAt(filaSeleccionada, 2).toString());
            view.getTxtDni().setText(view.getTablaEmpleados().getValueAt(filaSeleccionada, 3).toString());
            view.getTxtTelefono().setText(view.getTablaEmpleados().getValueAt(filaSeleccionada, 4).toString());
            view.getTxtCorreo().setText(view.getTablaEmpleados().getValueAt(filaSeleccionada, 5).toString());
        }
    }

    private boolean validarCampos() {
        if (view.getTxtNombre().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "El campo nombre es requerido.!");
            return false;
        }
        if (view.getTxtApellido().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "El campo apellido es requerido.!");
            return false;
        }
        if (view.getTxtDni().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "El campo DNI es requerido.!");
            return false;
        }
        if (view.getTxtTelefono().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "El campo telefono es requerido.!");
            return false;
        }
        if (view.getTxtCorreo().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "El campo correo es requerido.!");
            return false;
        }
        return true;
    }

    private void limpiarCasillas() {
        view.getTxtNombre().setText("");
        view.getTxtApellido().setText("");
        view.getTxtDni().setText("");
        view.getTxtCorreo().setText("");
        view.getTxtTelefono().setText("");
        id = 0;
    }

    private void regresarInicio() {
        this.view.setVisible(false);
        inicioVISTA inicioVista = new inicioVISTA();
        InicioControlador inicioControlador = new InicioControlador(inicioVista);
        inicioVista.setVisible(true);
    }
}
