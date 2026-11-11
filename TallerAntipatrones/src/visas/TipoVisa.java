package visas;

import tarifas.EstrategiaTarifa;
import tarifas.TarifaEstudiante;
import tarifas.TarifaTrabajo;
import tarifas.TarifaTurista;


public enum TipoVisa {

    TURISTA(ReglasVisa.CODIGO_TURISTA, ReglasVisa.EDAD_MINIMA_ADULTO,
            ReglasVisa.SIN_LIMITE_DE_EDAD, new TarifaTurista()),
    ESTUDIANTE(ReglasVisa.CODIGO_ESTUDIANTE, ReglasVisa.EDAD_MINIMA_ESTUDIANTE,
            ReglasVisa.EDAD_MAXIMA_ESTUDIANTE, new TarifaEstudiante()),
    TRABAJO(ReglasVisa.CODIGO_TRABAJO, ReglasVisa.EDAD_MINIMA_ADULTO,
            ReglasVisa.EDAD_MAXIMA_TRABAJO, new TarifaTrabajo());

    private final int codigo;
    private final int edadMinima;
    private final int edadMaxima;
    private final EstrategiaTarifa tarifa;

    TipoVisa(int codigo, int edadMinima, int edadMaxima, EstrategiaTarifa tarifa) {
        this.codigo = codigo;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
        this.tarifa = tarifa;
    }

    public boolean aceptaEdad(int edad) {
        return edad >= edadMinima && edad <= edadMaxima;
    }

    public EstrategiaTarifa getTarifa() {
        return tarifa;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoVisa desdeCodigo(int codigo) {
        for (TipoVisa tipo : values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new TipoVisaInvalidoException(codigo, descripcionDeTiposValidos());
    }

    private static String descripcionDeTiposValidos() {
        StringBuilder sb = new StringBuilder();
        for (TipoVisa tipo : values()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(tipo.name()).append("(").append(tipo.codigo).append(")");
        }
        return sb.toString();
    }
}
