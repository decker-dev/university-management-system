package ui;

import modelo.*;
import enums.*;
import repositorio.SistemaUniversitario;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfesorDashboard extends JFrame {
    private Profesor profesor;
    private SistemaUniversitario sistema;
    
    public ProfesorDashboard(Profesor profesor) {
        this.profesor = profesor;
        this.sistema = SistemaUniversitario.getInstance();
        cargarMateriasDelProfesor();
        inicializarUI();
    }
    
    private void cargarMateriasDelProfesor() {
        for (Materia m : sistema.obtenerTodasMaterias()) {
            if (m.getProfesor() == profesor) {
                profesor.agregarMateria(m);
            }
        }
    }
    
    private void inicializarUI() {
        setTitle("Sistema Universitario - Profesor: " + profesor.getNombreCompleto());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Título
        JLabel lblTitulo = new JLabel("Panel del Profesor: " + profesor.getNombreCompleto());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblTitulo, BorderLayout.NORTH);
        
        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Mis Materias", crearPanelMaterias());
        tabbedPane.addTab("Crear Clases", crearPanelCrearClases());
        tabbedPane.addTab("Crear Exámenes", crearPanelCrearExamenes());
        tabbedPane.addTab("Calificar Exámenes", crearPanelCalificar());
        tabbedPane.addTab("Tomar Asistencia", crearPanelAsistencia());
        tabbedPane.addTab("Ver Estudiantes", crearPanelEstudiantes());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel crearPanelMaterias() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> materiasList = new JList<>(listModel);
        actualizarListaMaterias(listModel);
        
        JScrollPane scrollPane = new JScrollPane(materiasList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelCrearClases() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Selector de materia
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Materia:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<Materia> cmbMateria = new JComboBox<>();
        for (Materia m : profesor.getMaterias()) {
            cmbMateria.addItem(m);
        }
        panel.add(cmbMateria, gbc);
        
        // Fecha
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Fecha (dd/MM/yyyy):"), gbc);
        
        gbc.gridx = 1;
        JTextField txtFecha = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        panel.add(txtFecha, gbc);
        
        // Hora inicio
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Hora Inicio (HH:mm):"), gbc);
        
        gbc.gridx = 1;
        JTextField txtInicio = new JTextField("08:00");
        panel.add(txtInicio, gbc);
        
        // Hora fin
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Hora Fin (HH:mm):"), gbc);
        
        gbc.gridx = 1;
        JTextField txtFin = new JTextField("10:00");
        panel.add(txtFin, gbc);
        
        // Selector de aula
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Aula:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<Aula> cmbAula = new JComboBox<>();
        for (Aula a : sistema.obtenerTodasAulas()) {
            cmbAula.addItem(a);
        }
        panel.add(cmbAula, gbc);
        
        // Botón crear
        gbc.gridx = 1; gbc.gridy = 5;
        JButton btnCrear = new JButton("Crear Clase");
        btnCrear.addActionListener(e -> {
            try {
                Materia materia = (Materia) cmbMateria.getSelectedItem();
                Aula aula = (Aula) cmbAula.getSelectedItem();
                
                if (materia == null || aula == null) {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar materia y aula", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy");
                Date fecha = sdfFecha.parse(txtFecha.getText());
                
                SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
                Date inicio = sdfHora.parse(txtInicio.getText());
                Date fin = sdfHora.parse(txtFin.getText());
                
                // Usar método del UML: profesor.dictaMateria()
                Clase clase = profesor.dictaMateria(materia, fecha, inicio, fin, aula);
                sistema.agregarClase(clase);
                
                JOptionPane.showMessageDialog(this, "Clase creada exitosamente");
                txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(btnCrear, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelCrearExamenes() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Selector de materia
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Materia:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<Materia> cmbMateria = new JComboBox<>();
        for (Materia m : profesor.getMaterias()) {
            cmbMateria.addItem(m);
        }
        panel.add(cmbMateria, gbc);
        
        // Fecha
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Fecha (dd/MM/yyyy):"), gbc);
        
        gbc.gridx = 1;
        JTextField txtFecha = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        panel.add(txtFecha, gbc);
        
        // Cantidad de preguntas
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Cantidad de Preguntas:"), gbc);
        
        gbc.gridx = 1;
        JSpinner spnPreguntas = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        panel.add(spnPreguntas, gbc);
        
        // Tipo de examen
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Tipo:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<TipoExamen> cmbTipo = new JComboBox<>(TipoExamen.values());
        panel.add(cmbTipo, gbc);
        
        // Botón crear
        gbc.gridx = 1; gbc.gridy = 4;
        JButton btnCrear = new JButton("Crear Examen");
        btnCrear.addActionListener(e -> {
            try {
                Materia materia = (Materia) cmbMateria.getSelectedItem();
                
                if (materia == null) {
                    JOptionPane.showMessageDialog(this, "Debe seleccionar una materia", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fecha = sdf.parse(txtFecha.getText());
                int cantPreguntas = (Integer) spnPreguntas.getValue();
                TipoExamen tipo = (TipoExamen) cmbTipo.getSelectedItem();
                
                // Usar método del UML: profesor.crearExamen()
                Examen examen = profesor.crearExamen(materia, fecha, cantPreguntas, tipo);
                sistema.agregarExamen(examen);
                
                JOptionPane.showMessageDialog(this, "Examen creado exitosamente");
                txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(btnCrear, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelCalificar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel superior con filtros
        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel lblMateria = new JLabel("Materia:");
        JComboBox<Materia> cmbMateria = new JComboBox<>();
        cmbMateria.addItem(null); // Opción "Todas"
        for (Materia m : profesor.getMaterias()) {
            cmbMateria.addItem(m);
        }
        
        JLabel lblExamen = new JLabel("Examen:");
        JComboBox<Examen> cmbExamen = new JComboBox<>();
        
        cmbMateria.addActionListener(e -> {
            cmbExamen.removeAllItems();
            Materia materiaSeleccionada = (Materia) cmbMateria.getSelectedItem();
            if (materiaSeleccionada != null) {
                for (Examen ex : sistema.obtenerTodosExamenes()) {
                    if (ex.getMateria() == materiaSeleccionada) {
                        cmbExamen.addItem(ex);
                    }
                }
            }
        });
        
        filtrosPanel.add(lblMateria);
        filtrosPanel.add(cmbMateria);
        filtrosPanel.add(lblExamen);
        filtrosPanel.add(cmbExamen);
        
        panel.add(filtrosPanel, BorderLayout.NORTH);
        
        // Lista de estudiantes
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> estudiantesList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(estudiantesList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnCargar = new JButton("Cargar Estudiantes");
        btnCargar.addActionListener(e -> {
            Materia materia = (Materia) cmbMateria.getSelectedItem();
            if (materia == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una materia", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            listModel.clear();
            for (Estudiante est : sistema.obtenerTodosEstudiantes()) {
                // Usar método del UML: profesor.esEstudiante()
                if (profesor.esEstudiante(est, materia)) {
                    listModel.addElement(est.getLegajo() + " - " + est.getNombreCompleto());
                }
            }
        });
        
        JButton btnCalificar = new JButton("Calificar Estudiante");
        btnCalificar.addActionListener(e -> {
            String seleccionado = estudiantesList.getSelectedValue();
            Examen examen = (Examen) cmbExamen.getSelectedItem();
            
            if (seleccionado == null || examen == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un estudiante y un examen", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String legajo = seleccionado.split(" - ")[0];
            Estudiante estudiante = null;
            
            // Buscar estudiante por legajo
            for (Estudiante est : sistema.obtenerTodosEstudiantes()) {
                if (est.getLegajo().equals(legajo)) {
                    estudiante = est;
                    break;
                }
            }
            
            if (estudiante == null) {
                JOptionPane.showMessageDialog(this, "Estudiante no encontrado", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String notaStr = JOptionPane.showInputDialog(this, 
                "Ingrese la nota (0-10) para " + estudiante.getNombreCompleto() + ":");
            
            if (notaStr != null && !notaStr.trim().isEmpty()) {
                try {
                    float nota = Float.parseFloat(notaStr);
                    
                    if (nota < 0 || nota > 10) {
                        JOptionPane.showMessageDialog(this, "La nota debe estar entre 0 y 10", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Usar método del UML: profesor.calificarExamen()
                    Nota notaObj = profesor.calificarExamen(estudiante, examen, nota);
                    sistema.agregarNota(notaObj);
                    
                    JOptionPane.showMessageDialog(this, "Examen calificado exitosamente");
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Nota inválida", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        buttonPanel.add(btnCargar);
        buttonPanel.add(btnCalificar);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelAsistencia() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel superior con filtros
        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel lblClase = new JLabel("Clase:");
        JComboBox<Clase> cmbClase = new JComboBox<>();
        for (Clase c : sistema.obtenerTodasClases()) {
            if (c.getMateria().getProfesor() == profesor) {
                cmbClase.addItem(c);
            }
        }
        
        filtrosPanel.add(lblClase);
        filtrosPanel.add(cmbClase);
        
        panel.add(filtrosPanel, BorderLayout.NORTH);
        
        // Lista de estudiantes con checkboxes
        JPanel estudiantesPanel = new JPanel();
        estudiantesPanel.setLayout(new BoxLayout(estudiantesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(estudiantesPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Botón para cargar estudiantes
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnCargar = new JButton("Cargar Estudiantes");
        btnCargar.addActionListener(e -> {
            Clase clase = (Clase) cmbClase.getSelectedItem();
            if (clase == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una clase", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            estudiantesPanel.removeAll();
            
            for (Estudiante est : sistema.obtenerTodosEstudiantes()) {
                if (profesor.esEstudiante(est, clase.getMateria())) {
                    JCheckBox chk = new JCheckBox(est.getLegajo() + " - " + est.getNombreCompleto());
                    chk.putClientProperty("estudiante", est);
                    estudiantesPanel.add(chk);
                }
            }
            
            estudiantesPanel.revalidate();
            estudiantesPanel.repaint();
        });
        
        JButton btnGuardar = new JButton("Guardar Asistencias");
        btnGuardar.addActionListener(e -> {
            Clase clase = (Clase) cmbClase.getSelectedItem();
            if (clase == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una clase", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int guardadas = 0;
            for (Component comp : estudiantesPanel.getComponents()) {
                if (comp instanceof JCheckBox) {
                    JCheckBox chk = (JCheckBox) comp;
                    Estudiante est = (Estudiante) chk.getClientProperty("estudiante");
                    
                    if (est != null) {
                        boolean asistio = chk.isSelected();
                        Asistencia asistencia = new Asistencia(asistio, est, clase);
                        sistema.agregarAsistencia(asistencia);
                        guardadas++;
                    }
                }
            }
            
            JOptionPane.showMessageDialog(this, "Se guardaron " + guardadas + " asistencias");
        });
        
        buttonPanel.add(btnCargar);
        buttonPanel.add(btnGuardar);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelEstudiantes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel superior con selector de materia
        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel lblMateria = new JLabel("Materia:");
        JComboBox<Materia> cmbMateria = new JComboBox<>();
        cmbMateria.addItem(null); // Opción "Todas"
        for (Materia m : profesor.getMaterias()) {
            cmbMateria.addItem(m);
        }
        
        filtrosPanel.add(lblMateria);
        filtrosPanel.add(cmbMateria);
        
        panel.add(filtrosPanel, BorderLayout.NORTH);
        
        // Lista de estudiantes
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> estudiantesList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(estudiantesList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Botón para cargar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnCargar = new JButton("Cargar Estudiantes");
        btnCargar.addActionListener(e -> {
            Materia materia = (Materia) cmbMateria.getSelectedItem();
            
            listModel.clear();
            
            if (materia == null) {
                // Mostrar todos los estudiantes de todas las materias
                for (Materia m : profesor.getMaterias()) {
                    listModel.addElement("=== " + m.getNombre() + " ===");
                    for (Estudiante est : sistema.obtenerTodosEstudiantes()) {
                        // Usar método del UML: profesor.esEstudiante()
                        if (profesor.esEstudiante(est, m)) {
                            listModel.addElement("  " + est.getLegajo() + " - " + est.getNombreCompleto());
                        }
                    }
                }
            } else {
                // Mostrar estudiantes de la materia seleccionada
                for (Estudiante est : sistema.obtenerTodosEstudiantes()) {
                    // Usar método del UML: profesor.esEstudiante()
                    if (profesor.esEstudiante(est, materia)) {
                        listModel.addElement(est.getLegajo() + " - " + est.getNombreCompleto());
                    }
                }
            }
        });
        
        buttonPanel.add(btnCargar);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void actualizarListaMaterias(DefaultListModel<String> listModel) {
        listModel.clear();
        if (profesor.getMaterias().isEmpty()) {
            listModel.addElement("No tiene materias asignadas");
        } else {
            for (Materia m : profesor.getMaterias()) {
                int cantEstudiantes = 0;
                for (Estudiante est : sistema.obtenerTodosEstudiantes()) {
                    if (profesor.esEstudiante(est, m)) {
                        cantEstudiantes++;
                    }
                }
                listModel.addElement(m.getNombre() + " (" + cantEstudiantes + " estudiantes)");
            }
        }
    }
}
