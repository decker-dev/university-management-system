package excepciones;

/**
 * Excepción lanzada cuando un usuario no tiene permisos para realizar una acción
 * Hereda de Exception
 */
public class PermisosDenegadosException extends Exception {
    
    private String usuario;
    private String accion;
    
    public PermisosDenegadosException(String usuario, String accion) {
        super("El usuario '" + usuario + "' no tiene permisos para: " + accion);
        this.usuario = usuario;
        this.accion = accion;
    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public String getAccion() {
        return accion;
    }
}

