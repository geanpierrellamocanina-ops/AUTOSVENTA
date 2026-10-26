package controlador;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.DAOClienteImpl;
import modelo.DAOMarcaImpl;
import modelo.DAOVehiculosImp;
import modelo.DAOVentaImpl;
import modelo.DTOCliente;
import modelo.DTOClienteEmpresa;
import modelo.DTOClienteNatural;
import modelo.DTODetalleComprobante;
import modelo.DTOMarca;
import modelo.DTOVehiculo;
import modelo.DTOVenta;
import vista.clientesVISTA;
import vista.inicioVISTA;
import vista.vehiculosVISTA;
import vista.ventaVISTA;

/**
 *
 * @autor JE
 */
public class VentaControlador {
    private ventaVISTA view;
    private DAOMarcaImpl marcaDAO;
    private DAOClienteImpl clienteDAO;
    private DAOVehiculosImp vehiculoDAO;
    private DAOVentaImpl ventaDAO;

    public VentaControlador(ventaVISTA view) {
        this.view = view;
        this.clienteDAO = new DAOClienteImpl();
        this.vehiculoDAO = new DAOVehiculosImp();
        this.marcaDAO = new DAOMarcaImpl();
        this.ventaDAO = new DAOVentaImpl();

        // Inicializar controladores
        this.view.getBtnBuscarCliente().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
            }
        });

        this.view.getBtnBuscarVehiculo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarVehiculo();
            }
        });

        this.view.getBtnComprar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarVenta();
            }
        });

        this.view.getBtnAtras().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresarInicio();
            }
        });
        this.view.getBtnEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarVehiculo();
            }
        });
    actualizarLabelUltimoID();

    }

    private void buscarCliente() {
        String dniOrRuc = view.getTxtDniRUC().getText().trim();
        if (!dniOrRuc.isEmpty()) {
            DTOCliente cliente = clienteDAO.verificarCliente(dniOrRuc);
            if (cliente != null) {
                if (cliente instanceof DTOClienteNatural) {
                    DTOClienteNatural natural = (DTOClienteNatural) cliente;
                    view.getTxtNombre().setText(natural.getNombre());
                    view.getTxtDireccion().setText(natural.getDireccion());
                    view.getTxtCorreo().setText(natural.getCorreo());
                } else if (cliente instanceof DTOClienteEmpresa) {
                    DTOClienteEmpresa empresa = (DTOClienteEmpresa) cliente;
                    view.getTxtNombre().setText(empresa.getRazonSocial());
                    view.getTxtDireccion().setText(empresa.getDireccion());
                    view.getTxtCorreo().setText(empresa.getCorreo());
                }
            } else {
                JOptionPane.showMessageDialog(view, "Cliente no encontrado. Regístrelo.");
                abrirVistaClientes();
            }
        } else {
            JOptionPane.showMessageDialog(view, "Ingrese un DNI o RUC válido.");
        }
    }

    private void buscarVehiculo() {
        String codigoVehiculo = view.getTxtCodigoVehiculo().getText().trim();
        if (!codigoVehiculo.isEmpty()) {
            DTOVehiculo vehiculo = vehiculoDAO.verificarVehiculo(new DTOVehiculo(codigoVehiculo));
            if (vehiculo != null) {
                String nombreMarca = obtenerNombreMarca(vehiculo.getMarca());
                DefaultTableModel model = (DefaultTableModel) view.getTablaVenta().getModel();
                model.addRow(new Object[]{vehiculo.getModelo(), nombreMarca, vehiculo.getColor(), vehiculo.getCodigovehiculo(), vehiculo.getCosto()});
                actualizarSubtotal();
            } else {
                JOptionPane.showMessageDialog(view, "Vehículo no encontrado.");
            }
        } else {
            abrirVistaVehiculos();
        }
    }

    private void abrirVistaVehiculos() {
        vehiculosVISTA vistaVehiculos = new vehiculosVISTA();
        VehiculosControlador vehiculoControlador = new VehiculosControlador(vistaVehiculos);
        vistaVehiculos.setVisible(true);
    }


    private void actualizarSubtotal() {
        DefaultTableModel model = (DefaultTableModel) view.getTablaVenta().getModel();
        float subtotal = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            subtotal += (float) model.getValueAt(i, 4);
        }

        view.getJLabelSubtotal().setText(String.format("%.2f", subtotal));
        actualizarIGV(subtotal);
        actualizarTotal(subtotal);
    }

    private void actualizarIGV(float subtotal) {
        float igv = subtotal * 0.18f;
        view.getJLabelIgv().setText(String.format("%.2f", igv));
    }

    private void actualizarTotal(float subtotal) {
        float igv = Float.parseFloat(view.getJLabelIgv().getText());
        float total = subtotal + igv;
        view.getJLabelTotal().setText(String.format("%.2f", total));
    }

    
    private void eliminarVehiculo() {
        int filaSeleccionada = view.getTablaVenta().getSelectedRow();
        if (filaSeleccionada >= 0) {
            DefaultTableModel model = (DefaultTableModel) view.getTablaVenta().getModel();
            model.removeRow(filaSeleccionada);
            actualizarSubtotal();
            JOptionPane.showMessageDialog(view, "Vehículo eliminado");
        } else {
            JOptionPane.showMessageDialog(view, "Seleccione un vehículo para eliminar.");
        }
    }

    private void abrirVistaClientes() {
        clientesVISTA vistaClientes = new clientesVISTA();
        ClienteControlador clienteControlador = new ClienteControlador(vistaClientes);
        vistaClientes.setVisible(true);
    }

    private void regresarInicio() {
        this.view.setVisible(false);
        inicioVISTA inicioVista = new inicioVISTA();
        InicioControlador inicioControlador = new InicioControlador(inicioVista);
        inicioVista.setVisible(true);
    }
    
    public void actualizarLabelUltimoID() {
        int ultimoID = ventaDAO.buscarUltimoIDComprobante();
        int siguienteID = ultimoID + 1;
        view.getJLabelVenta().setText(String.valueOf(siguienteID));
    }


    private void registrarVenta() {
        try {
            DTOCliente cliente = clienteDAO.verificarCliente(view.getTxtDniRUC().getText());
            int idCliente = cliente.getIdCli();

            String tipoCompra = (String) view.getJComboBoxTipoCompra().getSelectedItem();
            int idTipo = 0;
            if ("FACTURA".equals(tipoCompra)) {
                idTipo = 1;
            } else if ("BOLETA".equals(tipoCompra)) {
                idTipo = 2;
            }
            int idEmpleado = 3; 
            Date fecha = new Date();
            float subtotal = 0;


            DefaultTableModel model = (DefaultTableModel) view.getTablaVenta().getModel();
            List<DTODetalleComprobante> detalles = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                String placaVehiculo = (String) model.getValueAt(i, 3);
                float precioVehiculo = (float) model.getValueAt(i, 4);
                subtotal += precioVehiculo;

                DTOVehiculo vehiculo = vehiculoDAO.verificarVehiculo(new DTOVehiculo(placaVehiculo));
                int idVehiculo = vehiculo.getIdvehiculo();
                detalles.add(new DTODetalleComprobante(idVehiculo, 1, precioVehiculo)); 
            }

            float igv = subtotal * 0.18f;
            float total = subtotal + igv;

            // Crear objeto DTOVenta
            DTOVenta venta = new DTOVenta(idCliente, idTipo, idEmpleado, new Timestamp(fecha.getTime()), subtotal, igv, total);
            venta.setDetalles(detalles);

            // Registrar la venta
            int idComprobante = ventaDAO.registrarVenta(venta);
            if (idComprobante > 0) {
                generarBoletaPDF(venta, idComprobante);
                JOptionPane.showMessageDialog(view, "Venta registrada exitosamente. ID Comprobante: " + idComprobante);
            } else {
                JOptionPane.showMessageDialog(view, "Error al registrar la venta.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error al registrar la venta: " + e.getMessage());
        }
    }


