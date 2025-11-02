import modelo.*;
import enums.*;
import java.util.Date;

/**
 * Clase Main para demostrar el uso del modelo UML en español
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  SISTEMA UNIVERSITARIO - MODELO UML");
        System.out.println("========================================");
        System.out.println();

        // 1. Crear Administrador
        System.out.println("1. CREANDO ADMINISTRADOR...");
        Administrador admin = new Administrador("ADM001", "Juan", "Pérez", "admin@universidad.edu", "admin123");
        System.out.println("   " + admin);
        System.out.println();

        // 2. Administrador crea una Carrera
        System.out.println("2. ADMINISTRADOR CREA CARRERA...");
        Carrera ingenieriaSistemas = admin.crearCarrera(
            "Ingeniería en Sistemas", 
            "Carrera de grado en informática", 
            "ISI"
        );
        System.out.println();

        // 3. Administrador crea Materias
        System.out.println("3. ADMINISTRADOR CREA MATERIAS...");
        Materia poo = admin.crearMateria(
            "Programación Orientada a Objetos",
            "Conceptos de POO en Java",
            ingenieriaSistemas,
            "/carreras/isi/poo"
        );
        
        Materia algebra = admin.crearMateria(
            "Álgebra",
            "Matemática básica",
            ingenieriaSistemas,
            "/carreras/isi/algebra"
        );
        System.out.println();

        // 4. Crear Profesor
        System.out.println("4. ADMINISTRADOR CREA PROFESOR...");
        Profesor profesor = admin.crearProfesor(
            "PROF001",
            "María",
            "González",
            "maria@universidad.edu",
            "prof123"
        );
        System.out.println();

        // 5. Asignar profesor a materia
        System.out.println("5. ASIGNANDO PROFESOR A MATERIA...");
        admin.asignarProfesorAMateria(profesor, poo);
        admin.asignarProfesorAMateria(profesor, algebra);
        System.out.println();

        // 6. Crear Estudiante
        System.out.println("6. ADMINISTRADOR CREA ESTUDIANTE...");
        Estudiante estudiante = admin.crearEstudiante(
            "EST001",
            "Carlos",
            "Rodríguez",
            "carlos@estudiante.edu",
            "est123",
            ingenieriaSistemas
        );
        System.out.println();

        // 7. Estudiante se inscribe en materias
        System.out.println("7. ESTUDIANTE SE INSCRIBE EN MATERIAS...");
        try {
            estudiante.inscribirseEnCurso(poo);
            estudiante.inscribirseEnCurso(algebra);
        } catch (excepciones.InscripcionException e) {
            System.out.println("   ERROR: " + e.getMessage());
        }
        System.out.println();

        // 8. Ver materias disponibles
        System.out.println("8. MATERIAS DISPONIBLES PARA EL ESTUDIANTE...");
        System.out.println("   (Materias aún no cursadas): " + estudiante.verMaterias().size());
        System.out.println();

        // 9. Ver materias inscriptas
        System.out.println("9. MATERIAS INSCRIPTAS DEL ESTUDIANTE...");
        for (Materia m : estudiante.getMateriasInscriptas()) {
            System.out.println("   - " + m.getNombre());
        }
        System.out.println();

        // 10. Profesor verifica si es estudiante de su materia
        System.out.println("10. PROFESOR VERIFICA ESTUDIANTE...");
        boolean esEstudiante = profesor.esEstudiante(estudiante, poo);
        System.out.println("   ¿" + estudiante.getNombre() + " es estudiante de " + poo.getNombre() + "? " + esEstudiante);
        System.out.println();

        // 11. Crear Aula
        System.out.println("11. ADMINISTRADOR CREA AULA...");
        Aula aula = admin.crearAula("Campus Central", 101, 1, TipoAula.TEORIA, 40);
        System.out.println();

        // 12. Profesor dicta una clase
        System.out.println("12. PROFESOR DICTA CLASE...");
        Date hoy = new Date();
        Date inicio = new Date(hoy.getTime());
        Date fin = new Date(hoy.getTime() + 2 * 60 * 60 * 1000); // 2 horas después
        
        Clase clase = null;
        try {
            clase = profesor.dictaMateria(poo, hoy, inicio, fin, aula);
        } catch (excepciones.PermisosDenegadosException e) {
            System.out.println("   ERROR: " + e.getMessage());
        }
        System.out.println();

        // 13. Registrar asistencia
        System.out.println("13. PROFESOR REGISTRA ASISTENCIA...");
        profesor.registrarAsistencia(estudiante, clase, true);
        System.out.println();

        // 14. Crear examen
        System.out.println("14. PROFESOR CREA EXAMEN...");
        Examen examen = null;
        try {
            examen = profesor.crearExamen(poo, hoy, 10, TipoExamen.PARCIAL);
        } catch (excepciones.PermisosDenegadosException e) {
            System.out.println("   ERROR: " + e.getMessage());
        }
        System.out.println();

        // 15. Calificar examen
        System.out.println("15. PROFESOR CALIFICA EXAMEN...");
        if (examen != null) {
            Nota nota = profesor.calificarExamen(estudiante, examen, 8.5);
            System.out.println("   Nota: " + nota.getNota());
        }
        System.out.println();
        
        // 16. DEMOSTRACIÓN DE EXCEPCIONES
        System.out.println("16. DEMOSTRANDO MANEJO DE EXCEPCIONES...");
        System.out.println("   a) Intento de inscripción duplicada:");
        try {
            estudiante.inscribirseEnCurso(poo);
        } catch (excepciones.InscripcionException e) {
            System.out.println("      ✓ Excepción capturada: " + e.getMessage());
        }
        System.out.println();
        
        System.out.println("   b) Intento de crear examen sin permisos:");
        Profesor otroProfesor = admin.crearProfesor("PROF999", "Pedro", "López", "pedro@universidad.edu", "prof999");
        try {
            otroProfesor.crearExamen(poo, hoy, 5, TipoExamen.FINAL);
        } catch (excepciones.PermisosDenegadosException e) {
            System.out.println("      ✓ Excepción capturada: " + e.getMessage());
        }
        System.out.println();

        // 17. Estadísticas finales
        System.out.println("========================================");
        System.out.println("  ESTADÍSTICAS FINALES");
        System.out.println("========================================");
        System.out.println("Carrera: " + ingenieriaSistemas.getNombre());
        System.out.println("Materias en carrera: " + ingenieriaSistemas.getMaterias().size());
        System.out.println("Estudiantes en POO: " + poo.getEstudiantes().size());
        System.out.println("Clases de POO: " + poo.getClases().size());
        System.out.println("Exámenes de POO: " + poo.getExamenes().size());
        System.out.println("Materias del profesor: " + profesor.getMaterias().size());
        System.out.println();
        
        System.out.println("========================================");
        System.out.println("  DEMOSTRACIÓN COMPLETADA");
        System.out.println("========================================");
    }
}

