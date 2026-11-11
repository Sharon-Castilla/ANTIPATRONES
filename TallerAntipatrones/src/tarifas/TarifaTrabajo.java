package tarifas;

public class TarifaTrabajo implements EstrategiaTarifa {

    private static final double RECARGO_TRABAJO = 1.5;

    @Override
    public double ajustar(double subtotal) {
        return subtotal * RECARGO_TRABAJO;
    }
}
