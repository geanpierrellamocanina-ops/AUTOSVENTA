package controlador;

import vista.marcaVISTA;
import modelo.DAOMarcaImpl;
import modelo.DTOMarca;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import vista.inicioVISTA;

public class MarcaControlador {
    private marcaVISTA view;
    private DAOMarcaImpl marcaDAO;
    private DefaultTableModel modelo;
    private int id;

    public MarcaControlador(marcaVISTA view) {
        this.view = view;
        this.marcaDAO = new DAOMarcaImpl();
        this.modelo = new DefaultTableModel();
        this.id = 0;

        // Inicializar controladores
        this.view.getBtnLeer().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarMarcas();
            }
        });

        this.view.getBtnCrear().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grabarMarca();
            }
        });

        this.view.getBtnBorrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrarMarca();
            }
        });

        this.view.getBtnActualizar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarMarca();
            }
        });

        this.view.getButtonATRAS().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresarInicio();
            }
        });

        this.view.getTablaMarca().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMarcasMouseClicked(evt);
            }
        });
    }

    private void listarMarcas() {
        List<DTOMarca> lista = marcaDAO.leer();
        modelo = (DefaultTableModel) view.getTablaMarca().getModel();
        modelo.setRowCount(0);

        Object[] ob = new Object[2];
        for (DTOMarca marca : lista) {
            ob[0] = marca.getIdMarca();
            ob[1] = marca.getMarca();
            modelo.addRow(ob);
        }
        view.getTablaMarca().setModel(modelo);
    }

    private void borrarMarca() {
        int filaSeleccionada = view.getTablaMarca().getSelectedRow();
        if (filaSeleccionada != -1) {
            int id = (int) modelo.getValueAt(filaSeleccionada, 0);
            DTOMarca dtoMarca = new DTOMarca();
            dtoMarca.setIdMarca(id);
            if (marcaDAO.borrar(dtoMarca)) {
                JOptionPane.showMessageDialog(view, "Marca eliminada");
                listarMarcas();
            } else {
                JOptionPane.showMessageDialog(view, "No se pudo eliminar la marca");
            }
        } else {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una fila a eliminar");
        }
    }

    private void grabarMarca() {
        if (validarCampos()) {
            DTOMarca dto = new DTOMarca();
            dto.setMarca(view.getTxtMarca().getText().trim());

            if (marcaDAO.grabar(dto)) {
                JOptionPane.showMessageDialog(view, "Marca registrada!");
                limpiarCasillas();
                listarMarcas();
            } else {
                JOptionPane.showMessageDialog(view, "No se pudo registrar la marca");
            }
        }
    }

    private void actualizarMarca() {
        int filaSeleccionada = view.getTablaMarca().getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una marca a actualizar");
            return;
        }

        if (validarCampos()) {
            DTOMarca dto = new DTOMarca();
            dto.setIdMarca(id);
            dto.setMarca(view.getTxtMarca().getText().trim());

            if (marcaDAO.actualizar(dto)) {
                JOptionPane.showMessageDialog(view, "Marca actualizada!");
                limpiarCasillas();
                listarMarcas();
            } else {
                JOptionPane.showMessageDialog(view, "No se pudo actualizar la marca!");
            }
        }
    }

    private void tablaMarcasMouseClicked(java.awt.event.MouseEvent evt) {
        int filaSeleccionada = view.getTablaMarca().getSelectedRow();
        if (filaSeleccionada >= 0) {
            id = Integer.parseInt(view.getTablaMarca().getValueAt(filaSeleccionada, 0).toString());
            view.getTxtMarca().setText(view.getTablaMarca().getValueAt(filaSeleccionada, 1).toString());
        }
    }

    private boolean validarCampos() {
        if (view.getTxtMarca().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "El campo marca es requerido.");
            return false;
        }
        return true;
    }

    private void limpiarCasillas() {
        view.getTxtMarca().setText("");
        id = 0;
    }

    private void regresarInicio() {
        this.view.setVisible(false);
        inicioVISTA inicioVista = new inicioVISTA();
        InicioControlador inicioControlador = new InicioControlador(inicioVista);
        inicioVista.setVisible(true);
    }
}
