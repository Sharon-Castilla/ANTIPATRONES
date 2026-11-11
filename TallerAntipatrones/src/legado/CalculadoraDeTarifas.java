package legado;

// ANTIPATRON: Copy-Paste Programming. El calculo esta clonado 3 veces.

public class CalculadoraDeTarifas {

    private double baseTurista = 50;
    private double baseEstudiante = 50;
    private double baseTrabajo = 50;
    private int edicionesHechas = 0;

    public double visaTurista(int meses) {
        double total = baseTurista * meses;
        total = total + 10;
        return total;
    }

    public double visaEstudiante(int meses) {
        double total = baseEstudiante * meses;
        total = total + 10;
        return total * 0.5;
    }

    public double visaTrabajo(int meses) {
        double total = baseTrabajo * meses;
        total = total + 10;
        return total * 1.5;
    }


    public void editarBaseTurista(double nuevo) {
        baseTurista = nuevo;
        edicionesHechas++;
    }

    public void editarBaseEstudiante(double nuevo) {
        baseEstudiante = nuevo;
        edicionesHechas++;
    }

    public void editarBaseTrabajo(double nuevo) {
        baseTrabajo = nuevo;
        edicionesHechas++;
    }

    public int getEdicionesHechas() {
        return edicionesHechas;
    }
}
