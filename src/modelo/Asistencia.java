package modelo;

/**
 * Clase Asistencia según UML
 * Representa si un estudiante asistió o no a una clase
 */
public class Asistencia {
    private boolean asistio;
    private Estudiante estudiante;
    private Clase clase;

    public Asistencia() {
    }

    public Asistencia(boolean asistio, Estudiante estudiante, Clase clase) {
        this.asistio = asistio;
        this.estudiante = estudiante;
        this.clase = clase;
    }

    // Getters y Setters
    public boolean isAsistio() {
        return asistio;
    }

    public void setAsistio(boolean asistio) {
        this.asistio = asistio;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }
}

