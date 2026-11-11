package tarifas;

public class CalculadoraTarifas {

    private final ConfiguracionTarifas config;

    public CalculadoraTarifas(ConfiguracionTarifas config) {
        this.config = config;
    }

    public double calcular(EstrategiaTarifa estrategia, int meses) {
        double subtotal = config.getPrecioBaseMes() * meses + ConfiguracionTarifas.CARGO_FIJO;
        return estrategia.ajustar(subtotal);
    }
}
