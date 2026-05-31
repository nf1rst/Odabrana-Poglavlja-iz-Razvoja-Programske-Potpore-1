package hr.algebra.models;

import java.util.Objects;

public class Brand implements Comparable<Brand>{
    private int id;

    private String name;
    private String description;

    public Brand(int id,String name, String description) {//za citanje iz baze
        this.id=id;
        this.name = name;
        this.description = description;
    }

    public Brand(String name, String description) {//za stvaranje novog
       this.id=0; //placeholder, baza ce to poslije sama rijesit
        this.name = name;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Brand() {
        this.id=0;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}

    public void setName(String name) {this.name = name;}

    public void setDescription(String description) {this.description = description;}

    @Override
    public boolean equals(Object o) {
        return o instanceof Brand b && b.getName().equals(this.getName()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id); //PROMIJENI U ONO S VJEZBI
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int compareTo(Brand o) {return this.name.compareTo(o.name);}
}
