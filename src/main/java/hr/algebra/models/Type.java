package hr.algebra.models;

import java.util.Objects;

public class Type implements Comparable<Type> {
    private int id;
    private String name;


    public void setId(int id) {
        this.id = id;
    }

    public Type(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Type(String name) {
        this.id=0; //placeholder
        this.name = name;
    }

    public Type() {
        this.id=0;
    }

    public String getName() {return name;}
    public int getId() {return id;}

    public void setName(String name) {this.name = name;}

    @Override
    public boolean equals(Object o) {return o instanceof Type t && t.getName().equals(this.getName());}

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Type o) {return this.name.compareTo(o.name);}
}
