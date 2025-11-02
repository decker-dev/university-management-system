package shared.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {
    protected String filePath;

    public BaseDAO(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    private void ensureFileExists() {
        File file = new File(filePath);
        File parent = file.getParentFile();
        
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating file: " + filePath);
                e.printStackTrace();
            }
        }
    }

    protected abstract T parseLine(String line);
    protected abstract String toLine(T entity);

    public List<T> loadAll() {
        List<T> entities = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        T entity = parseLine(line);
                        if (entity != null) {
                            entities.add(entity);
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing line: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }
        
        return entities;
    }

    public void saveAll(List<T> entities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T entity : entities) {
                writer.write(toLine(entity));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + filePath);
            e.printStackTrace();
        }
    }

    public void save(T entity) {
        List<T> entities = loadAll();
        entities.add(entity);
        saveAll(entities);
    }

    public void update(T entity) {
        // To be implemented by subclasses based on their ID logic
    }

    public void delete(T entity) {
        // To be implemented by subclasses based on their ID logic
    }
}

