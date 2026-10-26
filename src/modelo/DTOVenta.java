package modelo;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @autor JE
 */
public class DTOVenta {
    private int idCliente;
    private int idTipo;
    private int idEmpleado;
    private Timestamp fecha;
    private float importeTotal;
    private float igv;
    private float total;
    private List<DTODetalleComprobante> detalles;

    public DTOVenta() {
    }

    public DTOVenta(int idCliente, int idTipo, int idEmpleado, Timestamp fecha, float importeTotal, float igv, float total) {
        this.idCliente = idCliente;
        this.idTipo = idTipo;
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.importeTotal = importeTotal;
        this.igv = igv;
        this.total = total;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public float getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(float importeTotal) {
        this.importeTotal = importeTotal;
    }

    public float getIgv() {
        return igv;
    }

    public void setIgv(float igv) {
        this.igv = igv;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public List<DTODetalleComprobante> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DTODetalleComprobante> detalles) {
        this.detalles = detalles;
    }
}
