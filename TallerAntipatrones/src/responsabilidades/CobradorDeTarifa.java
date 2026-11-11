package responsabilidades;


public class CobradorDeTarifa {

    public void cobrar(String nombre, double precio) {
        System.out.println("[CobradorDeTarifa] cobrando $" + precio + " a " + nombre);
    }
}
