package excepciones;

/**
 * Excepción lanzada cuando no se encuentra una materia
 * Hereda de Exception
 */
public class MateriaNoEncontradaException extends Exception {
    
    private String nombreMateria;
    
    public MateriaNoEncontradaException(String nombreMateria) {
        super("Materia no encontrada: " + nombreMateria);
        this.nombreMateria = nombreMateria;
    }
    
    public MateriaNoEncontradaException(String nombreMateria, String mensaje) {
        super(mensaje);
        this.nombreMateria = nombreMateria;
    }
    
    public String getNombreMateria() {
        return nombreMateria;
    }
}

