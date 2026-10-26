package controlador;

import modelo.DAOClienteImpl;
import modelo.DTOCliente;
import modelo.DTOClienteEmpresa;
import modelo.DTOClienteNatural;
import vista.clientesVISTA;
import vista.inicioVISTA;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @autor JE
 */
public class ClienteControlador {
    private clientesVISTA view;
    private DAOClienteImpl clienteDAO;
    private DefaultTableModel modelo;

    public ClienteControlador(clientesVISTA view) {
        this.view = view;
        this.clienteDAO = new DAOClienteImpl();
        this.modelo = new DefaultTableModel();
        initController();
    }
    
    private void initController() {
        this.view.getBtnListar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarClientes();
            }
        });

        this.view.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grabarCliente();
            }
        });

        this.view.getBtnBorrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrarCliente();
            }
        });

        this.view.getComboBoxTipo().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent evt) {
                tipoClienteEvento(evt);
            }
        });

        this.view.getButtonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresarInicio();
            }
        });

        this.view.getBtnModificar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCliente();
            }
        });

        this.view.getTablaClientes().addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaClientesMouseClicked(evt);
            }
        });
    }

    private void listarClientes() {
        List<DTOCliente> listarClientes = clienteDAO.leer();
        modelo = (DefaultTableModel) view.getTablaClientes().getModel();
        modelo.setRowCount(0);

        Object[] ob = new Object[9]; 
        for (DTOCliente cliente : listarClientes) {
            ob[0] = cliente.getIdCli();
            ob[1] = cliente.getTipoCliente();
            ob[2] = cliente.getDireccion();
            ob[3] = cliente.getCorreo();

            if (cliente instanceof DTOClienteNatural) {
                DTOClienteNatural natural = (DTOClienteNatural) cliente;
                ob[4] = natural.getNombre();
                ob[5] = natural.getApellidos();
                ob[6] = natural.getDni();
                ob[7] = null; 
                ob[8] = null; 
            } else if (cliente instanceof DTOClienteEmpresa) {
                DTOClienteEmpresa empresa = (DTOClienteEmpresa) cliente;
                ob[4] = null; 
                ob[5] = null; 
                ob[6] = null; 
                ob[7] = empresa.getRuc();
                ob[8] = empresa.getRazonSocial();
            }

            modelo.addRow(ob);
        }
        view.getTablaClientes().setModel(modelo);
    }

    private void tablaClientesMouseClicked(java.awt.event.MouseEvent evt) {
        int filaSeleccionada = view.getTablaClientes().getSelectedRow();
        if (filaSeleccionada >= 0) {
            view.getTxtDni().setText(view.getTablaClientes().getValueAt(filaSeleccionada, 6) != null ? view.getTablaClientes().getValueAt(filaSeleccionada, 6).toString() : "");
            view.getTxtNombreCliente().setText(view.getTablaClientes().getValueAt(filaSeleccionada, 4) != null ? view.getTablaClientes().getValueAt(filaSeleccionada, 4).toString() : "");
            view.getTxtApellidosCliente().setText(view.getTablaClientes().getValueAt(filaSeleccionada, 5) != null ? view.getTablaClientes().getValueAt(filaSeleccionada, 5).toString() : "");
            view.getTxtDireccionCliente().setText(view.getTablaClientes().getValueAt(filaSeleccionada, 2).toString());
            view.getTxtCorreoCliente().setText(view.getTablaClientes().getValueAt(filaSeleccionada, 3).toString());
            view.getTxtRazonSocial().setText(view.getTablaClientes().getValueAt(filaSeleccionada, 8) != null ? view.getTablaClientes().getValueAt(filaSeleccionada, 8).toString() : "");
            view.getTxtRucCliente().setText(view.getTablaClientes().getValueAt(filaSeleccionada, 7) != null ? view.getTablaClientes().getValueAt(filaSeleccionada, 7).toString() : "");

            String tipo = view.getTablaClientes().getValueAt(filaSeleccionada, 1).toString();
            view.getComboBoxTipo().setSelectedItem(tipo);
        }
    }

    private void borrarCliente() {
        int filaSeleccionada = view.getTablaClientes().getSelectedRow();
        if (filaSeleccionada != -1) {
            int idCli = (int) modelo.getValueAt(filaSeleccionada, 0);
            String tipoCliente = (String) modelo.getValueAt(filaSeleccionada, 1);

            DTOCliente cliente = null;
            if ("natural".equalsIgnoreCase(tipoCliente)) {
                String nombre = (String) modelo.getValueAt(filaSeleccionada, 4);
                String apellidos = (String) modelo.getValueAt(filaSeleccionada, 5);
                String dni = (String) modelo.getValueAt(filaSeleccionada, 6);
                cliente = new DTOClienteNatural(nombre, apellidos, dni, idCli, tipoCliente, null, null);
            } else if ("empresa".equalsIgnoreCase(tipoCliente)) {
                String ruc = (String) modelo.getValueAt(filaSeleccionada, 7);
                String razonSocial = (String) modelo.getValueAt(filaSeleccionada, 8);
                cliente = new DTOClienteEmpresa(ruc, razonSocial, idCli, tipoCliente, null, null);
            }

            if (cliente != null) {
                clienteDAO.borrar(cliente);
                modelo.removeRow(filaSeleccionada); 
            }
        } else {
            JOptionPane.showMessageDialog(view, "Seleccione un cliente para borrar.");
        }
    }

    private void grabarCliente() {
        String tipoCliente = view.getComboBoxTipo().getSelectedItem().toString();
        DTOCliente cliente = null;

        if (validarCampos()) {
            if (tipoCliente.equalsIgnoreCase("Natural")) {
                cliente = new DTOClienteNatural(view.getTxtNombreCliente().getText(), 
                                                view.getTxtApellidosCliente().getText(),
                                                view.getTxtDni().getText(), 
                                                0, 
                                                tipoCliente, 
                                                view.getTxtDireccionCliente().getText(),
                                                view.getTxtCorreoCliente().getText());
            } else if (tipoCliente.equalsIgnoreCase("Empresa")) {
                cliente = new DTOClienteEmpresa(view.getTxtRucCliente().getText(), 
                                                view.getTxtRazonSocial().getText(), 
                                                0, 
                                                tipoCliente, 
                                                view.getTxtDireccionCliente().getText(), 
                                                view.getTxtCorreoCliente().getText());
            }

            if (cliente != null) {
                clienteDAO.grabar(cliente);
                JOptionPane.showMessageDialog(view, "Cliente agregado.");
                limpiarCasillas();
                listarClientes();
            }
        }
    }

    private void tipoClienteEvento(ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (view.getComboBoxTipo().getSelectedItem().equals("Natural")) {
                view.getTxtRucCliente().setEnabled(false);
                view.getTxtRazonSocial().setEnabled(false);
                view.getTxtRucCliente().setText("");
                view.getTxtRazonSocial().setText("");
                view.getTxtNombreCliente().setEnabled(true);
                view.getTxtApellidosCliente().setEnabled(true);
                view.getTxtDni().setEnabled(true);
            } else if (view.getComboBoxTipo().getSelectedItem().equals("Empresa")) {
                view.getTxtRucCliente().setEnabled(true);
                view.getTxtRazonSocial().setEnabled(true);
                view.getTxtNombreCliente().setEnabled(false);
                view.getTxtApellidosCliente().setEnabled(false);
                view.getTxtDni().setEnabled(false);
                view.getTxtNombreCliente().setText("");
                view.getTxtApellidosCliente().setText("");
                view.getTxtDni().setText("");
            }
        }
    }

    private void actualizarCliente() {
        int filaSeleccionada = view.getTablaClientes().getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar un cliente");
            return;
        }

        if (validarCampos()) {
            String tipoCliente = view.getComboBoxTipo().getSelectedItem().toString();
            DTOCliente cliente = null;
            int idCli = Integer.parseInt(view.getTablaClientes().getValueAt(filaSeleccionada, 0).toString());

            if (tipoCliente.equalsIgnoreCase("Natural")) {
                cliente = new DTOClienteNatural();
                ((DTOClienteNatural) cliente).setNombre(view.getTxtNombreCliente().getText().trim());
                ((DTOClienteNatural) cliente).setApellidos(view.getTxtApellidosCliente().getText().trim());
                ((DTOClienteNatural) cliente).setDni(view.getTxtDni().getText().trim());
            } else if (tipoCliente.equalsIgnoreCase("Empresa")) {
                cliente = new DTOClienteEmpresa();
                ((DTOClienteEmpresa) cliente).setRuc(view.getTxtRucCliente().getText().trim());
                ((DTOClienteEmpresa) cliente).setRazonSocial(view.getTxtRazonSocial().getText().trim());
            }

            cliente.setIdCli(idCli);
            cliente.setTipoCliente(tipoCliente);
            cliente.setDireccion(view.getTxtDireccionCliente().getText().trim());
            cliente.setCorreo(view.getTxtCorreoCliente().getText().trim());

            try {
                clienteDAO.actualizar(cliente);
                JOptionPane.showMessageDialog(view, "Cliente actualizado");
                limpiarCasillas();
                listarClientes();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "No se pudo actualizar el cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarCampos() {
        String tipoCliente = view.getComboBoxTipo().getSelectedItem().toString();
        boolean valid = false;

        if (tipoCliente.equalsIgnoreCase("Natural")) {
            valid = !view.getTxtNombreCliente().getText().trim().isEmpty() &&
                    !view.getTxtApellidosCliente().getText().trim().isEmpty() &&
                    !view.getTxtDni().getText().trim().isEmpty() &&
                    !view.getTxtDireccionCliente().getText().trim().isEmpty() &&
                    !view.getTxtCorreoCliente().getText().trim().isEmpty();
        } else if (tipoCliente.equalsIgnoreCase("Empresa")) {
            valid = !view.getTxtRucCliente().getText().trim().isEmpty() &&
                    !view.getTxtRazonSocial().getText().trim().isEmpty() &&
                    !view.getTxtDireccionCliente().getText().trim().isEmpty() &&
                    !view.getTxtCorreoCliente().getText().trim().isEmpty();
        }

        if (!valid) {
            JOptionPane.showMessageDialog(view, "Complete todos los campos.", "Campos Incompletos", JOptionPane.ERROR_MESSAGE);
        }

        return valid;
    }

    private void limpiarCasillas() {
        view.getTxtDni().setText("");
        view.getTxtNombreCliente().setText("");
        view.getTxtApellidosCliente().setText("");
        view.getTxtDireccionCliente().setText("");
        view.getTxtCorreoCliente().setText("");
        view.getTxtRazonSocial().setText("");
        view.getTxtRucCliente().setText("");
        view.getComboBoxTipo().setSelectedIndex(0);
    }

    private void regresarInicio() {
        this.view.setVisible(false);
        inicioVISTA inicioVista = new inicioVISTA();
        InicioControlador inicioControlador = new InicioControlador(inicioVista);
        inicioVista.setVisible(true);
    }
}
