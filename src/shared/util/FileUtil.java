package shared.util;

import java.io.File;

public class FileUtil {
    public static final String DATA_DIR = "data";
    public static final String ADMIN_DIR = DATA_DIR + "/admin";
    public static final String TEACHER_DIR = DATA_DIR + "/teacher";
    public static final String STUDENT_DIR = DATA_DIR + "/student";

    public static void ensureDirectoriesExist() {
        createDirectoryIfNotExists(DATA_DIR);
        createDirectoryIfNotExists(ADMIN_DIR);
        createDirectoryIfNotExists(TEACHER_DIR);
        createDirectoryIfNotExists(STUDENT_DIR);
    }

    private static void createDirectoryIfNotExists(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}

