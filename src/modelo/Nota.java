package modelo;

/**
 * Clase Nota - CLASE DE ASOCIACIÓN (corrección del UML)
 * 
 * Representa la relación entre un Estudiante y un Examen
 * con la nota que obtuvo ese estudiante en ese examen específico.
 * 
 * CORRECCIÓN: En el UML original, la nota estaba en Examen,
 * pero esto es incorrecto porque muchos estudiantes rinden el mismo examen
 * y cada uno tiene su propia nota.
 */
public class Nota {
    private Estudiante estudiante;
    private Examen examen;
    private double nota;

    public Nota() {
    }

    public Nota(Estudiante estudiante, Examen examen, double nota) {
        this.estudiante = estudiante;
        this.examen = examen;
        this.nota = nota;
    }

    // Getters y Setters
    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Examen getExamen() {
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
}

