package observer;

// Observer: cualquiera que quiera enterarse de una cancelacion implementa esto
public interface ObservadorSolicitud {

    void alCancelarse(Solicitud solicitud);
}
