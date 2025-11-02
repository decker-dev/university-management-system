package admin.dao;

import shared.dao.BaseDAO;
import shared.model.Classroom;
import shared.enums.ClassroomType;
import shared.util.FileUtil;

public class AdminClassroomDAO extends BaseDAO<Classroom> {
    private static final String FILE_PATH = FileUtil.ADMIN_DIR + "/classrooms.txt";

    public AdminClassroomDAO() {
        super(FILE_PATH);
    }

    @Override
    protected Classroom parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 6) return null;

        int id = Integer.parseInt(parts[0]);
        ClassroomType type = ClassroomType.valueOf(parts[1]);
        int number = Integer.parseInt(parts[2]);
        int floor = Integer.parseInt(parts[3]);
        int capacity = Integer.parseInt(parts[4]);
        String location = parts[5];

        return new Classroom(id, type, number, floor, capacity, location);
    }

    @Override
    protected String toLine(Classroom classroom) {
        return classroom.getId() + "|" + classroom.getType().name() + "|" + 
               classroom.getNumber() + "|" + classroom.getFloor() + "|" + 
               classroom.getCapacity() + "|" + classroom.getLocation();
    }

    public int getNextId() {
        return loadAll().stream()
            .mapToInt(Classroom::getId)
            .max()
            .orElse(0) + 1;
    }

    public Classroom findById(int id) {
        return loadAll().stream()
            .filter(c -> c.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public void update(Classroom classroom) {
        java.util.List<Classroom> classrooms = loadAll();
        for (int i = 0; i < classrooms.size(); i++) {
            if (classrooms.get(i).getId() == classroom.getId()) {
                classrooms.set(i, classroom);
                saveAll(classrooms);
                return;
            }
        }
    }

    public void delete(Classroom classroom) {
        java.util.List<Classroom> classrooms = loadAll();
        classrooms.removeIf(c -> c.getId() == classroom.getId());
        saveAll(classrooms);
    }
}

