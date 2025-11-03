package modelo;

import java.util.ArrayList;
import java.util.List;

public class Estudiante extends Usuario {
    private Carrera carrera;
    private List<Materia> materiasInscriptas;

    public Estudiante() {
        super();
        this.materiasInscriptas = new ArrayList<>();
    }

    public Estudiante(String legajo, String nombre, String apellido, String email, String password, Carrera carrera) {
        super(legajo, nombre, apellido, email, password);
        this.carrera = carrera;
        this.materiasInscriptas = new ArrayList<>();
    }

    // Getters y Setters
    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public List<Materia> getMateriasInscriptas() {
        return materiasInscriptas;
    }

    public void setMateriasInscriptas(List<Materia> materiasInscriptas) {
        this.materiasInscriptas = materiasInscriptas;
    }

    public boolean inscribirseEnCurso(Materia materia) throws excepciones.InscripcionException {
        // Validar que la materia pertenece a su carrera
        if (materia.getCarrera() != this.carrera) {
            throw new excepciones.InscripcionException(
                "La materia '" + materia.getNombre() + "' no pertenece a tu carrera '" + carrera.getNombre() + "'"
            );
        }

        // Verificar que no esté ya inscripto
        if (this.materiasInscriptas.contains(materia)) {
            throw new excepciones.InscripcionException(
                "Ya estás inscripto en la materia: " + materia.getNombre()
            );
        }

        // Inscribirse
        this.materiasInscriptas.add(materia);
        materia.agregarEstudiante(this);
        
        System.out.println("Inscripción exitosa en: " + materia.getNombre());
        return true;
    }

    public List<Materia> verMaterias() {
        List<Materia> materiasDisponibles = new ArrayList<>();
        
        // Obtener todas las materias de la carrera
        for (Materia materia : carrera.getMaterias()) {
            // Si no está inscripto, está disponible
            if (!this.materiasInscriptas.contains(materia)) {
                materiasDisponibles.add(materia);
            }
        }
        
        return materiasDisponibles;
    }

    public List<Materia> materiasInscriptas(String legajo) {
        if (this.legajo.equals(legajo)) {
            return this.materiasInscriptas;
        }
        return new ArrayList<>();
    }

    public boolean desinscribirseDeMateria(Materia materia) {
        if (this.materiasInscriptas.contains(materia)) {
            this.materiasInscriptas.remove(materia);
            materia.eliminarEstudiante(this);
            System.out.println("Te has desinscripto de: " + materia.getNombre());
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (Legajo: " + legajo + ")";
    }
}

