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

    public Carrera crearCarrera(String nombre, String descripcion, String carrera) {
        Carrera nuevaCarrera = new Carrera(nombre, descripcion, carrera);
        System.out.println("Carrera creada: " + nombre + " (" + carrera + ")");
        return nuevaCarrera;
    }

    public Materia crearMateria(String nombre, String descripcion, Carrera carrera, String camino) {
        Materia nuevaMateria = new Materia(nombre, descripcion, carrera, null);
        carrera.agregarMateria(nuevaMateria);
        System.out.println("Materia creada: " + nombre + " en " + carrera.getNombre());
        return nuevaMateria;
    }

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

    public Estudiante crearEstudiante(String legajo, String nombre, String apellido,
                                     String email, String password, Carrera carrera) {
        Estudiante nuevoEstudiante = new Estudiante(legajo, nombre, apellido, email, password, carrera);
        System.out.println("Estudiante creado: " + nombre + " " + apellido + " (Legajo: " + legajo + ")");
        return nuevoEstudiante;
    }

    public Profesor crearProfesor(String legajo, String nombre, String apellido,
                                 String email, String password) {
        Profesor nuevoProfesor = new Profesor(legajo, nombre, apellido, email, password);
        System.out.println("Profesor creado: " + nombre + " " + apellido + " (Legajo: " + legajo + ")");
        return nuevoProfesor;
    }

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

