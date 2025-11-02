package repositorio;

import modelo.*;
import enums.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Clase para manejar la persistencia de datos en archivos TXT
 */
public class PersistenciaTXT {
    private static final String CARPETA_DATOS = "datos";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
    
    // Nombres de archivos
    private static final String ARCHIVO_ADMINISTRADORES = "administradores.txt";
    private static final String ARCHIVO_PROFESORES = "profesores.txt";
    private static final String ARCHIVO_ESTUDIANTES = "estudiantes.txt";
    private static final String ARCHIVO_CARRERAS = "carreras.txt";
    private static final String ARCHIVO_MATERIAS = "materias.txt";
    private static final String ARCHIVO_AULAS = "aulas.txt";
    private static final String ARCHIVO_CLASES = "clases.txt";
    private static final String ARCHIVO_EXAMENES = "examenes.txt";
    private static final String ARCHIVO_NOTAS = "notas.txt";
    private static final String ARCHIVO_ASISTENCIAS = "asistencias.txt";
    private static final String ARCHIVO_INSCRIPCIONES = "inscripciones.txt";
    
    public PersistenciaTXT() {
        crearCarpetaDatos();
    }
    
    private void crearCarpetaDatos() {
        File carpeta = new File(CARPETA_DATOS);
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }
    
    // ============ GUARDAR DATOS ============
    
