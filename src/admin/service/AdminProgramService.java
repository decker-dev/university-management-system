package admin.service;

import admin.dao.AdminProgramDAO;
import admin.dao.AdminCourseDAO;
import shared.model.Program;
import shared.util.ValidationUtil;

import java.util.List;

public class AdminProgramService {
    private AdminProgramDAO programDAO;
    private AdminCourseDAO courseDAO;

    public AdminProgramService() {
        this.programDAO = new AdminProgramDAO();
        this.courseDAO = new AdminCourseDAO();
    }

    public boolean createProgram(String name, String code) {
        if (!ValidationUtil.isNotEmpty(name) || !ValidationUtil.isNotEmpty(code)) {
            return false;
        }

        int id = programDAO.getNextId();
        Program program = new Program(id, name, code);
        programDAO.save(program);
        return true;
    }

    public boolean updateProgram(Program program) {
        if (!ValidationUtil.isNotEmpty(program.getName()) || 
            !ValidationUtil.isNotEmpty(program.getCode())) {
            return false;
        }
        programDAO.update(program);
        return true;
    }

    public boolean deleteProgram(int id) {
        // Check if program has courses
        if (!courseDAO.findByProgramId(id).isEmpty()) {
            return false; // Cannot delete program with courses
        }

        Program program = programDAO.findById(id);
        if (program != null) {
            programDAO.delete(program);
            return true;
        }
        return false;
    }

    public List<Program> getAllPrograms() {
        return programDAO.loadAll();
    }

    public Program getProgramById(int id) {
        return programDAO.findById(id);
    }
}

