package modelo;

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

