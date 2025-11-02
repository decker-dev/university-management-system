package teacher.ui;

import admin.dao.AdminCourseDAO;
import admin.dao.AdminClassroomDAO;
import teacher.service.TeacherClassSessionService;
import teacher.model.ClassSession;
import shared.model.Course;
import shared.model.Classroom;
import shared.util.SessionManager;
import shared.util.DateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;

public class ClassSessionManagementPanel extends JPanel {
    private TeacherClassSessionService sessionService;
    private AdminCourseDAO courseDAO;
    private AdminClassroomDAO classroomDAO;
    private JTable sessionTable;
    private DefaultTableModel tableModel;

    public ClassSessionManagementPanel() {
        this.sessionService = new TeacherClassSessionService();
        this.courseDAO = new AdminCourseDAO();
        this.classroomDAO = new AdminClassroomDAO();
        setLayout(new BorderLayout());
        initComponents();
        loadSessions();
    }

    private void initComponents() {
        String[] columnNames = {"ID", "Materia", "Fecha", "Hora Inicio", "Hora Fin", "Aula"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        sessionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(sessionTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Agregar Clase");
        JButton deleteButton = new JButton("Eliminar");
        JButton refreshButton = new JButton("Actualizar");

        addButton.addActionListener(e -> showAddDialog());
        deleteButton.addActionListener(e -> deleteSession());
        refreshButton.addActionListener(e -> loadSessions());

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadSessions() {
        tableModel.setRowCount(0);
        List<ClassSession> sessions = sessionService.getAllClassSessions();
        String teacherFileNumber = SessionManager.getInstance().getCurrentUser().getFileNumber();

        for (ClassSession session : sessions) {
            Course course = courseDAO.findById(session.getCourseId());
            if (course != null && teacherFileNumber.equals(course.getTeacherFileNumber())) {
                Classroom classroom = classroomDAO.findById(session.getClassroomId());
                Object[] row = {
                    session.getId(),
                    course.getName(),
                    DateUtil.formatDate(session.getDate()),
                    session.getStartTime(),
                    session.getEndTime(),
                    classroom != null ? classroom.toString() : "N/A"
                };
                tableModel.addRow(row);
            }
        }
    }

    private void showAddDialog() {
        String teacherFileNumber = SessionManager.getInstance().getCurrentUser().getFileNumber();
        List<Course> teacherCourses = courseDAO.findByTeacher(teacherFileNumber);
        
        if (teacherCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tiene materias asignadas");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Agregar Clase", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<Course> courseCombo = new JComboBox<>();
        for (Course c : teacherCourses) {
            courseCombo.addItem(c);
        }

        JTextField dateField = new JTextField("2024-01-01", 15);
        JTextField startTimeField = new JTextField("08:00", 10);
        JTextField endTimeField = new JTextField("10:00", 10);

        JComboBox<Classroom> classroomCombo = new JComboBox<>();
        List<Classroom> classrooms = classroomDAO.loadAll();
        for (Classroom c : classrooms) {
            classroomCombo.addItem(c);
        }

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Materia:"), gbc);
        gbc.gridx = 1; dialog.add(courseCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Fecha (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; dialog.add(dateField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Hora Inicio (HH:mm):"), gbc);
        gbc.gridx = 1; dialog.add(startTimeField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Hora Fin (HH:mm):"), gbc);
        gbc.gridx = 1; dialog.add(endTimeField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Aula:"), gbc);
        gbc.gridx = 1; dialog.add(classroomCombo, gbc);

        row++;
        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(e -> {
            Course course = (Course) courseCombo.getSelectedItem();
            Classroom classroom = (Classroom) classroomCombo.getSelectedItem();
            java.util.Date date = DateUtil.parseDate(dateField.getText());

            if (course == null || classroom == null || date == null) {
                JOptionPane.showMessageDialog(dialog, "Complete todos los campos correctamente");
                return;
            }

            if (sessionService.createClassSession(course.getId(), date, 
                startTimeField.getText(), endTimeField.getText(), classroom.getId())) {
                JOptionPane.showMessageDialog(dialog, "Clase creada exitosamente");
                loadSessions();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al crear clase", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        dialog.add(btnPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteSession() {
        int selectedRow = sessionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una clase para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar esta clase?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            if (sessionService.deleteClassSession(id)) {
                JOptionPane.showMessageDialog(this, "Clase eliminada exitosamente");
                loadSessions();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar clase", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

