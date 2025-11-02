package modelo;

import java.util.Date;

/**
 * Clase "Clase" (sesión de clase) según UML
 */
public class Clase {
    private Date inicio;
    private Date fin;
    private Date fecha;
    private Materia materia;
    private Aula aula;

    public Clase() {
    }

    public Clase(Date inicio, Date fin, Date fecha, Materia materia, Aula aula) {
        this.inicio = inicio;
        this.fin = fin;
        this.fecha = fecha;
        this.materia = materia;
        this.aula = aula;
    }

    // Getters y Setters
    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }
}

