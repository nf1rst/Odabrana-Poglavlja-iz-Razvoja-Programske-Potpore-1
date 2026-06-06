package hr.algebra.models;

import java.util.Objects;

public class User implements Comparable<User>{
    private final int id;
    private String username;
    private String password;
    private final Role role;
    private String imagePath;

    public User(int id, String username, String password, Role role, String imagePath) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.imagePath=imagePath;
    }

    public User(String username, String password, Role role) {
        this.id=0;//plcaeholder
        this.username = username;
        this.password = password;
        this.role = role;
        this.imagePath=null; // po defaultu korisnik nema profilnu sliku
    }

    public int getId() {return id;}
    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public Role getRole() {return role;}
    public String getImagePath() {return imagePath;}

    public void setUsername(String username) {this.username = username;}

    @Override
    public boolean equals(Object o) {
       return o instanceof User u && u.username.equals(this.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public int compareTo(User o) {return this.username.compareTo(o.username);}
}
