package repositorio;

import modelo.*;
import enums.*;
import java.io.IOException;
import java.util.*;

/**
 * Repositorio en memoria que mantiene todos los datos del sistema
 * Singleton para acceso global
 * Ahora con persistencia en archivos TXT
 */
public class SistemaUniversitario {
    private static SistemaUniversitario instancia;
    private PersistenciaTXT persistencia;
    
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
        
        persistencia = new PersistenciaTXT();
        
        // Intentar cargar datos, si no existen, crear datos por defecto
        if (!cargarDatos()) {
            inicializarDatosDefault();
            guardarDatos();
        }
    }
    
    public static SistemaUniversitario getInstance() {
        if (instancia == null) {
            instancia = new SistemaUniversitario();
        }
        return instancia;
    }
    
    private void inicializarDatosDefault() {
        System.out.println("Inicializando datos por defecto...");
        
        // Crear administrador por defecto
        Administrador admin = new Administrador("admin", "Admin", "Sistema", 
            "admin@universidad.edu", "admin123");
        usuarios.put(admin.getLegajo(), admin);
        
        // Crear algunas carreras
        Carrera isi = new Carrera("Ingeniería en Sistemas", "Carrera de grado", "ISI");
        carreras.put(nextCarreraId++, isi);
        
        Carrera iin = new Carrera("Ingeniería Industrial", "Carrera de grado", "IIN");
        carreras.put(nextCarreraId++, iin);
        
        System.out.println("Datos inicializados correctamente");
    }
    
    // === PERSISTENCIA ===
    
    public boolean cargarDatos() {
        try {
            System.out.println("Cargando datos desde archivos...");
            
            // 1. Cargar carreras primero (sin dependencias)
            List<Carrera> carrerasList = persistencia.cargarCarreras();
            Map<String, Carrera> carrerasMap = new HashMap<>();
            for (Carrera c : carrerasList) {
                int id = nextCarreraId++;
                carreras.put(id, c);
                carrerasMap.put(c.getCarrera(), c);
            }
            
            // 2. Cargar aulas (sin dependencias)
            List<Aula> aulasList = persistencia.cargarAulas();
            Map<String, Aula> aulasMap = new HashMap<>();
            for (Aula a : aulasList) {
                int id = nextAulaId++;
                aulas.put(id, a);
                aulasMap.put(a.getSede() + "_" + a.getNumero(), a);
            }
            
            // 3. Cargar profesores (sin dependencias)
            List<Profesor> profesoresList = persistencia.cargarProfesores();
            Map<String, Profesor> profesoresMap = new HashMap<>();
            for (Profesor p : profesoresList) {
                usuarios.put(p.getLegajo(), p);
                profesoresMap.put(p.getLegajo(), p);
            }
            
            // 4. Cargar administradores
            List<Administrador> administradoresList = persistencia.cargarAdministradores();
            for (Administrador a : administradoresList) {
                usuarios.put(a.getLegajo(), a);
            }
            
            // 5. Cargar estudiantes (dependen de carreras)
            List<Estudiante> estudiantesList = persistencia.cargarEstudiantes(carrerasMap);
            Map<String, Estudiante> estudiantesMap = new HashMap<>();
            for (Estudiante e : estudiantesList) {
                usuarios.put(e.getLegajo(), e);
                estudiantesMap.put(e.getLegajo(), e);
            }
            
            // 6. Cargar materias (dependen de carreras y profesores)
            List<Materia> materiasList = persistencia.cargarMaterias(carrerasMap, profesoresMap);
            Map<String, Materia> materiasMap = new HashMap<>();
            for (Materia m : materiasList) {
                int id = nextMateriaId++;
                materias.put(id, m);
                materiasMap.put(m.getNombre(), m);
                
                // Agregar materia al profesor
                if (m.getProfesor() != null) {
                    m.getProfesor().agregarMateria(m);
                }
                
                // Agregar materia a la carrera
                if (m.getCarrera() != null) {
                    m.getCarrera().agregarMateria(m);
                }
            }
            
            // 7. Cargar inscripciones
            persistencia.cargarInscripciones(estudiantesMap, materiasMap);
            
            // 8. Cargar clases (dependen de materias y aulas)
            List<Clase> clasesList = persistencia.cargarClases(materiasMap, aulasMap);
            for (Clase c : clasesList) {
                int id = nextClaseId++;
                clases.put(id, c);
            }
            
            // 9. Cargar exámenes (dependen de materias)
            List<Examen> examenesList = persistencia.cargarExamenes(materiasMap);
            for (Examen e : examenesList) {
                int id = nextExamenId++;
                examenes.put(id, e);
            }
            
            // 10. Cargar notas (dependen de estudiantes y exámenes)
            notas = persistencia.cargarNotas(estudiantesMap, new ArrayList<>(examenes.values()));
            
            // 11. Cargar asistencias (dependen de estudiantes y clases)
            asistencias = persistencia.cargarAsistencias(estudiantesMap, new ArrayList<>(clases.values()));
            
            System.out.println("Datos cargados correctamente:");
            System.out.println("  - Carreras: " + carreras.size());
            System.out.println("  - Materias: " + materias.size());
            System.out.println("  - Usuarios: " + usuarios.size());
            System.out.println("  - Aulas: " + aulas.size());
            System.out.println("  - Clases: " + clases.size());
            System.out.println("  - Exámenes: " + examenes.size());
            System.out.println("  - Notas: " + notas.size());
            System.out.println("  - Asistencias: " + asistencias.size());
            
            // Si no hay usuarios, retornar false para crear datos por defecto
            return !usuarios.isEmpty();
            
        } catch (IOException e) {
            System.err.println("Error cargando datos: " + e.getMessage());
            return false;
        }
    }
    
    public void guardarDatos() {
        try {
            System.out.println("Guardando datos en archivos...");
            
            // Separar usuarios por tipo
            List<Administrador> administradores = new ArrayList<>();
            List<Profesor> profesores = new ArrayList<>();
            List<Estudiante> estudiantes = new ArrayList<>();
            
            for (Usuario u : usuarios.values()) {
                if (u instanceof Administrador) {
                    administradores.add((Administrador) u);
                } else if (u instanceof Profesor) {
                    profesores.add((Profesor) u);
                } else if (u instanceof Estudiante) {
                    estudiantes.add((Estudiante) u);
                }
            }
            
            persistencia.guardarAdministradores(administradores);
            persistencia.guardarProfesores(profesores);
            persistencia.guardarEstudiantes(estudiantes);
            persistencia.guardarCarreras(new ArrayList<>(carreras.values()));
            persistencia.guardarMaterias(new ArrayList<>(materias.values()));
            persistencia.guardarAulas(new ArrayList<>(aulas.values()));
            persistencia.guardarClases(new ArrayList<>(clases.values()));
            persistencia.guardarExamenes(new ArrayList<>(examenes.values()));
            persistencia.guardarNotas(notas);
            persistencia.guardarAsistencias(asistencias);
            persistencia.guardarInscripciones(estudiantes);
            
            System.out.println("Datos guardados exitosamente");
            
        } catch (IOException e) {
            System.err.println("Error guardando datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // === GESTIÓN DE USUARIOS ===
    public void agregarUsuario(Usuario usuario) {
        usuarios.put(usuario.getLegajo(), usuario);
        guardarDatos();
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
    
    public List<Profesor> obtenerTodosProfesores() {
        List<Profesor> profesores = new ArrayList<>();
        for (Usuario u : usuarios.values()) {
            if (u instanceof Profesor) {
                profesores.add((Profesor) u);
            }
        }
        return profesores;
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
    
    // === GESTIÓN DE CARRERAS ===
    public int agregarCarrera(Carrera carrera) {
        int id = nextCarreraId++;
        carreras.put(id, carrera);
        guardarDatos();
        return id;
    }
    
    public Carrera obtenerCarrera(int id) {
        return carreras.get(id);
    }
    
    public Carrera obtenerCarreraPorCodigo(String codigo) {
        for (Carrera c : carreras.values()) {
            if (c.getCarrera().equals(codigo)) {
                return c;
            }
        }
        return null;
    }
    
    public List<Carrera> obtenerTodasCarreras() {
        return new ArrayList<>(carreras.values());
    }
    
    // === GESTIÓN DE MATERIAS ===
    public int agregarMateria(Materia materia) {
        int id = nextMateriaId++;
        materias.put(id, materia);
        guardarDatos();
        return id;
    }
    
    public Materia obtenerMateria(int id) {
        return materias.get(id);
    }
    
    public List<Materia> obtenerTodasMaterias() {
        return new ArrayList<>(materias.values());
    }
    
    // === GESTIÓN DE AULAS ===
    public int agregarAula(Aula aula) {
        int id = nextAulaId++;
        aulas.put(id, aula);
        guardarDatos();
        return id;
    }
    
    public Aula obtenerAula(int id) {
        return aulas.get(id);
    }
    
    public List<Aula> obtenerTodasAulas() {
        return new ArrayList<>(aulas.values());
    }
    
    // === GESTIÓN DE CLASES ===
    public int agregarClase(Clase clase) {
        int id = nextClaseId++;
        clases.put(id, clase);
        guardarDatos();
        return id;
    }
    
    public List<Clase> obtenerTodasClases() {
        return new ArrayList<>(clases.values());
    }
    
    // === GESTIÓN DE EXÁMENES ===
    public int agregarExamen(Examen examen) {
        int id = nextExamenId++;
        examenes.put(id, examen);
        guardarDatos();
        return id;
    }
    
    public List<Examen> obtenerTodosExamenes() {
        return new ArrayList<>(examenes.values());
    }
    
    // === GESTIÓN DE NOTAS ===
    public void agregarNota(Nota nota) {
        notas.add(nota);
        guardarDatos();
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
        guardarDatos();
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
    
    // === GESTIÓN DE INSCRIPCIONES ===
    public void guardarInscripcion() {
        guardarDatos();
    }
}
