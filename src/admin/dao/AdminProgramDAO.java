package admin.dao;

import shared.dao.BaseDAO;
import shared.util.FileUtil;
import shared.model.Program;

public class AdminProgramDAO extends BaseDAO<Program> {
    private static final String FILE_PATH = FileUtil.ADMIN_DIR + "/programs.txt";

    public AdminProgramDAO() {
        super(FILE_PATH);
    }

    @Override
    protected Program parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) return null;

        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        String code = parts[2];

        return new Program(id, name, code);
    }

    @Override
    protected String toLine(Program program) {
        return program.getId() + "|" + program.getName() + "|" + program.getCode();
    }

    public int getNextId() {
        return loadAll().stream()
            .mapToInt(Program::getId)
            .max()
            .orElse(0) + 1;
    }

    public Program findById(int id) {
        return loadAll().stream()
            .filter(p -> p.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public void update(Program program) {
        java.util.List<Program> programs = loadAll();
        for (int i = 0; i < programs.size(); i++) {
            if (programs.get(i).getId() == program.getId()) {
                programs.set(i, program);
                saveAll(programs);
                return;
            }
        }
    }

    public void delete(Program program) {
        java.util.List<Program> programs = loadAll();
        programs.removeIf(p -> p.getId() == program.getId());
        saveAll(programs);
    }
}

