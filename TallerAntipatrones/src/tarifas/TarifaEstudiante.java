package tarifas;

public class TarifaEstudiante implements EstrategiaTarifa {

    private static final double DESCUENTO_ESTUDIANTE = 0.5;

    @Override
    public double ajustar(double subtotal) {
        return subtotal * DESCUENTO_ESTUDIANTE;
    }
}
