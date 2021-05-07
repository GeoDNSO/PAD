package es.ucm.fdi.tieryourlikes.model;

public class Category {
    String id;
    String name;
    String creation_time;

    public Category(String id, String name, String creation_time) {
        this.id = id;
        this.name = name;
        this.creation_time = creation_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationTime() {
        return creation_time;
    }

    public void setCreationTime(String creation_time) {
        this.creation_time = creation_time;
    }
}
