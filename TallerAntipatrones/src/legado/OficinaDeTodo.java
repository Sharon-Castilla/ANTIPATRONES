package legado;

// ANTIPATRON: God Object. Ramiro hace todo en un solo metodo.
public class OficinaDeTodo {

    public void procesar(String nombre, String pasaporte, double precio) {
        if (pasaporte == null || pasaporte.length() < 6) {
            System.out.println("[OficinaDeTodo/Ramiro] Pasaporte invalido");
            return;
        }
        System.out.println("[OficinaDeTodo/Ramiro] Cobrando $" + precio + " a " + nombre);
        System.out.println("[OficinaDeTodo/Ramiro] Guardando expediente de " + nombre);
        System.out.println("[OficinaDeTodo/Ramiro] Correo para " + nombre + ": su visa va en camino");
    }
}
