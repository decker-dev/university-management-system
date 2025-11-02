package admin.service;

import admin.dao.AdminClassroomDAO;
import shared.model.Classroom;
import shared.enums.ClassroomType;
import shared.util.ValidationUtil;

import java.util.List;

public class AdminClassroomService {
    private AdminClassroomDAO classroomDAO;

    public AdminClassroomService() {
        this.classroomDAO = new AdminClassroomDAO();
    }

    public boolean createClassroom(ClassroomType type, int number, int floor, 
                                   int capacity, String location) {
        if (!ValidationUtil.isValidCapacity(capacity) || 
            !ValidationUtil.isNotEmpty(location)) {
            return false;
        }

        int id = classroomDAO.getNextId();
        Classroom classroom = new Classroom(id, type, number, floor, capacity, location);
        classroomDAO.save(classroom);
        return true;
    }

    public boolean updateClassroom(Classroom classroom) {
        if (!ValidationUtil.isValidCapacity(classroom.getCapacity())) {
            return false;
        }
        classroomDAO.update(classroom);
        return true;
    }

    public boolean deleteClassroom(int id) {
        Classroom classroom = classroomDAO.findById(id);
        if (classroom != null) {
            classroomDAO.delete(classroom);
            return true;
        }
        return false;
    }

    public List<Classroom> getAllClassrooms() {
        return classroomDAO.loadAll();
    }

    public Classroom getClassroomById(int id) {
        return classroomDAO.findById(id);
    }
}

