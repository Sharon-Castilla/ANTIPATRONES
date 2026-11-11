package observer;

public class CorreoObservador implements ObservadorSolicitud {

    @Override
    public void alCancelarse(Solicitud solicitud) {
        System.out.println("   [Correo] aviso de cancelacion enviado a " + solicitud.getViajero());
    }
}
