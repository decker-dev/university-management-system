package excepciones;

/**
 * Excepción personalizada para errores de inscripción
 * Hereda de Exception para manejo de errores de negocio
 */
public class InscripcionException extends Exception {
    
    public InscripcionException(String mensaje) {
        super(mensaje);
    }
    
    public InscripcionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

