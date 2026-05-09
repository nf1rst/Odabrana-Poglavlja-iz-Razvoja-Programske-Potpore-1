package hr.algebra.models;

import java.util.Objects;

public class Use implements Comparable<Use> {
    private final int id;
    private String name;

    public Use(String name) {
        this.id=0;
        this.name = name;
    }

    public Use(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {return id;}
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Override
    public boolean equals(Object o) {return o instanceof Use u && u.id==this.id;}
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Use o) {return this.name.compareTo(o.name);}
}