    public void guardarAdministradores(List<Administrador> administradores) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CARPETA_DATOS + "/" + ARCHIVO_ADMINISTRADORES))) {
            for (Administrador admin : administradores) {
                pw.println(admin.getLegajo() + "|" + 
                          admin.getNombre() + "|" + 
                          admin.getApellido() + "|" + 
                          admin.getEmail() + "|" + 
                          admin.getPassword());
            }
        }
    }
    
    public void guardarProfesores(List<Profesor> profesores) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CARPETA_DATOS + "/" + ARCHIVO_PROFESORES))) {
            for (Profesor prof : profesores) {
                pw.println(prof.getLegajo() + "|" + 
                          prof.getNombre() + "|" + 
                          prof.getApellido() + "|" + 
                          prof.getEmail() + "|" + 
                          prof.getPassword());
            }
        }
    }
    
    public void guardarEstudiantes(List<Estudiante> estudiantes) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CARPETA_DATOS + "/" + ARCHIVO_ESTUDIANTES))) {
            for (Estudiante est : estudiantes) {
                String codigoCarrera = est.getCarrera() != null ? est.getCarrera().getCarrera() : "NULL";
                pw.println(est.getLegajo() + "|" + 
                          est.getNombre() + "|" + 
                          est.getApellido() + "|" + 
                          est.getEmail() + "|" + 
                          est.getPassword() + "|" + 
                          codigoCarrera);
            }
        }
    }
    
    public void guardarCarreras(List<Carrera> carreras) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CARPETA_DATOS + "/" + ARCHIVO_CARRERAS))) {
            for (Carrera carrera : carreras) {
                pw.println(carrera.getCarrera() + "|" + 
                          carrera.getNombre() + "|" + 
                          carrera.getDescripcion());
            }
        }
    }
    
    public void guardarMaterias(List<Materia> materias) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CARPETA_DATOS + "/" + ARCHIVO_MATERIAS))) {
            for (Materia materia : materias) {
                String codigoCarrera = materia.getCarrera() != null ? materia.getCarrera().getCarrera() : "NULL";
                String legajoProfesor = materia.getProfesor() != null ? materia.getProfesor().getLegajo() : "NULL";
                pw.println(materia.getNombre() + "|" + 
                          materia.getDescripcion() + "|" + 
                          codigoCarrera + "|" + 
                          legajoProfesor);
            }
        }
    }
    
    public void guardarAulas(List<Aula> aulas) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CARPETA_DATOS + "/" + ARCHIVO_AULAS))) {
            for (Aula aula : aulas) {
                pw.println(aula.getSede() + "|" + 
                          aula.getNumero() + "|" + 
                          aula.getPiso() + "|" + 
                          aula.getTipoAula().name() + "|" + 
                          aula.getCapacidad());
            }
        }
    }
    
    public void guardarClases(List<Clase> clases) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CARPETA_DATOS + "/" + ARCHIVO_CLASES))) {
            for (Clase clase : clases) {
                String nombreMateria = clase.getMateria() != null ? clase.getMateria().getNombre() : "NULL";
                String sede = clase.getAula() != null ? clase.getAula().getSede() : "NULL";
                int numero = clase.getAula() != null ? clase.getAula().getNumero() : 0;
                
                pw.println(sdfFecha.format(clase.getFecha()) + "|" + 
                          sdfHora.format(clase.getInicio()) + "|" + 
                          sdfHora.format(clase.getFin()) + "|" + 
                          nombreMateria + "|" + 
                          sede + "|" + 
                          numero);
            }
        }
    }
    
    public void guardarExamenes(List<Examen> examenes) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CARPETA_DATOS + "/" + ARCHIVO_EXAMENES))) {
            for (Examen examen : examenes) {
                String nombreMateria = examen.getMateria() != null ? examen.getMateria().getNombre() : "NULL";
                pw.println(sdfFecha.format(examen.getFecha()) + "|" + 
                          examen.getCantidadPreguntas() + "|" + 
                          examen.getTipo().name() + "|" + 
                          nombreMateria);
            }
        }
    }
    
    public void guardarNotas(List<Nota> notas) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CARPETA_DATOS + "/" + ARCHIVO_NOTAS))) {
            for (Nota nota : notas) {
                String legajoEstudiante = nota.getEstudiante() != null ? nota.getEstudiante().getLegajo() : "NULL";
                String fechaExamen = nota.getExamen() != null ? sdfFecha.format(nota.getExamen().getFecha()) : "NULL";
                String tipoExamen = nota.getExamen() != null ? nota.getExamen().getTipo().name() : "NULL";
                String nombreMateria = nota.getExamen() != null && nota.getExamen().getMateria() != null ? 
                                      nota.getExamen().getMateria().getNombre() : "NULL";
                
                pw.println(legajoEstudiante + "|" + 
                          fechaExamen + "|" + 
                          tipoExamen + "|" + 
                          nombreMateria + "|" + 
                          nota.getNota());
            }
        }
    }
    
    public void guardarAsistencias(List<Asistencia> asistencias) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CARPETA_DATOS + "/" + ARCHIVO_ASISTENCIAS))) {
            for (Asistencia asistencia : asistencias) {
                String legajoEstudiante = asistencia.getEstudiante() != null ? asistencia.getEstudiante().getLegajo() : "NULL";
                String fechaClase = asistencia.getClase() != null ? sdfFecha.format(asistencia.getClase().getFecha()) : "NULL";
                String nombreMateria = asistencia.getClase() != null && asistencia.getClase().getMateria() != null ? 
                                      asistencia.getClase().getMateria().getNombre() : "NULL";
                
                pw.println(asistencia.isAsistio() + "|" + 
                          legajoEstudiante + "|" + 
                          fechaClase + "|" + 
                          nombreMateria);
            }
        }
    }
    
    public void guardarInscripciones(List<Estudiante> estudiantes) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CARPETA_DATOS + "/" + ARCHIVO_INSCRIPCIONES))) {
            for (Estudiante est : estudiantes) {
                for (Materia materia : est.materiasInscriptas(est.getLegajo())) {
                    pw.println(est.getLegajo() + "|" + materia.getNombre());
                }
            }
        }
    }
    
    // ============ CARGAR DATOS ============
    
    public List<Administrador> cargarAdministradores() throws IOException {
        List<Administrador> lista = new ArrayList<>();
        File archivo = new File(CARPETA_DATOS + "/" + ARCHIVO_ADMINISTRADORES);
        
        if (!archivo.exists()) {
            return lista;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 5) {
                    Administrador admin = new Administrador(partes[0], partes[1], partes[2], partes[3], partes[4]);
                    lista.add(admin);
                }
            }
        }
        
        return lista;
    }
    
    public List<Profesor> cargarProfesores() throws IOException {
        List<Profesor> lista = new ArrayList<>();
        File archivo = new File(CARPETA_DATOS + "/" + ARCHIVO_PROFESORES);
        
        if (!archivo.exists()) {
            return lista;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 5) {
                    Profesor prof = new Profesor(partes[0], partes[1], partes[2], partes[3], partes[4]);
                    lista.add(prof);
                }
            }
        }
        
        return lista;
    }
    
    public List<Estudiante> cargarEstudiantes(Map<String, Carrera> carreras) throws IOException {
        List<Estudiante> lista = new ArrayList<>();
        File archivo = new File(CARPETA_DATOS + "/" + ARCHIVO_ESTUDIANTES);
        
        if (!archivo.exists()) {
            return lista;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 6) {
                    Carrera carrera = carreras.get(partes[5]);
                    Estudiante est = new Estudiante(partes[0], partes[1], partes[2], partes[3], partes[4], carrera);
                    lista.add(est);
                }
            }
        }
        
        return lista;
    }
    
    public List<Carrera> cargarCarreras() throws IOException {
        List<Carrera> lista = new ArrayList<>();
        File archivo = new File(CARPETA_DATOS + "/" + ARCHIVO_CARRERAS);
        
        if (!archivo.exists()) {
            return lista;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 3) {
                    Carrera carrera = new Carrera(partes[1], partes[2], partes[0]);
                    lista.add(carrera);
                }
            }
        }
        
        return lista;
    }
    
    public List<Materia> cargarMaterias(Map<String, Carrera> carreras, Map<String, Profesor> profesores) throws IOException {
        List<Materia> lista = new ArrayList<>();
        File archivo = new File(CARPETA_DATOS + "/" + ARCHIVO_MATERIAS);
        
        if (!archivo.exists()) {
            return lista;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 4) {
                    Carrera carrera = carreras.get(partes[2]);
                    Profesor profesor = profesores.get(partes[3]);
                    
                    Materia materia = new Materia(partes[0], partes[1], carrera, profesor);
                    lista.add(materia);
                }
            }
        }
        
        return lista;
    }
    
    public List<Aula> cargarAulas() throws IOException {
        List<Aula> lista = new ArrayList<>();
        File archivo = new File(CARPETA_DATOS + "/" + ARCHIVO_AULAS);
        
        if (!archivo.exists()) {
            return lista;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 5) {
                    TipoAula tipo = TipoAula.valueOf(partes[3]);
                    int numero = Integer.parseInt(partes[1]);
                    int piso = Integer.parseInt(partes[2]);
                    int capacidad = Integer.parseInt(partes[4]);
                    
                    Aula aula = new Aula(partes[0], numero, piso, tipo, capacidad);
                    lista.add(aula);
                }
            }
        }
        
        return lista;
    }
    
    public List<Clase> cargarClases(Map<String, Materia> materias, Map<String, Aula> aulas) throws IOException {
        List<Clase> lista = new ArrayList<>();
        File archivo = new File(CARPETA_DATOS + "/" + ARCHIVO_CLASES);
        
        if (!archivo.exists()) {
            return lista;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 6) {
                    try {
                        Date fecha = sdfFecha.parse(partes[0]);
                        Date inicio = sdfHora.parse(partes[1]);
                        Date fin = sdfHora.parse(partes[2]);
                        Materia materia = materias.get(partes[3]);
                        String claveAula = partes[4] + "_" + partes[5];
                        Aula aula = aulas.get(claveAula);
                        
                        if (materia != null && aula != null) {
                            Clase clase = new Clase(fecha, inicio, fin, materia, aula);
                            lista.add(clase);
                        }
                    } catch (ParseException e) {
                        System.err.println("Error parseando fecha en clase: " + linea);
                    }
                }
            }
        }
        
        return lista;
    }
    
    public List<Examen> cargarExamenes(Map<String, Materia> materias) throws IOException {
        List<Examen> lista = new ArrayList<>();
        File archivo = new File(CARPETA_DATOS + "/" + ARCHIVO_EXAMENES);
        
        if (!archivo.exists()) {
            return lista;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 4) {
                    try {
                        Date fecha = sdfFecha.parse(partes[0]);
                        int cantPreguntas = Integer.parseInt(partes[1]);
                        TipoExamen tipo = TipoExamen.valueOf(partes[2]);
                        Materia materia = materias.get(partes[3]);
                        
                        if (materia != null) {
                            Profesor profesor = materia.getProfesor();
                            Examen examen = new Examen(fecha, cantPreguntas, materia, profesor, tipo);
                            lista.add(examen);
                        }
                    } catch (ParseException e) {
                        System.err.println("Error parseando fecha en examen: " + linea);
                    }
                }
            }
        }
        
        return lista;
    }
    
    public List<Nota> cargarNotas(Map<String, Estudiante> estudiantes, 
                                   List<Examen> examenes) throws IOException {
        List<Nota> lista = new ArrayList<>();
        File archivo = new File(CARPETA_DATOS + "/" + ARCHIVO_NOTAS);
        
        if (!archivo.exists()) {
            return lista;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 5) {
                    try {
                        Estudiante estudiante = estudiantes.get(partes[0]);
                        Date fechaExamen = sdfFecha.parse(partes[1]);
                        TipoExamen tipo = TipoExamen.valueOf(partes[2]);
                        String nombreMateria = partes[3];
                        double nota = Double.parseDouble(partes[4]);
                        
                        // Buscar el examen correspondiente
                        Examen examen = null;
                        for (Examen ex : examenes) {
                            if (ex.getTipo() == tipo && 
                                sdfFecha.format(ex.getFecha()).equals(sdfFecha.format(fechaExamen)) &&
                                ex.getMateria().getNombre().equals(nombreMateria)) {
                                examen = ex;
                                break;
                            }
                        }
                        
                        if (estudiante != null && examen != null) {
                            Nota notaObj = new Nota(estudiante, examen, nota);
                            lista.add(notaObj);
                        }
                    } catch (ParseException e) {
                        System.err.println("Error parseando fecha en nota: " + linea);
                    }
                }
            }
        }
        
        return lista;
    }
    
    public List<Asistencia> cargarAsistencias(Map<String, Estudiante> estudiantes, 
                                               List<Clase> clases) throws IOException {
        List<Asistencia> lista = new ArrayList<>();
        File archivo = new File(CARPETA_DATOS + "/" + ARCHIVO_ASISTENCIAS);
        
        if (!archivo.exists()) {
            return lista;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 4) {
                    try {
                        boolean asistio = Boolean.parseBoolean(partes[0]);
                        Estudiante estudiante = estudiantes.get(partes[1]);
                        Date fechaClase = sdfFecha.parse(partes[2]);
                        String nombreMateria = partes[3];
                        
                        // Buscar la clase correspondiente
                        Clase clase = null;
                        for (Clase c : clases) {
                            if (sdfFecha.format(c.getFecha()).equals(sdfFecha.format(fechaClase)) &&
                                c.getMateria().getNombre().equals(nombreMateria)) {
                                clase = c;
                                break;
                            }
                        }
                        
                        if (estudiante != null && clase != null) {
                            Asistencia asistencia = new Asistencia(asistio, estudiante, clase);
                            lista.add(asistencia);
                        }
                    } catch (ParseException e) {
                        System.err.println("Error parseando fecha en asistencia: " + linea);
                    }
                }
            }
        }
        
        return lista;
    }
    
    public void cargarInscripciones(Map<String, Estudiante> estudiantes, 
                                     Map<String, Materia> materias) throws IOException {
        File archivo = new File(CARPETA_DATOS + "/" + ARCHIVO_INSCRIPCIONES);
        
        if (!archivo.exists()) {
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes.length == 2) {
                    Estudiante estudiante = estudiantes.get(partes[0]);
                    Materia materia = materias.get(partes[1]);
                    
                    if (estudiante != null && materia != null) {
                        try {
                            estudiante.inscribirseEnCurso(materia);
                        } catch (Exception e) {
                            // Ya está inscripto, ignorar
                        }
                    }
                }
            }
        }
    }
}

