package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Estudiante según UML
 * Hereda de Usuario
 */
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

    // ==================== MÉTODOS DEL UML ====================

    /**
     * Método del UML: inscribirseEnCurso
     * Permite al estudiante inscribirse en una materia
     * @param materia La materia en la que desea inscribirse
     * @return true si la inscripción fue exitosa
     */
    public boolean inscribirseEnCurso(Materia materia) {
        // Validar que la materia pertenece a su carrera
        if (materia.getCarrera() != this.carrera) {
            System.out.println("Error: La materia no pertenece a tu carrera");
            return false;
        }

        // Verificar que no esté ya inscripto
        if (this.materiasInscriptas.contains(materia)) {
            System.out.println("Error: Ya estás inscripto en esta materia");
            return false;
        }

        // Inscribirse
        this.materiasInscriptas.add(materia);
        materia.agregarEstudiante(this);
        
        System.out.println("Inscripción exitosa en: " + materia.getNombre());
        return true;
    }

    /**
     * Método del UML: verMaterias
     * Muestra las materias disponibles para inscribirse de su carrera
     * @return Lista de materias disponibles (no inscriptas aún)
     */
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

    /**
     * Método del UML: materiasInscriptas (como getter con lógica)
     * Obtiene las materias en las que el estudiante está inscripto por su legajo
     * @param legajo El legajo del estudiante (this.legajo)
     * @return Lista de materias inscriptas
     */
    public List<Materia> materiasInscriptas(String legajo) {
        if (this.legajo.equals(legajo)) {
            return this.materiasInscriptas;
        }
        return new ArrayList<>();
    }

    /**
     * Método adicional: Desinscribirse de una materia
     */
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

