package modelo;

import enums.TipoExamen;
import java.util.Date;


public class Examen {
    private Date fecha;
    private int cantidadPreguntas;
    private Materia materia;
    private Profesor profesor;
    private TipoExamen tipo;

    public Examen() {
    }

    public Examen(Date fecha, int cantidadPreguntas, Materia materia, Profesor profesor, TipoExamen tipo) {
        this.fecha = fecha;
        this.cantidadPreguntas = cantidadPreguntas;
        this.materia = materia;
        this.profesor = profesor;
        this.tipo = tipo;
    }

    // Getters y Setters
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCantidadPreguntas() {
        return cantidadPreguntas;
    }

    public void setCantidadPreguntas(int cantidadPreguntas) {
        this.cantidadPreguntas = cantidadPreguntas;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public TipoExamen getTipo() {
        return tipo;
    }

    public void setTipo(TipoExamen tipo) {
        this.tipo = tipo;
    }
}

