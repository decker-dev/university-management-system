package shared.model;

import shared.enums.ClassroomType;

public class Classroom {
    private int id;
    private ClassroomType type;
    private int number;
    private int floor;
    private int capacity;
    private String location;

    public Classroom() {
    }

    public Classroom(int id, ClassroomType type, int number, int floor, int capacity, String location) {
        this.id = id;
        this.type = type;
        this.number = number;
        this.floor = floor;
        this.capacity = capacity;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ClassroomType getType() {
        return type;
    }

    public void setType(ClassroomType type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Aula " + number + " - Piso " + floor + " (" + type + ")";
    }
}

