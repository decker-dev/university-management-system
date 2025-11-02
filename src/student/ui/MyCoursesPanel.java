package student.ui;

import student.service.StudentQueryService;
import shared.model.Course;
import shared.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyCoursesPanel extends JPanel {
    private StudentQueryService queryService;
    private JTable courseTable;
    private DefaultTableModel tableModel;

    public MyCoursesPanel() {
        this.queryService = new StudentQueryService();
        setLayout(new BorderLayout());
        initComponents();
        loadCourses();
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
        JButton refreshButton = new JButton("Actualizar");
        refreshButton.addActionListener(e -> loadCourses());
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadCourses() {
        tableModel.setRowCount(0);
        String studentFileNumber = SessionManager.getInstance().getCurrentUser().getFileNumber();
        List<Course> courses = queryService.getEnrolledCourses(studentFileNumber);

        for (Course course : courses) {
            Object[] row = {course.getId(), course.getName(), course.getCode()};
            tableModel.addRow(row);
        }
    }
}

