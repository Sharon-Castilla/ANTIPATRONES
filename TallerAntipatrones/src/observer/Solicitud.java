package observer;

import java.util.ArrayList;
import java.util.List;
import visas.TipoVisa;

// El sujeto. No sabe quienes lo escuchan, solo avisa a su lista.
public class Solicitud {

    private final String viajero;
    private final TipoVisa tipo;
    private EstadoSolicitud estado = EstadoSolicitud.ACTIVA;
    private double montoPagado = 0;
    private final List<ObservadorSolicitud> observadores = new ArrayList<>();

    public Solicitud(String viajero, TipoVisa tipo) {
        this.viajero = viajero;
        this.tipo = tipo;
    }

    public void agregarObservador(ObservadorSolicitud obs) {
        observadores.add(obs);
    }

    public void registrarPago(double monto) {
        montoPagado = monto;
    }

    public void cancelar() {
        if (estado == EstadoSolicitud.CANCELADA) {
            return;
        }
        estado = EstadoSolicitud.CANCELADA;
        for (ObservadorSolicitud obs : observadores) {
            obs.alCancelarse(this);
        }
    }

    public String getViajero() {
        return viajero;
    }

    public TipoVisa getTipo() {
        return tipo;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public double getMontoPagado() {
        return montoPagado;
    }
}
