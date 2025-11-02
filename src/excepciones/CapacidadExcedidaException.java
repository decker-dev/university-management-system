package excepciones;

/**
 * Excepción lanzada cuando se excede la capacidad de un aula o materia
 * Hereda de RuntimeException (no chequeada)
 */
public class CapacidadExcedidaException extends RuntimeException {
    
    private int capacidadMaxima;
    private int capacidadActual;
    
    public CapacidadExcedidaException(int capacidadMaxima, int capacidadActual) {
        super("Capacidad excedida. Máxima: " + capacidadMaxima + ", Actual: " + capacidadActual);
        this.capacidadMaxima = capacidadMaxima;
        this.capacidadActual = capacidadActual;
    }
    
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public int getCapacidadActual() {
        return capacidadActual;
    }
}

