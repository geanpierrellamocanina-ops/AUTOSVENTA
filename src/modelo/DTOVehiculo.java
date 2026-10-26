package modelo;

/**
 *
 * @author JE
 */
public class DTOVehiculo {
    int idvehiculo;
    String codigovehiculo;
    int Marca;
    String modelo;
    String color;
    String categoria;
    String año;
    Float costo;
    byte [] imagenVehiculo;
    
    public DTOVehiculo() {
    }

    public DTOVehiculo(String codigovehiculo) {
        this.codigovehiculo = codigovehiculo;
    }
    
    public DTOVehiculo(int idvehiculo) {
        this.idvehiculo = idvehiculo;
    }

    public DTOVehiculo(int idvehiculo, String codigovehiculo, int Marca, String modelo, String color, String categoria, String año, Float costo, byte[] imagenVehiculo) {
        this.idvehiculo = idvehiculo;
        this.codigovehiculo = codigovehiculo;
        this.Marca = Marca;
        this.modelo = modelo;
        this.color = color;
        this.categoria = categoria;
        this.año = año;
        this.costo = costo;
        this.imagenVehiculo = imagenVehiculo;
    }

    public int getIdvehiculo() {
        return idvehiculo;
    }

    public void setIdvehiculo(int idvehiculo) {
        this.idvehiculo = idvehiculo;
    }

    public String getCodigovehiculo() {
        return codigovehiculo;
    }

    public void setCodigovehiculo(String codigovehiculo) {
        this.codigovehiculo = codigovehiculo;
    }

    public int getMarca() {
        return Marca;
    }

    public void setMarca(int Marca) {
        this.Marca = Marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public byte[] getImagenVehiculo() {
        return imagenVehiculo;
    }

    public void setImagenVehiculo(byte[] imagenVehiculo) {
        this.imagenVehiculo = imagenVehiculo;
    }
    
  
}