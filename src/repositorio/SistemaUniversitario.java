package repositorio;

import modelo.*;
import enums.*;
import java.util.*;

/**
 * Repositorio en memoria que mantiene todos los datos del sistema
 * Singleton para acceso global
 */
public class SistemaUniversitario {
    private static SistemaUniversitario instancia;
    
    // Colecciones en memoria
    private Map<String, Usuario> usuarios;           // legajo -> Usuario
    private Map<Integer, Carrera> carreras;          // id -> Carrera
    private Map<Integer, Materia> materias;          // id -> Materia
    private Map<Integer, Aula> aulas;                // id -> Aula
    private Map<Integer, Clase> clases;              // id -> Clase
    private Map<Integer, Examen> examenes;           // id -> Examen
    private List<Nota> notas;
    private List<Asistencia> asistencias;
    
    // Contadores de IDs
    private int nextCarreraId = 1;
    private int nextMateriaId = 1;
    private int nextAulaId = 1;
    private int nextClaseId = 1;
    private int nextExamenId = 1;
    
    private SistemaUniversitario() {
        usuarios = new HashMap<>();
        carreras = new HashMap<>();
        materias = new HashMap<>();
        aulas = new HashMap<>();
        clases = new HashMap<>();
        examenes = new HashMap<>();
        notas = new ArrayList<>();
        asistencias = new ArrayList<>();
        
        inicializarDatosDefault();
    }
    
    public static SistemaUniversitario getInstance() {
        if (instancia == null) {
            instancia = new SistemaUniversitario();
        }
        return instancia;
    }
    
    private void inicializarDatosDefault() {
        // Crear administrador por defecto
        Administrador admin = new Administrador("admin", "Admin", "Sistema", 
            "admin@universidad.edu", "admin123");
        usuarios.put(admin.getLegajo(), admin);
        
        // Crear algunas carreras
        Carrera isi = new Carrera("Ingeniería en Sistemas", "Carrera de grado", "ISI");
        carreras.put(nextCarreraId++, isi);
        
        Carrera iin = new Carrera("Ingeniería Industrial", "Carrera de grado", "IIN");
        carreras.put(nextCarreraId++, iin);
    }
    
    // === GESTIÓN DE USUARIOS ===
    public void agregarUsuario(Usuario usuario) {
        usuarios.put(usuario.getLegajo(), usuario);
    }
    
    public Usuario obtenerUsuario(String legajo) {
        return usuarios.get(legajo);
    }
    
    public Usuario autenticar(String legajo, String password) {
        Usuario usuario = usuarios.get(legajo);
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }
    
    public List<Estudiante> obtenerTodosEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        for (Usuario u : usuarios.values()) {
            if (u instanceof Estudiante) {
                estudiantes.add((Estudiante) u);
            }
        }
        return estudiantes;
    }
    
    public List<Profesor> obtenerTodosProfesores() {
        List<Profesor> profesores = new ArrayList<>();
        for (Usuario u : usuarios.values()) {
            if (u instanceof Profesor) {
                profesores.add((Profesor) u);
            }
        }
        return profesores;
    }
    
    // === GESTIÓN DE CARRERAS ===
    public int agregarCarrera(Carrera carrera) {
        int id = nextCarreraId++;
        carreras.put(id, carrera);
        return id;
    }
    
    public Carrera obtenerCarrera(int id) {
        return carreras.get(id);
    }
    
    public List<Carrera> obtenerTodasCarreras() {
        return new ArrayList<>(carreras.values());
    }
    
    // === GESTIÓN DE MATERIAS ===
    public int agregarMateria(Materia materia) {
        int id = nextMateriaId++;
        materias.put(id, materia);
        return id;
    }
    
    public Materia obtenerMateria(int id) {
        return materias.get(id);
    }
    
    public List<Materia> obtenerTodasMaterias() {
        return new ArrayList<>(materias.values());
    }
    
    public List<Materia> obtenerMateriasPorCarrera(Carrera carrera) {
        List<Materia> resultado = new ArrayList<>();
        for (Materia m : materias.values()) {
            if (m.getCarrera() == carrera) {
                resultado.add(m);
            }
        }
        return resultado;
    }
    
    // === GESTIÓN DE AULAS ===
    public int agregarAula(Aula aula) {
        int id = nextAulaId++;
        aulas.put(id, aula);
        return id;
    }
    
    public List<Aula> obtenerTodasAulas() {
        return new ArrayList<>(aulas.values());
    }
    
    // === GESTIÓN DE CLASES ===
    public int agregarClase(Clase clase) {
        int id = nextClaseId++;
        clases.put(id, clase);
        return id;
    }
    
    public List<Clase> obtenerTodasClases() {
        return new ArrayList<>(clases.values());
    }
    
    // === GESTIÓN DE EXÁMENES ===
    public int agregarExamen(Examen examen) {
        int id = nextExamenId++;
        examenes.put(id, examen);
        return id;
    }
    
    public List<Examen> obtenerTodosExamenes() {
        return new ArrayList<>(examenes.values());
    }
    
    // === GESTIÓN DE NOTAS ===
    public void agregarNota(Nota nota) {
        notas.add(nota);
    }
    
    public List<Nota> obtenerNotasPorEstudiante(Estudiante estudiante) {
        List<Nota> resultado = new ArrayList<>();
        for (Nota n : notas) {
            if (n.getEstudiante() == estudiante) {
                resultado.add(n);
            }
        }
        return resultado;
    }
    
    // === GESTIÓN DE ASISTENCIAS ===
    public void agregarAsistencia(Asistencia asistencia) {
        asistencias.add(asistencia);
    }
    
    public List<Asistencia> obtenerAsistenciasPorEstudiante(Estudiante estudiante) {
        List<Asistencia> resultado = new ArrayList<>();
        for (Asistencia a : asistencias) {
            if (a.getEstudiante() == estudiante) {
                resultado.add(a);
            }
        }
        return resultado;
    }
}

