package admin.ui;

import admin.service.AdminCourseService;
import admin.service.AdminProgramService;
import admin.service.AdminUserService;
import shared.model.Course;
import shared.model.Program;
import shared.model.User;
import shared.enums.UserType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourseManagementPanel extends JPanel {
    private AdminCourseService courseService;
    private AdminProgramService programService;
    private AdminUserService userService;
    private JTable courseTable;
    private DefaultTableModel tableModel;

    public CourseManagementPanel() {
        this.courseService = new AdminCourseService();
        this.programService = new AdminProgramService();
        this.userService = new AdminUserService();
        setLayout(new BorderLayout());
        initComponents();
        loadCourses();
    }

    private void initComponents() {
        String[] columnNames = {"ID", "Nombre", "Código", "Carrera", "Profesor"};
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
        JButton addButton = new JButton("Agregar Materia");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Eliminar");
        JButton assignButton = new JButton("Asignar Profesor");
        JButton refreshButton = new JButton("Actualizar");

        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteCourse());
        assignButton.addActionListener(e -> showAssignTeacherDialog());
        refreshButton.addActionListener(e -> loadCourses());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(assignButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadCourses() {
        tableModel.setRowCount(0);
        List<Course> courses = courseService.getAllCourses();
        for (Course course : courses) {
            Program program = programService.getProgramById(course.getProgramId());
            String programName = program != null ? program.getName() : "N/A";
            
            String teacherName = "Sin asignar";
            if (course.getTeacherFileNumber() != null) {
                User teacher = userService.getUserByFileNumber(course.getTeacherFileNumber());
                if (teacher != null) {
                    teacherName = teacher.getFullName();
                }
            }

            Object[] row = {
                course.getId(),
                course.getName(),
                course.getCode(),
                programName,
                teacherName
            };
            tableModel.addRow(row);
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Agregar Materia", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JTextField codeField = new JTextField(20);
        JComboBox<Program> programCombo = new JComboBox<>();

        List<Program> programs = programService.getAllPrograms();
        for (Program p : programs) {
            programCombo.addItem(p);
        }

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; dialog.add(codeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; dialog.add(new JLabel("Carrera:"), gbc);
        gbc.gridx = 1; dialog.add(programCombo, gbc);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String code = codeField.getText().trim();
            Program program = (Program) programCombo.getSelectedItem();

            if (program == null) {
                JOptionPane.showMessageDialog(dialog, "Seleccione una carrera");
                return;
            }

            if (courseService.createCourse(name, code, program.getId())) {
                JOptionPane.showMessageDialog(dialog, "Materia creada exitosamente");
                loadCourses();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al crear materia", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        dialog.add(btnPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una materia para editar");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Course course = courseService.getCourseById(id);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Materia", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(course.getName(), 20);
        JTextField codeField = new JTextField(course.getCode(), 20);

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; dialog.add(codeField, gbc);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(e -> {
            course.setName(nameField.getText().trim());
            course.setCode(codeField.getText().trim());

            if (courseService.updateCourse(course)) {
                JOptionPane.showMessageDialog(dialog, "Materia actualizada exitosamente");
                loadCourses();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al actualizar materia", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        dialog.add(btnPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una materia para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar esta materia?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            if (courseService.deleteCourse(id)) {
                JOptionPane.showMessageDialog(this, "Materia eliminada exitosamente");
                loadCourses();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar materia", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAssignTeacherDialog() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una materia");
            return;
        }

        int courseId = (int) tableModel.getValueAt(selectedRow, 0);
        Course course = courseService.getCourseById(courseId);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Asignar Profesor", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> teacherCombo = new JComboBox<>();
        List<User> teachers = userService.getUsersByType(UserType.TEACHER);
        for (User teacher : teachers) {
            teacherCombo.addItem(teacher.getFileNumber() + " - " + teacher.getFullName());
        }

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Materia:"), gbc);
        gbc.gridx = 1; dialog.add(new JLabel(course.getName()), gbc);

        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Profesor:"), gbc);
        gbc.gridx = 1; dialog.add(teacherCombo, gbc);

        JPanel btnPanel = new JPanel();
        JButton assignBtn = new JButton("Asignar");
        JButton cancelBtn = new JButton("Cancelar");

        assignBtn.addActionListener(e -> {
            if (teacherCombo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(dialog, "Seleccione un profesor");
                return;
            }

            String selected = (String) teacherCombo.getSelectedItem();
            String teacherFileNumber = selected.split(" - ")[0];

            if (courseService.assignTeacher(courseId, teacherFileNumber)) {
                JOptionPane.showMessageDialog(dialog, "Profesor asignado exitosamente");
                loadCourses();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al asignar profesor", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(assignBtn);
        btnPanel.add(cancelBtn);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        dialog.add(btnPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}

