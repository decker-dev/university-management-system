package shared.ui;

import admin.ui.AdminDashboard;
import teacher.ui.TeacherDashboard;
import student.ui.StudentDashboard;
import shared.model.User;
import shared.util.SessionManager;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField fileNumberField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Sistema de Gestión Universitaria - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        JLabel fileNumberLabel = new JLabel("Legajo:");
        fileNumberField = new JTextField();
        
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField();
        
        formPanel.add(fileNumberLabel);
        formPanel.add(fileNumberField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Ingresar");
        loginButton.setPreferredSize(new Dimension(120, 30));
        loginButton.addActionListener(e -> performLogin());
        buttonPanel.add(loginButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Enter key support
        passwordField.addActionListener(e -> performLogin());

        add(mainPanel);
    }

    private void performLogin() {
        String fileNumber = fileNumberField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (fileNumber.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese legajo y contraseña",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Try to authenticate
        admin.service.AdminUserService userService = new admin.service.AdminUserService();
        User user = userService.authenticate(fileNumber, password);

        if (user != null) {
            SessionManager.getInstance().setCurrentUser(user);
            dispose();
            openDashboard(user);
        } else {
            JOptionPane.showMessageDialog(this,
                "Legajo o contraseña incorrectos",
                "Error de autenticación",
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    private void openDashboard(User user) {
        switch (user.getUserType()) {
            case ADMINISTRATOR:
                new AdminDashboard().setVisible(true);
                break;
            case TEACHER:
                new TeacherDashboard().setVisible(true);
                break;
            case STUDENT:
                new StudentDashboard().setVisible(true);
                break;
        }
    }
}