private void generarBoletaPDF(DTOVenta venta, int idComprobante) {
    Document document = new Document();
    try {
        PdfWriter.getInstance(document, new FileOutputStream("boleta_" + idComprobante + ".pdf"));
        document.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font fuenteSubTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font fuenteContenido = FontFactory.getFont(FontFactory.HELVETICA, 12);
        
        // Información de la Empresa
        Paragraph infoEmpresa = new Paragraph("CONCESIONARIO  - UTP\nAv. Carlos 800\nLIMA - LA VICTORIA\nTELÉFONO: 509-0000", fuenteContenido);
        infoEmpresa.setAlignment(Element.ALIGN_CENTER);
        document.add(infoEmpresa);

        document.add(new Paragraph(" ")); // Espacio en blanco
        
        // Título de la Boleta
        Paragraph titulo = new Paragraph("BOLETA DE VENTA ELECTRÓNICA\nBTC1 Nº " + idComprobante, fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        document.add(new Paragraph(" ")); // Espacio en blanco

        // Información del Cliente
        Paragraph infoCliente = new Paragraph("INFORMACIÓN DEL CLIENTE", fuenteSubTitulo);
        document.add(new Paragraph(" ")); // Espacio en blanco

        document.add(infoCliente);

        PdfPTable tablaCliente = new PdfPTable(2);
        tablaCliente.setWidthPercentage(100);

        String nombre = view.getTxtNombre().getText();
        String dniRuc = view.getTxtDniRUC().getText();
        String direccion = view.getTxtDireccion().getText();
        String correo = view.getTxtCorreo().getText();

        tablaCliente.addCell(new PdfPCell(new Phrase("Nombre:", fuenteSubTitulo)));
        tablaCliente.addCell(new PdfPCell(new Phrase(nombre, fuenteContenido)));

        tablaCliente.addCell(new PdfPCell(new Phrase("Tipo y Nro. Documento:", fuenteSubTitulo)));
        tablaCliente.addCell(new PdfPCell(new Phrase(dniRuc, fuenteContenido)));

        tablaCliente.addCell(new PdfPCell(new Phrase("Dirección:", fuenteSubTitulo)));
        tablaCliente.addCell(new PdfPCell(new Phrase(direccion, fuenteContenido)));

        tablaCliente.addCell(new PdfPCell(new Phrase("Correo:", fuenteSubTitulo)));
        tablaCliente.addCell(new PdfPCell(new Phrase(correo, fuenteContenido)));

        tablaCliente.addCell(new PdfPCell(new Phrase("Fecha de Emisión:", fuenteSubTitulo)));
        tablaCliente.addCell(new PdfPCell(new Phrase(new Timestamp(new Date().getTime()).toString(), fuenteContenido)));

        document.add(tablaCliente);

        document.add(new Paragraph(" ")); 

        // Datos de la Operación
        Paragraph datosOperacion = new Paragraph("DATOS DE LA OPERACIÓN", fuenteSubTitulo);
        document.add(datosOperacion);

        PdfPTable tablaOperacion = new PdfPTable(2);
        tablaOperacion.setWidthPercentage(100);

        document.add(new Paragraph(" ")); 
        
        String moneda = "Soles";  
        String tipoOperacion = "ABONO"; 
        String nroCuenta = "000009286980"; 

        tablaOperacion.addCell(new PdfPCell(new Phrase("Moneda:", fuenteSubTitulo)));
        tablaOperacion.addCell(new PdfPCell(new Phrase(moneda, fuenteContenido)));

        tablaOperacion.addCell(new PdfPCell(new Phrase("Tipo de operación:", fuenteSubTitulo)));
        tablaOperacion.addCell(new PdfPCell(new Phrase(tipoOperacion, fuenteContenido)));

        tablaOperacion.addCell(new PdfPCell(new Phrase("Nro Cuenta:", fuenteSubTitulo)));
        tablaOperacion.addCell(new PdfPCell(new Phrase(nroCuenta, fuenteContenido)));

        document.add(tablaOperacion);

        document.add(new Paragraph(" ")); 

        // Detalles de la Venta
        PdfPTable tablaDetalles = new PdfPTable(5);
        tablaDetalles.setWidthPercentage(100);
        tablaDetalles.addCell(new PdfPCell(new Phrase("Modelo", fuenteSubTitulo)));
        tablaDetalles.addCell(new PdfPCell(new Phrase("Marca", fuenteSubTitulo)));
        tablaDetalles.addCell(new PdfPCell(new Phrase("Color", fuenteSubTitulo)));
        tablaDetalles.addCell(new PdfPCell(new Phrase("Código", fuenteSubTitulo)));
        tablaDetalles.addCell(new PdfPCell(new Phrase("Costo", fuenteSubTitulo)));

        for (DTODetalleComprobante detalle : venta.getDetalles()) {
            DTOVehiculo vehiculo = vehiculoDAO.verificarVehiculoPorId(detalle.getIdVehiculo());
            String nombreMarca = obtenerNombreMarca(vehiculo.getMarca());
            tablaDetalles.addCell(new PdfPCell(new Phrase(vehiculo.getModelo(), fuenteContenido)));
            tablaDetalles.addCell(new PdfPCell(new Phrase(nombreMarca, fuenteContenido)));
            tablaDetalles.addCell(new PdfPCell(new Phrase(vehiculo.getColor(), fuenteContenido)));
            tablaDetalles.addCell(new PdfPCell(new Phrase(vehiculo.getCodigovehiculo(), fuenteContenido)));
            tablaDetalles.addCell(new PdfPCell(new Phrase(String.format("%.2f", detalle.getSubtotal()), fuenteContenido)));
        }

        document.add(tablaDetalles);

        document.add(new Paragraph(" ")); 

        // Totales
        PdfPTable tablaTotales = new PdfPTable(2);
        tablaTotales.setWidthPercentage(100);

        tablaTotales.addCell(new PdfPCell(new Phrase("Total Operaciones Gravadas:", fuenteSubTitulo)));
        tablaTotales.addCell(new PdfPCell(new Phrase("0.00", fuenteContenido)));

        tablaTotales.addCell(new PdfPCell(new Phrase("Total Operaciones Exoneradas:", fuenteSubTitulo)));
        tablaTotales.addCell(new PdfPCell(new Phrase("0.00", fuenteContenido)));

        tablaTotales.addCell(new PdfPCell(new Phrase("Total IGV:", fuenteSubTitulo)));
        tablaTotales.addCell(new PdfPCell(new Phrase(String.format("%.2f", venta.getIgv()), fuenteContenido)));

        tablaTotales.addCell(new PdfPCell(new Phrase("Importe Total Pagado:", fuenteSubTitulo)));
        tablaTotales.addCell(new PdfPCell(new Phrase(String.format("%.2f", venta.getTotal()), fuenteContenido)));

        document.add(tablaTotales);

        document.add(new Paragraph(" ")); 

        Paragraph nota = new Paragraph("Representación impresa de la Boleta de Venta Electrónica\nEste monto incluye los conceptos que corresponden a la prestación de servicios.", fuenteContenido);
        nota.setAlignment(Element.ALIGN_CENTER);
        document.add(nota);

    } catch (DocumentException | FileNotFoundException e) {
        e.printStackTrace();
    } finally {
        document.close();
    }
}


    private String obtenerNombreMarca(int idMarca) {
        DTOMarca oMarca = new DTOMarca();
        oMarca.setIdMarca(idMarca);
        DTOMarca marca = marcaDAO.buscarPorId(oMarca);
        return marca != null ? marca.getMarca() : "Desconocido";
    }

}
