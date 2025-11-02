package teacher.ui;

import admin.dao.AdminCourseDAO;
import teacher.service.TeacherExamService;
import teacher.model.Exam;
import shared.model.Course;
import shared.enums.ExamType;
import shared.util.SessionManager;
import shared.util.DateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ExamManagementPanel extends JPanel {
    private TeacherExamService examService;
    private AdminCourseDAO courseDAO;
    private JTable examTable;
    private DefaultTableModel tableModel;

    public ExamManagementPanel() {
        this.examService = new TeacherExamService();
        this.courseDAO = new AdminCourseDAO();
        setLayout(new BorderLayout());
        initComponents();
        loadExams();
    }

    private void initComponents() {
        String[] columnNames = {"ID", "Fecha", "Materia", "Tipo", "Cant. Preguntas"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        examTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(examTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Crear Examen");
        JButton deleteButton = new JButton("Eliminar");
        JButton refreshButton = new JButton("Actualizar");

        addButton.addActionListener(e -> showAddDialog());
        deleteButton.addActionListener(e -> deleteExam());
        refreshButton.addActionListener(e -> loadExams());

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadExams() {
        tableModel.setRowCount(0);
        String teacherFileNumber = SessionManager.getInstance().getCurrentUser().getFileNumber();
        List<Exam> exams = examService.getExamsByTeacher(teacherFileNumber);

        for (Exam exam : exams) {
            Course course = courseDAO.findById(exam.getCourseId());
            Object[] row = {
                exam.getId(),
                DateUtil.formatDate(exam.getDate()),
                course != null ? course.getName() : "N/A",
                exam.getType().name(),
                exam.getQuestionCount()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddDialog() {
        String teacherFileNumber = SessionManager.getInstance().getCurrentUser().getFileNumber();
        List<Course> teacherCourses = courseDAO.findByTeacher(teacherFileNumber);
        
        if (teacherCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tiene materias asignadas");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Crear Examen", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField dateField = new JTextField("2024-01-01", 15);
        JComboBox<Course> courseCombo = new JComboBox<>();
        for (Course c : teacherCourses) {
            courseCombo.addItem(c);
        }
        JComboBox<ExamType> typeCombo = new JComboBox<>(ExamType.values());
        JSpinner questionSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Fecha (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; dialog.add(dateField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Materia:"), gbc);
        gbc.gridx = 1; dialog.add(courseCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; dialog.add(typeCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Cantidad de Preguntas:"), gbc);
        gbc.gridx = 1; dialog.add(questionSpinner, gbc);

        row++;
        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(e -> {
            java.util.Date date = DateUtil.parseDate(dateField.getText());
            Course course = (Course) courseCombo.getSelectedItem();
            ExamType type = (ExamType) typeCombo.getSelectedItem();
            int questionCount = (int) questionSpinner.getValue();

            if (date == null || course == null) {
                JOptionPane.showMessageDialog(dialog, "Complete todos los campos correctamente");
                return;
            }

            if (examService.createExam(date, course.getId(), teacherFileNumber, type, questionCount)) {
                JOptionPane.showMessageDialog(dialog, "Examen creado exitosamente");
                loadExams();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al crear examen", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void deleteExam() {
        int selectedRow = examTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un examen para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar este examen y sus notas?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            if (examService.deleteExam(id)) {
                JOptionPane.showMessageDialog(this, "Examen eliminado exitosamente");
                loadExams();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar examen", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

