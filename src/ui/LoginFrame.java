package ui;

import modelo.*;
import repositorio.SistemaUniversitario;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana de login del sistema
 */
public class LoginFrame extends JFrame {
    private JTextField legajoField;
    private JPasswordField passwordField;
    private SistemaUniversitario sistema;
    
    public LoginFrame() {
        sistema = SistemaUniversitario.getInstance();
        inicializarUI();
    }
    
    private void inicializarUI() {
        setTitle("Sistema Universitario - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Título
        JLabel titleLabel = new JLabel("Sistema Universitario", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Campo legajo
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Legajo:"), gbc);
        
        gbc.gridx = 1;
        legajoField = new JTextField(15);
        mainPanel.add(legajoField, gbc);
        
        // Campo contraseña
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);
        
        // Botón login
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Ingresar");
        loginButton.addActionListener(e -> realizarLogin());
        mainPanel.add(loginButton, gbc);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("Credenciales por defecto"));
        JTextArea infoArea = new JTextArea(3, 30);
        infoArea.setText("Administrador:\n  Legajo: admin\n  Contraseña: admin123");
        infoArea.setEditable(false);
        infoArea.setBackground(infoPanel.getBackground());
        infoPanel.add(infoArea);
        
        add(mainPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
        
        // Enter para login
        passwordField.addActionListener(e -> realizarLogin());
    }
    
    private void realizarLogin() {
        String legajo = legajoField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (legajo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario usuario = sistema.autenticar(legajo, password);
        
        if (usuario == null) {
            JOptionPane.showMessageDialog(this, 
                "Legajo o contraseña incorrectos", 
                "Error de autenticación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Abrir dashboard según tipo de usuario
        abrirDashboard(usuario);
    }
    
    private void abrirDashboard(Usuario usuario) {
        this.dispose();
        
        if (usuario instanceof Administrador) {
            new AdminDashboard((Administrador) usuario).setVisible(true);
        } else if (usuario instanceof Profesor) {
            new ProfesorDashboard((Profesor) usuario).setVisible(true);
        } else if (usuario instanceof Estudiante) {
            new EstudianteDashboard((Estudiante) usuario).setVisible(true);
        }
    }
}

