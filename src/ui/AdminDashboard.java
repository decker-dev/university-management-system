package ui;

import modelo.*;
import repositorio.SistemaUniversitario;
import javax.swing.*;
import java.awt.*;

/**
 * Dashboard del Administrador
 */
public class AdminDashboard extends JFrame {
    private Administrador admin;
    private SistemaUniversitario sistema;
    private JTabbedPane tabbedPane;
    
    public AdminDashboard(Administrador admin) {
        this.admin = admin;
        this.sistema = SistemaUniversitario.getInstance();
        inicializarUI();
    }
    
    private void inicializarUI() {
        setTitle("Sistema Universitario - Administrador: " + admin.getNombreCompleto());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        // Pestañas
        tabbedPane.addTab("Carreras", crearPanelCarreras());
        tabbedPane.addTab("Materias", crearPanelMaterias());
        tabbedPane.addTab("Usuarios", crearPanelUsuarios());
        tabbedPane.addTab("Aulas", crearPanelAulas());
        
        add(tabbedPane);
        
        // Barra de menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Cerrar Sesión");
        itemSalir.addActionListener(e -> cerrarSesion());
        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);
    }
    
    private JPanel crearPanelCarreras() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Lista de carreras
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> carrerasList = new JList<>(listModel);
        actualizarListaCarreras(listModel);
        
        JScrollPane scrollPane = new JScrollPane(carrerasList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnNueva = new JButton("Nueva Carrera");
        btnNueva.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre de la carrera:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                String codigo = JOptionPane.showInputDialog(this, "Código de la carrera:");
                if (codigo != null && !codigo.trim().isEmpty()) {
                    Carrera carrera = admin.crearCarrera(nombre, "", codigo);
                    sistema.agregarCarrera(carrera);
                    actualizarListaCarreras(listModel);
                    JOptionPane.showMessageDialog(this, "Carrera creada exitosamente");
                }
            }
        });
        
        buttonPanel.add(btnNueva);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void actualizarListaCarreras(DefaultListModel<String> listModel) {
        listModel.clear();
        for (Carrera c : sistema.obtenerTodasCarreras()) {
            listModel.addElement(c.getNombre() + " (" + c.getCarrera() + ")");
        }
    }
    
    private JPanel crearPanelMaterias() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Lista de materias
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> materiasList = new JList<>(listModel);
        actualizarListaMaterias(listModel);
        
        JScrollPane scrollPane = new JScrollPane(materiasList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnNueva = new JButton("Nueva Materia");
        JButton btnAsignarProf = new JButton("Asignar Profesor");
        
        btnNueva.addActionListener(e -> {
            if (sistema.obtenerTodasCarreras().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Primero debe crear carreras", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String nombre = JOptionPane.showInputDialog(this, "Nombre de la materia:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                // Selector de carrera
                Carrera[] carreras = sistema.obtenerTodasCarreras().toArray(new Carrera[0]);
                Carrera carrera = (Carrera) JOptionPane.showInputDialog(this,
                    "Seleccione la carrera:",
                    "Carrera",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    carreras,
                    carreras[0]);
                
                if (carrera != null) {
                    Materia materia = admin.crearMateria(nombre, "", carrera, "");
                    sistema.agregarMateria(materia);
                    actualizarListaMaterias(listModel);
                    JOptionPane.showMessageDialog(this, "Materia creada exitosamente");
                }
            }
        });
        
        btnAsignarProf.addActionListener(e -> {
            if (sistema.obtenerTodasMaterias().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay materias disponibles", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (sistema.obtenerTodosProfesores().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay profesores disponibles", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Selector de materia
            Materia[] materias = sistema.obtenerTodasMaterias().toArray(new Materia[0]);
            Materia materia = (Materia) JOptionPane.showInputDialog(this,
                "Seleccione la materia:",
                "Asignar Profesor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                materias,
                materias[0]);
            
            if (materia != null) {
                // Selector de profesor
                Profesor[] profesores = sistema.obtenerTodosProfesores().toArray(new Profesor[0]);
                Profesor profesor = (Profesor) JOptionPane.showInputDialog(this,
                    "Seleccione el profesor:",
                    "Asignar Profesor",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    profesores,
                    profesores[0]);
                
                if (profesor != null) {
                    // Usar método del UML
                    admin.asignarProfesorAMateria(profesor, materia);
                    actualizarListaMaterias(listModel);
                    JOptionPane.showMessageDialog(this, "Profesor asignado exitosamente");
                }
            }
        });
        
        buttonPanel.add(btnNueva);
        buttonPanel.add(btnAsignarProf);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void actualizarListaMaterias(DefaultListModel<String> listModel) {
        listModel.clear();
        for (Materia m : sistema.obtenerTodasMaterias()) {
            String profesor = m.getProfesor() != null ? m.getProfesor().getNombreCompleto() : "Sin asignar";
            listModel.addElement(m.getNombre() + " - " + m.getCarrera().getNombre() + " (Prof: " + profesor + ")");
        }
    }
    
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Lista de usuarios
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> usuariosList = new JList<>(listModel);
        actualizarListaUsuarios(listModel);
        
        JScrollPane scrollPane = new JScrollPane(usuariosList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnProfesor = new JButton("Nuevo Profesor");
        btnProfesor.addActionListener(e -> crearProfesor(listModel));
        
        JButton btnEstudiante = new JButton("Nuevo Estudiante");
        btnEstudiante.addActionListener(e -> crearEstudiante(listModel));
        
        buttonPanel.add(btnProfesor);
        buttonPanel.add(btnEstudiante);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void crearProfesor(DefaultListModel<String> listModel) {
        JTextField legajoField = new JTextField();
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        
        Object[] message = {
            "Legajo:", legajoField,
            "Nombre:", nombreField,
            "Apellido:", apellidoField,
            "Email:", emailField,
            "Contraseña:", passwordField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Nuevo Profesor", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                Profesor profesor = admin.crearProfesor(
                    legajoField.getText(),
                    nombreField.getText(),
                    apellidoField.getText(),
                    emailField.getText(),
                    new String(passwordField.getPassword())
                );
                sistema.agregarUsuario(profesor);
                actualizarListaUsuarios(listModel);
                JOptionPane.showMessageDialog(this, "Profesor creado exitosamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear profesor: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void crearEstudiante(DefaultListModel<String> listModel) {
        if (sistema.obtenerTodasCarreras().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero debe crear carreras", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JTextField legajoField = new JTextField();
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        
        Carrera[] carreras = sistema.obtenerTodasCarreras().toArray(new Carrera[0]);
        JComboBox<Carrera> carreraCombo = new JComboBox<>(carreras);
        
        Object[] message = {
            "Legajo:", legajoField,
            "Nombre:", nombreField,
            "Apellido:", apellidoField,
            "Email:", emailField,
            "Contraseña:", passwordField,
            "Carrera:", carreraCombo
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Nuevo Estudiante", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                Carrera carrera = (Carrera) carreraCombo.getSelectedItem();
                Estudiante estudiante = admin.crearEstudiante(
                    legajoField.getText(),
                    nombreField.getText(),
                    apellidoField.getText(),
                    emailField.getText(),
                    new String(passwordField.getPassword()),
                    carrera
                );
                sistema.agregarUsuario(estudiante);
                actualizarListaUsuarios(listModel);
                JOptionPane.showMessageDialog(this, "Estudiante creado exitosamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear estudiante: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void actualizarListaUsuarios(DefaultListModel<String> listModel) {
        listModel.clear();
        for (Profesor p : sistema.obtenerTodosProfesores()) {
            listModel.addElement("[PROFESOR] " + p.getNombreCompleto() + " (Legajo: " + p.getLegajo() + ")");
        }
        for (Estudiante e : sistema.obtenerTodosEstudiantes()) {
            listModel.addElement("[ESTUDIANTE] " + e.getNombreCompleto() + " (Legajo: " + e.getLegajo() + 
                ") - " + e.getCarrera().getNombre());
        }
    }
    
    private JPanel crearPanelAulas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> aulasList = new JList<>(listModel);
        actualizarListaAulas(listModel);
        
        JScrollPane scrollPane = new JScrollPane(aulasList);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNueva = new JButton("Nueva Aula");
        btnNueva.addActionListener(e -> {
            JTextField sedeField = new JTextField("Campus Central");
            JTextField numeroField = new JTextField();
            JTextField pisoField = new JTextField();
            JTextField capacidadField = new JTextField();
            
            enums.TipoAula[] tipos = enums.TipoAula.values();
            JComboBox<enums.TipoAula> tipoCombo = new JComboBox<>(tipos);
            
            Object[] message = {
                "Sede:", sedeField,
                "Número:", numeroField,
                "Piso:", pisoField,
                "Tipo:", tipoCombo,
                "Capacidad:", capacidadField
            };
            
            int option = JOptionPane.showConfirmDialog(this, message, "Nueva Aula", JOptionPane.OK_CANCEL_OPTION);
            
            if (option == JOptionPane.OK_OPTION) {
                try {
                    Aula aula = admin.crearAula(
                        sedeField.getText(),
                        Integer.parseInt(numeroField.getText()),
                        Integer.parseInt(pisoField.getText()),
                        (enums.TipoAula) tipoCombo.getSelectedItem(),
                        Integer.parseInt(capacidadField.getText())
                    );
                    sistema.agregarAula(aula);
                    actualizarListaAulas(listModel);
                    JOptionPane.showMessageDialog(this, "Aula creada exitosamente");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Error: Los valores numéricos son inválidos", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        buttonPanel.add(btnNueva);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void actualizarListaAulas(DefaultListModel<String> listModel) {
        listModel.clear();
        for (Aula a : sistema.obtenerTodasAulas()) {
            listModel.addElement(a.toString() + " - Capacidad: " + a.getCapacidad());
        }
    }
    
    private void cerrarSesion() {
        this.dispose();
        new LoginFrame().setVisible(true);
    }
}

