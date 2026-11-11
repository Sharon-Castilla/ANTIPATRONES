package observer;

public class PagosObservador implements ObservadorSolicitud {

    private int reembolsosHechos = 0;

    @Override
    public void alCancelarse(Solicitud solicitud) {
        reembolsosHechos++;
        System.out.println("   [Pagos] reembolso de $" + solicitud.getMontoPagado()
                + " procesado para " + solicitud.getViajero());
    }

    public int getReembolsosHechos() {
        return reembolsosHechos;
    }
}
