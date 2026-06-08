package hr.algebra.models;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Headphones implements Comparable<Headphones> {
    private int id; // ovdje id nije final da ga se moze u repo sloju postavit na inserted objekt tako da ga u controlleru mogu poslat u metodu koja ce preko vracenog
    //ID-a novostvorenog objekta moc insertirat u M:N medutablice

    private int yearEdition;
    private int impedance; // impedance = el. otpor audio signalima (Ohm)
    private int minFrequency;
    private int maxFrequency;
    private int batteryLife;//za bezicne
    private double price;
    private String description;
    private String imagePath;
    private String name;
    private Brand brand;
    private List<Technology>technologies=new ArrayList<>();
    private List<Use>uses=new ArrayList<>();
    private Type type;

    @Override
    public String toString() {
        return "Headphones{" +
                "id=" + id +
                ", yearEdition=" + yearEdition +
                ", impedance=" + impedance +
                ", minFrequency=" + minFrequency +
                ", maxFrequency=" + maxFrequency +
                ", batteryLife=" + batteryLife +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", name='" + name + '\'' +
                ", brand=" + brand +
                ", technologies=" + technologies +
                ", uses=" + uses +
                ", type=" + type +
                '}';
    }

    public Headphones() {
        this.id=0;
    }

    public Headphones(int id, String name,int yearEdition, int impedance, int minFrequency, int maxFrequency, int batteryLife, double price, String description, String imagePath, Brand brand,Type type, List<Technology> technologies, List<Use> uses) {
        this.id = id;
        this.name=name;
        this.yearEdition = yearEdition;
        this.impedance = impedance;
        this.minFrequency = minFrequency;
        this.maxFrequency = maxFrequency;
        this.batteryLife = batteryLife;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
        this.brand = brand;
        this.technologies = technologies;
        this.uses = uses;
        this.type = type;
    }

    public Headphones(String name,int yearEdition, int impedance, int minFrequency, int maxFrequency, int batteryLife, double price, String description, String imagePath, Brand brand,Type type, List<Technology> technologies, List<Use> uses) {
        this.id=0;//plcaeholder
        this.name=name;
        this.yearEdition = yearEdition;
        this.impedance = impedance;
        this.minFrequency = minFrequency;
        this.maxFrequency = maxFrequency;
        this.batteryLife = batteryLife;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
        this.brand = brand;
        this.technologies = technologies;
        this.uses = uses;
        this.type = type;
    }

    public int getId() { return id; }
    public int getYearEdition() { return yearEdition; }
    public int getImpedance() { return impedance; }
    public int getMinFrequency() { return minFrequency; }
    public int getMaxFrequency() { return maxFrequency; }
    public int getBatteryLife() { return batteryLife; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
    public Brand getBrand() { return brand; }
    public Type getType() { return type; }
    public List<Technology> getTechnologies() { return technologies; }
    public List<Use> getPossibleUsages() { return uses; }
    public String getName() {return name;}



    public void setName(String name) {this.name = name;}
    public void setYearEdition(int yearEdition) { this.yearEdition = yearEdition; }
    public void setImpedance(int impedance) { this.impedance = impedance; }
    public void setMinFrequency(int minFrequency) { this.minFrequency = minFrequency; }
    public void setMaxFrequency(int maxFrequency) { this.maxFrequency = maxFrequency; }
    public void setBatteryLife(int batteryLife) { this.batteryLife = batteryLife; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setBrand(Brand brand) { this.brand = brand; }
    public void setType(Type type) { this.type = type; }
    public void setId(int id) {this.id = id;}

    public void setTechnologies(List<Technology> technologies) { this.technologies = technologies; }
    public void setPossibleUsages(List<Use> possibleUsages) { this.uses = possibleUsages; }

    @Override
    public boolean equals(Object o) {return o instanceof Headphones h && h.name.equals(name);}
    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    //public int compareTo(Headphones o) {return name.compareTo(o.name);}
    public int compareTo(Headphones o) {return Double.compare(this.price,o.price);}

}
