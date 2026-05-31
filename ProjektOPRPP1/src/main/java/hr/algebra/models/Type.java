package hr.algebra.models;

import java.util.Objects;

public class Type implements Comparable<Type> {
    private final int id;
    private String name;


    public Type(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Type(String name) {
        this.id=0; //placeholder
        this.name = name;
    }

    public String getName() {return name;}
    public int getId() {return id;}

    public void setName(String name) {this.name = name;}

    @Override
    public boolean equals(Object o) {return o instanceof Type t && this.id==t.id;}

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Type o) {return this.name.compareTo(o.name);}
}
