package student.ui;

import student.service.StudentEnrollmentService;
import student.model.Student;
import shared.model.Course;
import shared.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnrollmentPanel extends JPanel {
    private StudentEnrollmentService enrollmentService;
    private JTable courseTable;
    private DefaultTableModel tableModel;

    public EnrollmentPanel() {
        this.enrollmentService = new StudentEnrollmentService();
        setLayout(new BorderLayout());
        initComponents();
        loadAvailableCourses();
    }

    private void initComponents() {
        String[] columnNames = {"ID", "Nombre", "Código"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        courseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton enrollButton = new JButton("Inscribirse");
        JButton refreshButton = new JButton("Actualizar");

        enrollButton.addActionListener(e -> enrollInCourse());
        refreshButton.addActionListener(e -> loadAvailableCourses());

        buttonPanel.add(enrollButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAvailableCourses() {
        tableModel.setRowCount(0);
        Student student = (Student) SessionManager.getInstance().getCurrentUser();
        List<Course> courses = enrollmentService.getAvailableCoursesForStudent(student);

        for (Course course : courses) {
            Object[] row = {course.getId(), course.getName(), course.getCode()};
            tableModel.addRow(row);
        }
    }

    private void enrollInCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una materia");
            return;
        }

        int courseId = (int) tableModel.getValueAt(selectedRow, 0);
        Student student = (Student) SessionManager.getInstance().getCurrentUser();

        if (enrollmentService.enrollStudent(student, courseId)) {
            JOptionPane.showMessageDialog(this, "Inscripción exitosa");
            loadAvailableCourses();
        } else {
            JOptionPane.showMessageDialog(this, "Error en la inscripción", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

