package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Materia según UML
 * Contiene listas de Estudiantes, Clases y Exámenes
 */
public class Materia {
    private String nombre;
    private String descripcion;
    private Carrera carrera;
    private Profesor profesor;
    
    // Relaciones según UML
    private List<Estudiante> estudiantes;  // Estudiantes inscriptos (*)
    private List<Clase> clases;            // Clases de la materia (*)
    private List<Examen> examenes;         // Exámenes de la materia (*)

    public Materia() {
        this.estudiantes = new ArrayList<>();
        this.clases = new ArrayList<>();
        this.examenes = new ArrayList<>();
    }

    public Materia(String nombre, String descripcion, Carrera carrera, Profesor profesor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.carrera = carrera;
        this.profesor = profesor;
        this.estudiantes = new ArrayList<>();
        this.clases = new ArrayList<>();
        this.examenes = new ArrayList<>();
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public List<Clase> getClases() {
        return clases;
    }

    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }

    public List<Examen> getExamenes() {
        return examenes;
    }

    public void setExamenes(List<Examen> examenes) {
        this.examenes = examenes;
    }

    // Métodos para manejar las colecciones
    public void agregarEstudiante(Estudiante estudiante) {
        if (!this.estudiantes.contains(estudiante)) {
            this.estudiantes.add(estudiante);
        }
    }

    public void eliminarEstudiante(Estudiante estudiante) {
        this.estudiantes.remove(estudiante);
    }

    public void agregarClase(Clase clase) {
        if (!this.clases.contains(clase)) {
            this.clases.add(clase);
        }
    }

    public void agregarExamen(Examen examen) {
        if (!this.examenes.contains(examen)) {
            this.examenes.add(examen);
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}

