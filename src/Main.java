import admin.model.Administrator;
import admin.service.AdminUserService;
import admin.service.AdminProgramService;
import admin.service.AdminCourseService;
import admin.service.AdminClassroomService;
import shared.util.FileUtil;
import shared.ui.LoginFrame;
import shared.enums.ClassroomType;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Ensure data directories exist
        FileUtil.ensureDirectoriesExist();

        // Initialize default data
        initializeDefaultData();

        // Start the application
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    private static void initializeDefaultData() {
        AdminUserService userService = new AdminUserService();
        AdminProgramService programService = new AdminProgramService();
        AdminCourseService courseService = new AdminCourseService();
        AdminClassroomService classroomService = new AdminClassroomService();

        // Create default administrator if not exists
        if (userService.getUserByFileNumber("admin") == null) {
            Administrator admin = new Administrator("admin", "Admin", "System", 
                "admin@university.edu", "admin123");
            userService.createUser(admin);
            System.out.println("Default administrator created:");
            System.out.println("  Legajo: admin");
            System.out.println("  Password: admin123");
        }

        // Create some sample programs (carreras)
        if (programService.getAllPrograms().isEmpty()) {
            programService.createProgram("Ingeniería en Sistemas", "ISI");
            programService.createProgram("Ingeniería Industrial", "IIN");
            programService.createProgram("Licenciatura en Administración", "LAD");
            System.out.println("Sample programs created");
        }

        // Create some sample classrooms
        if (classroomService.getAllClassrooms().isEmpty()) {
            classroomService.createClassroom(ClassroomType.THEORY, 101, 1, 40, "Campus Central");
            classroomService.createClassroom(ClassroomType.THEORY, 102, 1, 35, "Campus Central");
            classroomService.createClassroom(ClassroomType.LABORATORY, 201, 2, 25, "Campus Central");
            classroomService.createClassroom(ClassroomType.LABORATORY, 202, 2, 25, "Campus Central");
            classroomService.createClassroom(ClassroomType.AUDITORIUM, 301, 3, 100, "Campus Central");
            System.out.println("Sample classrooms created");
        }

        System.out.println("System initialized successfully!");
        System.out.println("================================================");
        System.out.println("Welcome to University Management System");
        System.out.println("================================================");
        System.out.println();
        System.out.println("To get started, login with:");
        System.out.println("  Legajo: admin");
        System.out.println("  Password: admin123");
        System.out.println();
        System.out.println("As administrator, you can:");
        System.out.println("  - Create users (teachers and students)");
        System.out.println("  - Create and manage programs");
        System.out.println("  - Create and manage courses");
        System.out.println("  - Assign teachers to courses");
        System.out.println("  - Manage classrooms");
        System.out.println();
    }
}
