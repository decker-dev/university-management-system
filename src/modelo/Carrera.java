package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Carrera según UML
 */
public class Carrera {
    private String nombre;
    private String descripcion;
    private String carrera; // Código de la carrera

    // Relación con Materia (según UML: una carrera tiene muchas materias)
    private List<Materia> materias;

    public Carrera() {
        this.materias = new ArrayList<>();
    }

    public Carrera(String nombre, String descripcion, String carrera) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.carrera = carrera;
        this.materias = new ArrayList<>();
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

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    public void agregarMateria(Materia materia) {
        if (!this.materias.contains(materia)) {
            this.materias.add(materia);
        }
    }

    @Override
    public String toString() {
        return nombre + " (" + carrera + ")";
    }
}

