package admin.ui;

import admin.model.Administrator;
import admin.service.AdminUserService;
import admin.service.AdminProgramService;
import shared.model.User;
import shared.model.Program;
import shared.enums.UserType;
import student.model.Student;
import teacher.model.Teacher;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private AdminUserService userService;
    private AdminProgramService programService;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, refreshButton;

    public UserManagementPanel() {
        this.userService = new AdminUserService();
        this.programService = new AdminProgramService();
        setLayout(new BorderLayout());
        initComponents();
        loadUsers();
    }

    private void initComponents() {
        // Table
        String[] columnNames = {"Legajo", "Nombre", "Apellido", "Email", "Tipo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Agregar Usuario");
        editButton = new JButton("Editar");
        deleteButton = new JButton("Eliminar");
        refreshButton = new JButton("Actualizar");

        addButton.addActionListener(e -> showAddUserDialog());
        editButton.addActionListener(e -> showEditUserDialog());
        deleteButton.addActionListener(e -> deleteUser());
        refreshButton.addActionListener(e -> loadUsers());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            Object[] row = {
                user.getFileNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUserType().name()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddUserDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Agregar Usuario", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField fileNumberField = new JTextField(15);
        JTextField firstNameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JComboBox<UserType> typeCombo = new JComboBox<>(UserType.values());
        JComboBox<Program> programCombo = new JComboBox<>();

        // Load programs
        List<Program> programs = programService.getAllPrograms();
        for (Program p : programs) {
            programCombo.addItem(p);
        }

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Legajo:"), gbc);
        gbc.gridx = 1; dialog.add(fileNumberField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; dialog.add(firstNameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1; dialog.add(lastNameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; dialog.add(emailField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; dialog.add(passwordField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; dialog.add(typeCombo, gbc);

        row++;
        JLabel programLabel = new JLabel("Carrera:");
        gbc.gridx = 0; gbc.gridy = row; dialog.add(programLabel, gbc);
        gbc.gridx = 1; dialog.add(programCombo, gbc);

        programLabel.setVisible(false);
        programCombo.setVisible(false);

        typeCombo.addActionListener(e -> {
            boolean isStudent = typeCombo.getSelectedItem() == UserType.STUDENT;
            programLabel.setVisible(isStudent);
            programCombo.setVisible(isStudent);
            dialog.pack();
        });

        row++;
        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(e -> {
            User newUser = null;
            String fileNumber = fileNumberField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            UserType type = (UserType) typeCombo.getSelectedItem();

            switch (type) {
                case ADMINISTRATOR:
                    newUser = new Administrator(fileNumber, firstName, lastName, email, password);
                    break;
                case TEACHER:
                    newUser = new Teacher(fileNumber, firstName, lastName, email, password);
                    break;
                case STUDENT:
                    Program program = (Program) programCombo.getSelectedItem();
                    if (program == null) {
                        JOptionPane.showMessageDialog(dialog, "Seleccione una carrera");
                        return;
                    }
                    newUser = new Student(fileNumber, firstName, lastName, email, password, program.getId());
                    break;
            }

            if (userService.createUser(newUser)) {
                JOptionPane.showMessageDialog(dialog, "Usuario creado exitosamente");
                loadUsers();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al crear usuario", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void showEditUserDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para editar");
            return;
        }

        String fileNumber = (String) tableModel.getValueAt(selectedRow, 0);
        User user = userService.getUserByFileNumber(fileNumber);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Usuario", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField firstNameField = new JTextField(user.getFirstName(), 15);
        JTextField lastNameField = new JTextField(user.getLastName(), 15);
        JTextField emailField = new JTextField(user.getEmail(), 15);

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; dialog.add(firstNameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1; dialog.add(lastNameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; dialog.add(emailField, gbc);

        row++;
        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(e -> {
            user.setFirstName(firstNameField.getText().trim());
            user.setLastName(lastNameField.getText().trim());
            user.setEmail(emailField.getText().trim());

            if (userService.updateUser(user)) {
                JOptionPane.showMessageDialog(dialog, "Usuario actualizado exitosamente");
                loadUsers();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al actualizar usuario", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar este usuario?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String fileNumber = (String) tableModel.getValueAt(selectedRow, 0);
            if (userService.deleteUser(fileNumber)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente");
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

