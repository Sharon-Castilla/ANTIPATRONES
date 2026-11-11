package visas;

public class EvaluadorVisa {

    public ResultadoVisa evaluar(TipoVisa tipo, int edad) {
        if (tipo.aceptaEdad(edad)) {
            return ResultadoVisa.APROBADA;
        }
        return ResultadoVisa.RECHAZADA;
    }
}
