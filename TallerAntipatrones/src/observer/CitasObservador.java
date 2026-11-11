package observer;

public class CitasObservador implements ObservadorSolicitud {

    @Override
    public void alCancelarse(Solicitud solicitud) {
        System.out.println("   [Citas] cupo liberado de " + solicitud.getViajero());
    }
}
