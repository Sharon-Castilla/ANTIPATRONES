package tarifas;

public class ConfiguracionTarifas {

    public static final double CARGO_FIJO = 10;

    private double precioBaseMes = 50;
    private int cambiosHechos = 0;

    public double getPrecioBaseMes() {
        return precioBaseMes;
    }

    public void setPrecioBaseMes(double nuevoPrecio) {
        precioBaseMes = nuevoPrecio;
        cambiosHechos++;
    }

    public int getCambiosHechos() {
        return cambiosHechos;
    }
}
