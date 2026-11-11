package app;

import legado.CalculadoraDeTarifas;
import legado.EvaluadorMagico;
import legado.OficinaDeTodo;
import legado.SolicitudAcoplada;
import observer.CitasObservador;
import observer.CorreoObservador;
import observer.PagosObservador;
import observer.Solicitud;
import responsabilidades.CobradorDeTarifa;
import responsabilidades.NotificadorPorCorreo;
import responsabilidades.RepositorioDeExpedientes;
import responsabilidades.TramiteVisa;
import responsabilidades.ValidadorPasaporte;
import tarifas.CalculadoraTarifas;
import tarifas.ConfiguracionTarifas;
import visas.EvaluadorVisa;
import visas.ResultadoVisa;
import visas.TipoVisa;
import visas.TipoVisaInvalidoException;

public class Main {

    private static final int MESES_PRUEBA = 3;

    public static void main(String[] args) {
        caso1GodObject();
        caso2CopyPaste();
        caso3MagicNumbers();
        caso4Acoplamiento();
        caso5Integracion();
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println(texto);
        System.out.println("==================================================");
    }


    private static void caso1GodObject() {
        titulo("CASO 1 - God Object");

        System.out.println("--- Version heredada (Ramiro lo hace todo) ---");
        OficinaDeTodo ramiro = new OficinaDeTodo();
        ramiro.procesar("Ana Torres", "AB123456", 190.0);
        System.out.println("Resultado: tramite aprobado, pero todo salio de UNA sola clase");

        System.out.println();
        System.out.println("--- Version rescatada (una clase por responsabilidad) ---");
        TramiteVisa tramite = new TramiteVisa(new ValidadorPasaporte(), new CobradorDeTarifa(),
                new RepositorioDeExpedientes(), new NotificadorPorCorreo());
        boolean aprobado = tramite.tramitar("Ana Torres", "AB123456", 190.0);
        System.out.println("Resultado: tramite " + (aprobado ? "aprobado" : "rechazado")
                + ", cada tarea la hizo una clase distinta");

        System.out.println();
        System.out.println("--- Version rescatada con pasaporte invalido ---");
        boolean rechazado = !tramite.tramitar("Pedro Gomez", "123", 190.0);
        System.out.println("Resultado: tramite " + (rechazado ? "rechazado" : "aprobado")
                + ", no se cobro ni se guardo nada");
    }

 
    private static void caso2CopyPaste() {
        titulo("CASO 2 - Copy-Paste Programming");
        double nuevaBase = 60;

        System.out.println("--- Version clonada: subir la base de 50 a 60 ---");
        CalculadoraDeTarifas clonada = new CalculadoraDeTarifas();
        clonada.editarBaseTurista(nuevaBase);
        clonada.editarBaseEstudiante(nuevaBase);
        clonada.editarBaseTrabajo(nuevaBase);
        System.out.println("Lugares editados: " + clonada.getEdicionesHechas());

        System.out.println("--- Version Strategy: subir la base de 50 a 60 ---");
        ConfiguracionTarifas config = new ConfiguracionTarifas();
        CalculadoraTarifas calc = new CalculadoraTarifas(config);
        config.setPrecioBaseMes(nuevaBase);
        System.out.println("Lugares editados: " + config.getCambiosHechos());

        System.out.println();
        System.out.println("Precios para " + MESES_PRUEBA + " meses (clonada | Strategy):");
        System.out.println("  Turista    : " + clonada.visaTurista(MESES_PRUEBA) + " | "
                + calc.calcular(TipoVisa.TURISTA.getTarifa(), MESES_PRUEBA));
        System.out.println("  Estudiante : " + clonada.visaEstudiante(MESES_PRUEBA) + " | "
                + calc.calcular(TipoVisa.ESTUDIANTE.getTarifa(), MESES_PRUEBA));
        System.out.println("  Trabajo    : " + clonada.visaTrabajo(MESES_PRUEBA) + " | "
                + calc.calcular(TipoVisa.TRABAJO.getTarifa(), MESES_PRUEBA));

        // lo que paso en el consulado: se editaron 2 de las 3 copias
        System.out.println();
        System.out.println("--- Y si en la clonada se olvidan de una copia (como paso en el consulado)? ---");
        CalculadoraDeTarifas descuidada = new CalculadoraDeTarifas();
        descuidada.editarBaseTurista(nuevaBase);
        descuidada.editarBaseEstudiante(nuevaBase);
        // se olvidaron de editarBaseTrabajo
        System.out.println("  Trabajo (clonada con olvido): " + descuidada.visaTrabajo(MESES_PRUEBA)
                + "  <-- precio viejo, deberia ser " + calc.calcular(TipoVisa.TRABAJO.getTarifa(), MESES_PRUEBA));
    }


