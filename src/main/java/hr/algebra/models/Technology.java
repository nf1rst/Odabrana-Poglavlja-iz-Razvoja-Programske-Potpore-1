package hr.algebra.models;

import java.util.Objects;

public class Technology implements Comparable<Technology> {

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String name;
    private String description;

    public Technology(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Technology{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public Technology(String name, String description) {
        this.id=0; //place holder
        this.name = name;
        this.description = description;
    }

    public Technology() {
        this.id=0;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}


    public void setName(String name) {this.name = name;}
    public void setDescription(String description) {this.description = description;}

    @Override
    public boolean equals(Object o) {return o instanceof Technology t && t.getName().equals(this.getName());}

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public int compareTo(Technology o) {
        return name.compareTo(o.name);
    }
}
