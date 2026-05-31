package hr.algebra.models;

import java.util.Objects;

public class Technology {

    private final int id;
    private String name;
    private String description;

    public Technology(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Technology(String name, String description) {
        this.id=0; //place holder
        this.name = name;
        this.description = description;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}


    public void setName(String name) {this.name = name;}
    public void setDescription(String description) {this.description = description;}

    @Override
    public boolean equals(Object o) {return o instanceof Technology t && t.id==this.id;}

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
