package controlador;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import modelo.DAOMarcaImpl;
import modelo.DAOVehiculosImp;
import modelo.DTOMarca;
import modelo.DTOVehiculo;
import modelo.DTOEmpleado;
import modelo.ImageRenderer;
import vista.inicioVISTA;
import vista.vehiculosVISTA;
import java.util.List;

public class VehiculosControlador {

    private vehiculosVISTA view;
    private DAOMarcaImpl marcaDAO;
    private DAOVehiculosImp vehiculoDAO;
    private DefaultTableModel modelo;
    // Almacena los bytes de la imagen seleccionada por el usuario (null si no eligió ninguna)
    private byte[] imagenSeleccionada = null;
    // Tamaño máximo permitido: 800 KB (margen seguro)
    private static final long MAX_IMAGEN_BYTES = 800 * 1024; // 800 KB
    private static final String MAX_IMAGEN_STR = "800 KB";

    public VehiculosControlador(vehiculosVISTA view) {
        this.view = view;
        this.marcaDAO = new DAOMarcaImpl();
        this.vehiculoDAO = new DAOVehiculosImp();
        this.modelo = new DefaultTableModel();

        cargarMarcas(view);

        // Inicializar controladores de eventos
        this.view.getJbtListar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarVehiculos();
            }
        });

        this.view.getJbtGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grabarVehiculo();
            }
        });

        this.view.getJbtEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrarVehiculo();
            }
        });

        this.view.getJbtModificar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarVehiculo();
            }
        });

        this.view.getButtonAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresarInicio();
            }
        });
        this.view.getTablaVehiculos().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaVehiculosMouseClicked(evt);
            }
        });
        this.view.getBtnFiltro().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarVehiculos();
            }
        });

        this.view.getJbtCargarImagen().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarImagen();
            }
        });
    }

    /**
     * Abre un JFileChooser para que el usuario elija una imagen. Los bytes se
     * guardan en imagenSeleccionada; si cancela, queda null.
     */
    private void seleccionarImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar imagen del vehículo");
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(
                "Imágenes (jpg, png, gif, bmp)", "jpg", "jpeg", "png", "gif", "bmp");
        chooser.setFileFilter(filtro);

        int resultado = chooser.showOpenDialog(view);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();

            // Validar tamaño antes de leer
            if (archivo.length() > MAX_IMAGEN_BYTES) {
                long tamanioKB = archivo.length() / 1024;
                JOptionPane.showMessageDialog(view,
                        "La imagen es demasiado grande (" + tamanioKB + " KB).\n"
                        + "El tamaño máximo permitido es " + MAX_IMAGEN_STR + ".\n"
                        + "Por favor seleccione una imagen más pequeña.",
                        "Imagen muy grande", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (FileInputStream fis = new FileInputStream(archivo)) {
                imagenSeleccionada = fis.readAllBytes();
                view.getLblRutaImagen().setText(archivo.getName());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "No se pudo leer la imagen seleccionada.");
                imagenSeleccionada = null;
                view.getLblRutaImagen().setText("Sin imagen seleccionada");
            }
        }
    }

    public void cargarMarcas(vehiculosVISTA vehiculosView) {
        List<DTOMarca> lista = marcaDAO.leer();
        DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>();

        for (DTOMarca marca : lista) {
            modeloCombo.addElement(marca.getMarca());
        }

        vehiculosView.getjComboBoxMarca().setModel(modeloCombo);
    }

    public void listarVehiculos() {
        List<DTOVehiculo> lista = vehiculoDAO.leer();
        modelo = (DefaultTableModel) view.getTablaVehiculos().getModel();
        modelo.setRowCount(0);

        Object[] ob = new Object[9];
        for (DTOVehiculo vehiculo : lista) {
            ob[0] = vehiculo.getIdvehiculo();
            ob[1] = vehiculo.getCodigovehiculo();
            ob[2] = obtenerNombreMarca(vehiculo.getMarca());
            ob[3] = vehiculo.getModelo();
            ob[4] = vehiculo.getColor();
            ob[5] = vehiculo.getCategoria();
            ob[6] = vehiculo.getAño();
            ob[7] = vehiculo.getCosto();

            // Obtener la imagen del vehículo y convertirla a un ImageIcon
            byte[] imgBytes = vehiculo.getImagenVehiculo();
            if (imgBytes != null) {
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes);
                    BufferedImage img = ImageIO.read(bais);
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                    ob[8] = icon;
                } catch (Exception e) {
                    System.out.println("Error al cargar la imagen del ID: " + vehiculo.getIdvehiculo());
                    ob[8] = cargarImagenPorDefecto();

                }
            } else {
                ob[8] = cargarImagenPorDefecto();
            }

            modelo.addRow(ob);
        }
        view.getTablaVehiculos().setModel(modelo);

        // Configurar el renderizador para la columna de imagen
        view.getTablaVehiculos().getColumnModel().getColumn(8).setCellRenderer(new ImageRenderer());
        view.getTablaVehiculos().getColumnModel().getColumn(8).setPreferredWidth(50); // Ajustar el ancho de la columna de imagen
    }

    /**
     * Devuelve el ImageIcon de sin-imagen.jpg escalado a 50x50.
     */
    private ImageIcon cargarImagenPorDefecto() {
        try {
            URL url = getClass().getResource("/imagenes/sin-imagen.jpg");
            if (url != null) {
                BufferedImage img = ImageIO.read(url);
                return new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void tablaVehiculosMouseClicked(java.awt.event.MouseEvent evt) {
        int filaSeleccionada = view.getTablaVehiculos().getSelectedRow();
        if (filaSeleccionada >= 0) {
            int Idvehiculo = Integer.parseInt(view.getTablaVehiculos().getValueAt(filaSeleccionada, 0).toString());

            view.getTxtCodigoVehiculo().setText(view.getTablaVehiculos().getValueAt(filaSeleccionada, 1).toString());

            String marca = view.getTablaVehiculos().getValueAt(filaSeleccionada, 2).toString();
            view.getjComboBoxMarca().setSelectedItem(marca);

            view.getTxtModelo().setText(view.getTablaVehiculos().getValueAt(filaSeleccionada, 3).toString());
            view.getTxtColor().setText(view.getTablaVehiculos().getValueAt(filaSeleccionada, 4).toString());

            String categoria = view.getTablaVehiculos().getValueAt(filaSeleccionada, 5).toString();
            view.getJComboBoxCATEGORIA().setSelectedItem(categoria);

            view.getTxtAnio().setText(view.getTablaVehiculos().getValueAt(filaSeleccionada, 6).toString());
            view.getTxtCosto().setText(view.getTablaVehiculos().getValueAt(filaSeleccionada, 7).toString());
        }
    }

    private String obtenerNombreMarca(int idMarca) {
        DTOMarca oMarca = new DTOMarca();
        oMarca.setIdMarca(idMarca);
        DTOMarca marca = marcaDAO.buscarPorId(oMarca);
        return marca != null ? marca.getMarca() : "Desconocido";
    }

    private void grabarVehiculo() {
        if (validarCampos()) {
            DTOVehiculo dtoVehiculo = new DTOVehiculo();
            dtoVehiculo.setCodigovehiculo(view.getTxtCodigoVehiculo().getText());
            dtoVehiculo.setMarca(obtenerIdMarca(view.getjComboBoxMarca().getSelectedItem().toString()));
            dtoVehiculo.setModelo(view.getTxtModelo().getText());
            dtoVehiculo.setColor(view.getTxtColor().getText());
            dtoVehiculo.setCategoria(view.getJComboBoxCATEGORIA().getSelectedItem().toString());
            dtoVehiculo.setAño(view.getTxtAnio().getText());
            dtoVehiculo.setCosto(Float.parseFloat(view.getTxtCosto().getText()));
            // Imagen: null si el usuario no seleccionó ninguna
            dtoVehiculo.setImagenVehiculo(imagenSeleccionada);

            String errorMsg = vehiculoDAO.grabar(dtoVehiculo);
            if (errorMsg == null) {
                JOptionPane.showMessageDialog(view, "Vehículo registrado!");
                imagenSeleccionada = null;
                view.getLblRutaImagen().setText("Sin imagen seleccionada");
                limpiarCampos();
                listarVehiculos();
            } else {
                JOptionPane.showMessageDialog(view,
                        "No se pudo registrar el vehículo:\n" + errorMsg,
                        "Error al guardar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarVehiculo() {
        int filaSeleccionada = view.getTablaVehiculos().getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una fila a actualizar!");
            return;
        }

        if (validarCampos()) {
            DTOVehiculo dtoVehiculo = new DTOVehiculo();
            dtoVehiculo.setIdvehiculo(Integer.parseInt(view.getTablaVehiculos().getValueAt(filaSeleccionada, 0).toString()));
            dtoVehiculo.setCodigovehiculo(view.getTxtCodigoVehiculo().getText());
            dtoVehiculo.setMarca(obtenerIdMarca(view.getjComboBoxMarca().getSelectedItem().toString()));
            dtoVehiculo.setModelo(view.getTxtModelo().getText());
            dtoVehiculo.setColor(view.getTxtColor().getText());
            dtoVehiculo.setCategoria(view.getJComboBoxCATEGORIA().getSelectedItem().toString());
            dtoVehiculo.setAño(view.getTxtAnio().getText());
            dtoVehiculo.setCosto(Float.parseFloat(view.getTxtCosto().getText()));

            if (vehiculoDAO.actualizar(dtoVehiculo)) {
                JOptionPane.showMessageDialog(view, "Vehículo actualizado!");
                limpiarCampos();
                listarVehiculos();
            } else {
                JOptionPane.showMessageDialog(view, "No se pudo actualizar el vehículo!");
            }
        }
    }

    private boolean validarCampos() {
        if (view.getTxtCodigoVehiculo().getText().trim().isEmpty()
                || view.getTxtModelo().getText().trim().isEmpty()
                || view.getTxtColor().getText().trim().isEmpty()
                || view.getTxtAnio().getText().trim().isEmpty()
                || view.getTxtCosto().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Todos los campos son obligatorios!");
            return false;
        }
        return true;
    }

    private int obtenerIdMarca(String nombreMarca) {
        List<DTOMarca> lista = marcaDAO.leer();
        for (DTOMarca marca : lista) {
            if (marca.getMarca().equals(nombreMarca)) {
                return marca.getIdMarca();
            }
        }
        return -1;
    }

    private void limpiarCampos() {
        view.getTxtCodigoVehiculo().setText("");
        view.getjComboBoxMarca().setSelectedIndex(0);
        view.getTxtModelo().setText("");
        view.getTxtColor().setText("");
        view.getJComboBoxCATEGORIA().setSelectedIndex(0);
        view.getTxtAnio().setText("");
        view.getTxtCosto().setText("");
    }

    private void borrarVehiculo() {
        int filaSeleccionada = view.getTablaVehiculos().getSelectedRow();
        if (filaSeleccionada != -1) {
            int Idvehiculo = (int) modelo.getValueAt(filaSeleccionada, 0);
            DTOVehiculo dtoVehiculo = new DTOVehiculo();
            dtoVehiculo.setIdvehiculo(Idvehiculo);
            if (vehiculoDAO.borrar(dtoVehiculo)) {
                JOptionPane.showMessageDialog(view, "Vehiculo eliminada!");
                listarVehiculos();
            } else {
                JOptionPane.showMessageDialog(view, "No se pudo eliminar el Vehiculo!");
            }
        } else {
            JOptionPane.showMessageDialog(view, "Debe seleccionar una fila a eliminar!");
        }
    }

    private void regresarInicio() {
        this.view.setVisible(false);
        inicioVISTA inicioVista = new inicioVISTA();
        InicioControlador inicioControlador = new InicioControlador(inicioVista);
        inicioVista.setVisible(true);
    }

    private void filtrarVehiculos() {
        List<DTOVehiculo> lista = vehiculoDAO.leer();
        modelo = (DefaultTableModel) view.getTablaVehiculos().getModel();
        modelo.setRowCount(0);

        String codigo = view.getTxtCodigoVehiculo().getText().trim();
        String marca = (String) view.getjComboBoxMarca().getSelectedItem();
        String modeloVehiculo = view.getTxtModelo().getText().trim();
        String color = view.getTxtColor().getText().trim();
        String categoria = (String) view.getJComboBoxCATEGORIA().getSelectedItem();
        String anio = view.getTxtAnio().getText().trim();
        String costoStr = view.getTxtCosto().getText().trim();
        Float costo = costoStr.isEmpty() ? null : Float.parseFloat(costoStr);

        for (DTOVehiculo vehiculo : lista) {
            boolean coincide = true;

            if (!codigo.isEmpty() && !vehiculo.getCodigovehiculo().equalsIgnoreCase(codigo)) {
                coincide = false;
            }
            if (marca != null && !marca.equals("Seleccionar") && !obtenerNombreMarca(vehiculo.getMarca()).equalsIgnoreCase(marca)) {
                coincide = false;
            }
            if (!modeloVehiculo.isEmpty() && !vehiculo.getModelo().equalsIgnoreCase(modeloVehiculo)) {
                coincide = false;
            }
            if (!color.isEmpty() && !vehiculo.getColor().equalsIgnoreCase(color)) {
                coincide = false;
            }
            if (categoria != null && !categoria.equals("Seleccionar") && !vehiculo.getCategoria().equalsIgnoreCase(categoria)) {
                coincide = false;
            }
            if (!anio.isEmpty() && !vehiculo.getAño().equalsIgnoreCase(anio)) {
                coincide = false;
            }
            if (costo != null && !vehiculo.getCosto().equals(costo)) {
                coincide = false;
            }

            if (coincide) {
                Object[] ob = new Object[8];
                ob[0] = vehiculo.getIdvehiculo();
                ob[1] = vehiculo.getCodigovehiculo();
                ob[2] = obtenerNombreMarca(vehiculo.getMarca());
                ob[3] = vehiculo.getModelo();
                ob[4] = vehiculo.getColor();
                ob[5] = vehiculo.getCategoria();
                ob[6] = vehiculo.getAño();
                ob[7] = vehiculo.getCosto();
                modelo.addRow(ob);
            }
        }

        view.getTablaVehiculos().setModel(modelo);
    }
}
