package ui;

import modelo.*;
import repositorio.SistemaUniversitario;
import excepciones.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EstudianteDashboard extends JFrame {
    private Estudiante estudiante;
    private SistemaUniversitario sistema;
    
    public EstudianteDashboard(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.sistema = SistemaUniversitario.getInstance();
        inicializarUI();
    }
    
    private void inicializarUI() {
        setTitle("Sistema Universitario - Estudiante: " + estudiante.getNombreCompleto());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Título
        JLabel lblTitulo = new JLabel("Panel del Estudiante: " + estudiante.getNombreCompleto());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel tituloPanel = new JPanel(new BorderLayout());
        tituloPanel.add(lblTitulo, BorderLayout.CENTER);
        
        // Info de carrera
        if (estudiante.getCarrera() != null) {
            JLabel lblCarrera = new JLabel("Carrera: " + estudiante.getCarrera().getNombre());
            lblCarrera.setFont(new Font("Arial", Font.PLAIN, 12));
            lblCarrera.setHorizontalAlignment(SwingConstants.CENTER);
            tituloPanel.add(lblCarrera, BorderLayout.SOUTH);
        }
        
        mainPanel.add(tituloPanel, BorderLayout.NORTH);
        
        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Mis Materias", crearPanelMisMaterias());
        tabbedPane.addTab("Inscripción", crearPanelInscripcion());
        tabbedPane.addTab("Mis Notas", crearPanelNotas());
        tabbedPane.addTab("Mis Asistencias", crearPanelAsistencias());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel crearPanelMisMaterias() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitulo = new JLabel("Materias en las que estoy inscripto:");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> materiasList = new JList<>(listModel);
        actualizarListaMisMateriasInscriptas(listModel);
        
        JScrollPane scrollPane = new JScrollPane(materiasList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelInscripcion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitulo = new JLabel("Materias disponibles para inscripción:");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        DefaultListModel<Materia> listModel = new DefaultListModel<>();
        JList<Materia> materiasList = new JList<>(listModel);
        actualizarListaMateriasDisponibles(listModel);
        
        JScrollPane scrollPane = new JScrollPane(materiasList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnInscribir = new JButton("Inscribirse");
        btnInscribir.addActionListener(e -> {
            Materia materia = materiasList.getSelectedValue();
            
            if (materia == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una materia", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Usar método del UML: estudiante.inscribirseEnCurso()
                estudiante.inscribirseEnCurso(materia);
                
                // Guardar inscripción
                sistema.guardarInscripcion();
                
                JOptionPane.showMessageDialog(this, 
                    "¡Inscripción exitosa en " + materia.getNombre() + "!");
                
                // Actualizar ambas listas
                actualizarListaMateriasDisponibles(listModel);
                
            } catch (InscripcionException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error de inscripción: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error inesperado: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> {
            actualizarListaMateriasDisponibles(listModel);
        });
        
        buttonPanel.add(btnInscribir);
        buttonPanel.add(btnActualizar);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelNotas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitulo = new JLabel("Mis Notas:");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> notasList = new JList<>(listModel);
        actualizarListaNotas(listModel);
        
        JScrollPane scrollPane = new JScrollPane(notasList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Botón para actualizar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarListaNotas(listModel));
        buttonPanel.add(btnActualizar);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelAsistencias() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitulo = new JLabel("Mis Asistencias:");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> asistenciasList = new JList<>(listModel);
        actualizarListaAsistencias(listModel);
        
        JScrollPane scrollPane = new JScrollPane(asistenciasList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Botón para actualizar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarListaAsistencias(listModel));
        buttonPanel.add(btnActualizar);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void actualizarListaMisMateriasInscriptas(DefaultListModel<String> listModel) {
        listModel.clear();
        
        // Usar método del UML: estudiante.materiasInscriptas(legajo)
        List<Materia> misMateriasInscriptas = estudiante.materiasInscriptas(estudiante.getLegajo());
        
        if (misMateriasInscriptas.isEmpty()) {
            listModel.addElement("No estás inscripto en ninguna materia");
        } else {
            for (Materia m : misMateriasInscriptas) {
                String profesor = m.getProfesor() != null ? m.getProfesor().getNombreCompleto() : "Sin profesor";
                listModel.addElement(m.getNombre() + " - Profesor: " + profesor);
            }
        }
    }
    
    private void actualizarListaMateriasDisponibles(DefaultListModel<Materia> listModel) {
        listModel.clear();
        
        if (estudiante.getCarrera() == null) {
            return;
        }
        
        // Usar método del UML: estudiante.verMaterias()
        List<Materia> materiasDisponibles = estudiante.verMaterias();
        
        if (materiasDisponibles.isEmpty()) {
            // No se puede agregar string a DefaultListModel<Materia>
            // En su lugar, simplemente no agregamos nada
        } else {
            for (Materia m : materiasDisponibles) {
                listModel.addElement(m);
            }
        }
    }
    
    private void actualizarListaNotas(DefaultListModel<String> listModel) {
        listModel.clear();
        
        List<Nota> misNotas = sistema.obtenerNotasPorEstudiante(estudiante);
        
        if (misNotas.isEmpty()) {
            listModel.addElement("No tienes notas registradas");
        } else {
            for (Nota nota : misNotas) {
                Examen examen = nota.getExamen();
                String tipo = examen.getTipo().toString();
                String materia = examen.getMateria().getNombre();
                double calificacion = nota.getNota();
                
                listModel.addElement(String.format("%s - %s: %.2f (Tipo: %s)", 
                    materia, examen.toString(), calificacion, tipo));
            }
        }
    }
    
    private void actualizarListaAsistencias(DefaultListModel<String> listModel) {
        listModel.clear();
        
        List<Asistencia> misAsistencias = sistema.obtenerAsistenciasPorEstudiante(estudiante);
        
        if (misAsistencias.isEmpty()) {
            listModel.addElement("No tienes asistencias registradas");
        } else {
            for (Asistencia asistencia : misAsistencias) {
                Clase clase = asistencia.getClase();
                String materia = clase.getMateria().getNombre();
                String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(clase.getFecha());
                String estado = asistencia.isAsistio() ? "PRESENTE" : "AUSENTE";
                
                listModel.addElement(String.format("%s - %s - %s", 
                    materia, fecha, estado));
            }
        }
    }
}