    private static void caso3MagicNumbers() {
        titulo("CASO 3 - Magic Numbers");
        EvaluadorMagico viejo = new EvaluadorMagico();
        EvaluadorVisa nuevo = new EvaluadorVisa();

        System.out.println("--- Tipo 2 con edad 20 ---");
        System.out.println("Heredada (tipo == 2)           : " + viejo.evaluar(2, 20));
        System.out.println("Rescatada (TipoVisa.ESTUDIANTE): " + nuevo.evaluar(TipoVisa.ESTUDIANTE, 20));

        System.out.println();
        System.out.println("--- Tipo 9 con edad 20 ---");
        System.out.println("Heredada: " + viejo.evaluar(9, 20) + "  (rechazada en silencio, nadie sabe por que)");
        try {
            ResultadoVisa r = nuevo.evaluar(TipoVisa.desdeCodigo(9), 20);
            System.out.println("Rescatada: " + r);
        } catch (TipoVisaInvalidoException e) {
            System.out.println("Rescatada: ERROR CLARO -> " + e.getMessage());
        }
    }


    private static void caso4Acoplamiento() {
        titulo("CASO 4 - Acoplamiento fuerte");

        System.out.println("--- Version heredada ---");
        SolicitudAcoplada vieja = new SolicitudAcoplada();
        vieja.cancelar();
        System.out.println("Estado: " + vieja.getEstado());
        System.out.println("Reembolso procesado: " + vieja.getPagos().isReembolsado()
                + "  <-- se olvidaron de la caja, el viajero pierde su plata");

        System.out.println();
        System.out.println("--- Version con Observer ---");
        Solicitud nueva = new Solicitud("Ana Torres", TipoVisa.TURISTA);
        nueva.registrarPago(190.0);
        PagosObservador pagos = new PagosObservador();
        nueva.agregarObservador(new CorreoObservador());
        nueva.agregarObservador(new CitasObservador());
        nueva.agregarObservador(pagos);
        nueva.cancelar();
        System.out.println("Estado: " + nueva.getEstado());
        System.out.println("Reembolsos procesados: " + pagos.getReembolsosHechos()
                + "  <-- la solicitud nunca conocio a los servicios");
    }


    private static void caso5Integracion() {
        titulo("CASO 5 - Integracion (viaje completo, version nueva)");

        ConfiguracionTarifas config = new ConfiguracionTarifas();
        CalculadoraTarifas calculadora = new CalculadoraTarifas(config);
        EvaluadorVisa evaluador = new EvaluadorVisa();
        CobradorDeTarifa cobrador = new CobradorDeTarifa();
        PagosObservador pagos = new PagosObservador();

        String viajero = "Luis Perez";
        int edad = 25;
        int meses = 6;
        TipoVisa tipo = TipoVisa.TRABAJO;

        System.out.println("1) Solicitar");
        Solicitud solicitud = new Solicitud(viajero, tipo);
        solicitud.agregarObservador(new CorreoObservador());
        solicitud.agregarObservador(new CitasObservador());
        solicitud.agregarObservador(pagos);
        System.out.println("   Solicitud de visa " + tipo + " creada para " + viajero
                + " (estado " + solicitud.getEstado() + ")");

        System.out.println("2) Evaluar");
        ResultadoVisa resultado = evaluador.evaluar(tipo, edad);
        System.out.println("   Edad " + edad + " -> " + resultado);

        if (resultado == ResultadoVisa.APROBADA) {
            System.out.println("3) Cobrar");
            double precio = calculadora.calcular(tipo.getTarifa(), meses);
            cobrador.cobrar(viajero, precio);
            solicitud.registrarPago(precio);
        }

        System.out.println("4) Cancelar");
        solicitud.cancelar();
        System.out.println("   Estado final: " + solicitud.getEstado()
                + " | reembolsos: " + pagos.getReembolsosHechos());

        imprimirTabla();
    }

    private static void imprimirTabla() {
        System.out.println();
        System.out.println("TABLA FINAL: antipatrones capturados");
        String formato = "%-22s | %-34s | %s%n";
        System.out.printf(formato, "Antipatron", "Sintoma", "Solucion");
        System.out.println("-----------------------+------------------------------------+------------------------------");
        System.out.printf(formato, "God Object", "Ramiro valida, cobra, guarda y", "Responsabilidad unica (1 clase");
        System.out.printf(formato, "", "manda el correo", "por tarea) + TramiteVisa");
        System.out.printf(formato, "Copy-Paste", "Misma cuenta copiada 3 veces", "Strategy + base en un solo lugar");
        System.out.printf(formato, "Magic Numbers", "tipo == 3, * 0.5, edad > 17", "enum TipoVisa y constantes");
        System.out.printf(formato, "Acoplamiento fuerte", "La solicitud crea y llama a los", "Observer (la solicitud solo");
        System.out.printf(formato, "", "3 servicios a mano", "conoce una interfaz)");
    }
}
