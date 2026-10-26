package modelo;


public class DTOClienteEmpresa extends DTOCliente{
    String ruc;
    String razonSocial;

    public DTOClienteEmpresa() {
    }


    public DTOClienteEmpresa(String ruc, String razonSocial, int idCli, String tipoCliente, String direccion, String correo) {
        super(idCli, tipoCliente, direccion, correo);
        this.ruc = ruc;
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    
}
