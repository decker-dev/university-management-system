package teacher.ui;

import admin.dao.AdminCourseDAO;
import shared.model.Course;
import shared.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyCoursesPanel extends JPanel {
    private AdminCourseDAO courseDAO;
    private JTable courseTable;
    private DefaultTableModel tableModel;

    public MyCoursesPanel() {
        this.courseDAO = new AdminCourseDAO();
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
        String teacherFileNumber = SessionManager.getInstance().getCurrentUser().getFileNumber();
        List<Course> courses = courseDAO.findByTeacher(teacherFileNumber);
        for (Course course : courses) {
            Object[] row = {course.getId(), course.getName(), course.getCode()};
            tableModel.addRow(row);
        }
    }
}

