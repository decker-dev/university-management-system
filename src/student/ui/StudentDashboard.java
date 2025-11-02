package student.ui;

import shared.util.SessionManager;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {
    public StudentDashboard() {
        setTitle("Panel de Estudiante - " + SessionManager.getInstance().getCurrentUser().getFullName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Sistema de Gestión Universitaria - Estudiante");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(e -> logout());
        headerPanel.add(Box.createHorizontalStrut(20));
        headerPanel.add(logoutButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane with different panels
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Inscripción a Materias", new EnrollmentPanel());
        tabbedPane.addTab("Mis Materias", new MyCoursesPanel());
        tabbedPane.addTab("Mis Notas", new MyGradesPanel());
        tabbedPane.addTab("Mis Asistencias", new MyAttendancePanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager.getInstance().logout();
            dispose();
            new shared.ui.LoginFrame().setVisible(true);
        }
    }
}

