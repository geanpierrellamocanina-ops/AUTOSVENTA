package modelo;

/**
 *
 * @author JE
 */
public class DTODetalleComprobante {
    private int idVehiculo;
    private int cantidadVehiculos;
    private float subtotal;

    public DTODetalleComprobante() {
    }

    public DTODetalleComprobante(int idVehiculo, int cantidadVehiculos, float subtotal) {
        this.idVehiculo = idVehiculo;
        this.cantidadVehiculos = cantidadVehiculos;
        this.subtotal = subtotal;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public int getCantidadVehiculos() {
        return cantidadVehiculos;
    }

    public void setCantidadVehiculos(int cantidadVehiculos) {
        this.cantidadVehiculos = cantidadVehiculos;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }


}