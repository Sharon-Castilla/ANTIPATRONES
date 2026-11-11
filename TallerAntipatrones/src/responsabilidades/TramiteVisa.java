package responsabilidades;

public class TramiteVisa {

    private final ValidadorPasaporte validador;
    private final CobradorDeTarifa cobrador;
    private final RepositorioDeExpedientes repositorio;
    private final NotificadorPorCorreo notificador;

    public TramiteVisa(ValidadorPasaporte validador, CobradorDeTarifa cobrador,
                    RepositorioDeExpedientes repositorio, NotificadorPorCorreo notificador) {
        this.validador = validador;
        this.cobrador = cobrador;
        this.repositorio = repositorio;
        this.notificador = notificador;
    }

    public boolean tramitar(String nombre, String pasaporte, double precio) {
        if (!validador.esValido(pasaporte)) {
            System.out.println("[TramiteVisa] pasaporte invalido, tramite detenido antes de cobrar");
            return false;
        }
        cobrador.cobrar(nombre, precio);
        repositorio.guardar(nombre);
        notificador.avisarVisaEnCamino(nombre);
        return true;
    }
}
