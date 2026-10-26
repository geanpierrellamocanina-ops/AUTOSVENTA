package modelo;

/**
 *
 * @author JE
 */
public class DTOMarca {
    int idMarca;
    String Marca;

    public DTOMarca() {
    }

    public DTOMarca(int idMarca, String Marca) {
        this.idMarca = idMarca;
        this.Marca = Marca;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String Marca) {
        this.Marca = Marca;
    }
    
}
