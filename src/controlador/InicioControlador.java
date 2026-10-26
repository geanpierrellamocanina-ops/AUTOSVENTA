package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.clientesVISTA;
import vista.empleadosVISTA;
import vista.inicioVISTA;
import vista.marcaVISTA;
import vista.reporteVISTA;
import vista.vehiculosVISTA;
import vista.ventaVISTA;

public class InicioControlador implements ActionListener {
    private inicioVISTA vista;

    public InicioControlador(inicioVISTA vista) {
        this.vista = vista;
        this.vista.getBtnClientes().addActionListener(this);
        this.vista.getBtnVehiculos().addActionListener(this);
        this.vista.getBtnVenta().addActionListener(this);
        this.vista.getBtnReporte().addActionListener(this);
        this.vista.getBtnCerrar().addActionListener(this);
        this.vista.getBtnEmpleados().addActionListener(this);
        this.vista.getBtnMarcas().addActionListener(this); 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnClientes()) {
            abrirClientes();
        } else if (e.getSource() == vista.getBtnVehiculos()) {
            abrirVehiculos();
        } else if (e.getSource() == vista.getBtnVenta()) {
            abrirVenta();
        } else if (e.getSource() == vista.getBtnReporte()) {
            abrirReporte();
        } else if (e.getSource() == vista.getBtnCerrar()) {
            cerrarSesion();
        } else if (e.getSource() == vista.getBtnEmpleados()) {
            abrirEmpleados();  
        } else if (e.getSource() == vista.getBtnMarcas()) { 
            abrirMarcas();
        }
    }

    private void abrirClientes() {
        clientesVISTA clientesVista = new clientesVISTA();
        ClienteControlador clienteControlador = new ClienteControlador(clientesVista);
        clientesVista.setVisible(true);
        vista.setVisible(false);
    }

    private void abrirVehiculos() {
        vehiculosVISTA vehiculosVista = new vehiculosVISTA();
        VehiculosControlador vehiculosControlador = new VehiculosControlador(vehiculosVista);
        vehiculosVista.setVisible(true);
        vista.setVisible(false);
    }

    private void abrirVenta() {
        ventaVISTA ventasVista = new ventaVISTA();
        VentaControlador ventaControlador = new VentaControlador(ventasVista);
        ventasVista.setVisible(true);
        vista.setVisible(false);
    }

    private void abrirReporte() {
        reporteVISTA reporteVista = new reporteVISTA();
        ReporteControlador reporteControlador = new ReporteControlador(reporteVista);
        reporteVista.setVisible(true);
        vista.setVisible(false);
    }

    private void cerrarSesion() {
        System.exit(0);
    }
    
    private void abrirEmpleados() {
        empleadosVISTA empleadosVista = new empleadosVISTA();
        EmpleadosControlador empleadosControlador = new EmpleadosControlador(empleadosVista);
        empleadosVista.setVisible(true);
        vista.setVisible(false);
    }
    
    private void abrirMarcas() { 
        marcaVISTA marcaVista = new marcaVISTA();
        MarcaControlador marcaControlador = new MarcaControlador(marcaVista);
        marcaVista.setVisible(true);
        vista.setVisible(false);
    }
}
