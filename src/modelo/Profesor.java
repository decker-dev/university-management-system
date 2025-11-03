package modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Profesor extends Usuario {
    private List<Materia> materias;

    public Profesor() {
        super();
        this.materias = new ArrayList<>();
    }

    public Profesor(String legajo, String nombre, String apellido, String email, String password) {
        super(legajo, nombre, apellido, email, password);
        this.materias = new ArrayList<>();
    }

    // Getters y Setters
    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    public void agregarMateria(Materia materia) {
        if (!this.materias.contains(materia)) {
            this.materias.add(materia);
            materia.setProfesor(this);
        }
    }

    public Clase dictaMateria(Materia m, Date fecha, Date inicio, Date fin, Aula aula) 
            throws excepciones.PermisosDenegadosException {
        // Verificar que el profesor dicta esta materia
        if (!this.materias.contains(m) && m.getProfesor() != this) {
            throw new excepciones.PermisosDenegadosException(
                this.getNombreCompleto(), 
                "dictar la materia '" + m.getNombre() + "'"
            );
        }

        // Crear la clase
        Clase clase = new Clase(inicio, fin, fecha, m, aula);
        m.agregarClase(clase);
        
        System.out.println("Clase creada: " + m.getNombre() + " - " + fecha);
        return clase;
    }

    public boolean esEstudiante(Estudiante e, Materia m) {
        return m.getEstudiantes().contains(e);
    }

    public boolean haAsistidoClase(Estudiante e, Clase c, Date fecha) {
        // En un sistema real, buscaríamos en una lista de asistencias
        // Por simplicidad, aquí podríamos tener una lista de asistencias
        // Pero para mantenerlo simple según UML, retornamos un placeholder
        
        // Verificar que la clase es de una materia del profesor
        if (!this.materias.contains(c.getMateria())) {
            System.out.println("Error: Esta clase no es de tu materia");
            return false;
        }

        // Aquí deberías buscar en una lista de Asistencia
        // Por ahora retornamos false por defecto
        System.out.println("Verificando asistencia de " + e.getNombre() + " a clase del " + fecha);
        return false;
    }

    public Asistencia registrarAsistencia(Estudiante e, Clase c, boolean asistio) {
        if (!this.materias.contains(c.getMateria())) {
            System.out.println("Error: Esta clase no es de tu materia");
            return null;
        }

        Asistencia asistencia = new Asistencia(asistio, e, c);
        System.out.println("Asistencia registrada: " + e.getNombre() + " - " + (asistio ? "Presente" : "Ausente"));
        return asistencia;
    }

    public Examen crearExamen(Materia m, Date fecha, int cantidadPreguntas, enums.TipoExamen tipo) 
            throws excepciones.PermisosDenegadosException {
        if (!this.materias.contains(m)) {
            throw new excepciones.PermisosDenegadosException(
                this.getNombreCompleto(),
                "crear examen para la materia '" + m.getNombre() + "'"
            );
        }

        Examen examen = new Examen(fecha, cantidadPreguntas, m, this, tipo);
        m.agregarExamen(examen);
        System.out.println("Examen creado: " + tipo + " - " + m.getNombre());
        return examen;
    }

    public Nota calificarExamen(Estudiante e, Examen examen, double nota) {
        if (!this.materias.contains(examen.getMateria())) {
            System.out.println("Error: Este examen no es de tu materia");
            return null;
        }

        Nota notaObj = new Nota(e, examen, nota);
        System.out.println("Nota registrada: " + e.getNombre() + " - " + nota);
        return notaObj;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (Profesor - Legajo: " + legajo + ")";
    }
}

