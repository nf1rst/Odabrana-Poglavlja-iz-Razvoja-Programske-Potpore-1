package hr.algebra.models;

public class User implements Comparable<User>{
    private final int id;
    private String username;
    private String password;
    private Role role;
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
        imagePath=null; // po defaultu korisnik nema profilnu sliku
    }

    public int getId() {return id;}
    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public Role getRole() {return role;}
    public String getImagePath() {return imagePath;}

    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setRole(Role role) {this.role = role;}
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}


    @Override
    public int compareTo(User o) {return this.username.compareTo(o.username);}
}
