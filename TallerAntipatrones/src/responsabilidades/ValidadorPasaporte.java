package responsabilidades;

public class ValidadorPasaporte {

    private static final int LONGITUD_MINIMA = 6;

    public boolean esValido(String pasaporte) {
        boolean ok = pasaporte != null && pasaporte.length() >= LONGITUD_MINIMA;
        System.out.println("[ValidadorPasaporte] pasaporte " + (ok ? "valido" : "invalido"));
        return ok;
    }
}
