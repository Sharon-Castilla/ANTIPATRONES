package tarifas;

public class TarifaTurista implements EstrategiaTarifa {

    private static final double FACTOR = 1.0;

    @Override
    public double ajustar(double subtotal) {
        return subtotal * FACTOR;
    }
}
