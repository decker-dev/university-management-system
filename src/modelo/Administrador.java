package modelo;

/**
 * Clase Administrador según UML
 * Hereda de Usuario
 */
public class Administrador extends Usuario {

    public Administrador() {
        super();
    }

    public Administrador(String legajo, String nombre, String apellido, String email, String password) {
        super(legajo, nombre, apellido, email, password);
    }

    // ==================== MÉTODOS DEL UML ====================

    /**
     * Método del UML: crearCarrera
     * Crea una nueva carrera en el sistema
     * @param nombre Nombre de la carrera
     * @param descripcion Descripción de la carrera
     * @param carrera Código de la carrera
     * @return La carrera creada
     */
    public Carrera crearCarrera(String nombre, String descripcion, String carrera) {
        Carrera nuevaCarrera = new Carrera(nombre, descripcion, carrera);
        System.out.println("Carrera creada: " + nombre + " (" + carrera + ")");
        return nuevaCarrera;
    }

    /**
     * Método del UML: crearMateria
     * Crea una nueva materia en el sistema
     * @param nombre Nombre de la materia
     * @param descripcion Descripción de la materia
     * @param carrera Carrera a la que pertenece
     * @param camino (interpretado como código o identificador)
     * @return La materia creada
     */
    public Materia crearMateria(String nombre, String descripcion, Carrera carrera, String camino) {
        Materia nuevaMateria = new Materia(nombre, descripcion, carrera, null);
        carrera.agregarMateria(nuevaMateria);
        System.out.println("Materia creada: " + nombre + " en " + carrera.getNombre());
        return nuevaMateria;
    }

    /**
     * Método del UML: modificarCarrera
     * Modifica una carrera existente
     * @param carreraObj La carrera a modificar
     * @param nombre Nuevo nombre
     * @param descripcion Nueva descripción  
     * @param codigo Nuevo código
     * @return true si se modificó exitosamente
     */
    public boolean modificarCarrera(Carrera carreraObj, String nombre, String descripcion, String codigo) {
        if (carreraObj == null) {
            System.out.println("Error: Carrera no encontrada");
            return false;
        }

        carreraObj.setNombre(nombre);
        carreraObj.setDescripcion(descripcion);
        carreraObj.setCarrera(codigo);
        
        System.out.println("Carrera modificada: " + nombre);
        return true;
    }

    /**
     * Método adicional: Modificar materia
     */
    public boolean modificarMateria(Materia materia, String nombre, String descripcion) {
        if (materia == null) {
            System.out.println("Error: Materia no encontrada");
            return false;
        }

        materia.setNombre(nombre);
        materia.setDescripcion(descripcion);
        
        System.out.println("Materia modificada: " + nombre);
        return true;
    }

    /**
     * Método adicional: Crear un estudiante
     */
    public Estudiante crearEstudiante(String legajo, String nombre, String apellido, 
                                     String email, String password, Carrera carrera) {
        Estudiante nuevoEstudiante = new Estudiante(legajo, nombre, apellido, email, password, carrera);
        System.out.println("Estudiante creado: " + nombre + " " + apellido + " (Legajo: " + legajo + ")");
        return nuevoEstudiante;
    }

    /**
     * Método adicional: Crear un profesor
     */
    public Profesor crearProfesor(String legajo, String nombre, String apellido, 
                                 String email, String password) {
        Profesor nuevoProfesor = new Profesor(legajo, nombre, apellido, email, password);
        System.out.println("Profesor creado: " + nombre + " " + apellido + " (Legajo: " + legajo + ")");
        return nuevoProfesor;
    }

    /**
     * Método adicional: Asignar profesor a materia
     */
    public boolean asignarProfesorAMateria(Profesor profesor, Materia materia) {
        if (profesor == null || materia == null) {
            System.out.println("Error: Profesor o materia no encontrados");
            return false;
        }

        materia.setProfesor(profesor);
        profesor.agregarMateria(materia);
        
        System.out.println("Profesor " + profesor.getNombre() + " asignado a " + materia.getNombre());
        return true;
    }

    /**
     * Método adicional: Crear un aula
     */
    public Aula crearAula(String sede, int numero, int piso, enums.TipoAula tipoAula, int capacidad) {
        Aula nuevaAula = new Aula(sede, numero, piso, tipoAula, capacidad);
        System.out.println("Aula creada: " + numero + " - " + tipoAula);
        return nuevaAula;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (Administrador - Legajo: " + legajo + ")";
    }
}

