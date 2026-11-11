package legado;

// ANTIPATRON: Acoplamiento fuerte. La solicitud crea y conoce a los 3 servicios,

public class SolicitudAcoplada {

    private String estado = "ACTIVA";
    private CorreoServicio correo = new CorreoServicio();
    private CitasServicio citas = new CitasServicio();
    private PagosServicio pagos = new PagosServicio();

    public void cancelar() {
        estado = "CANCELADA";
        correo.avisarCancelacion();
        citas.liberarCupo();

    }

    public String getEstado() {
        return estado;
    }

    public PagosServicio getPagos() {
        return pagos;
    }
}
