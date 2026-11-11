package visas;

public class TipoVisaInvalidoException extends IllegalArgumentException {

    public TipoVisaInvalidoException(int codigo, String tiposValidos) {
        super("El tipo de visa " + codigo + " no existe. Tipos validos: " + tiposValidos);
    }
}
