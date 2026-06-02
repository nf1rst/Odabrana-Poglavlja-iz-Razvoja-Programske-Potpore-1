package hr.algebra.xml.export;
import hr.algebra.models.*;
import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name="ChosenHeadphones")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"name", "yearEdition", "price", "impedance", "minFrequency", "maxFrequency", "batteryLife", "description", "brandName", "technologiesNames", "usesNames", "typeName"})
public class ChosenHeadphonesCatalogue {

    @XmlAttribute private int id;
    @XmlElement private int yearEdition;
    @XmlElement private int impedance;
    @XmlElement private int minFrequency;
    @XmlElement private int maxFrequency;
    @XmlElement  private int batteryLife;
    @XmlElement private double price;
    @XmlElement   private String description;
    @XmlElement   private String name;

    @XmlElement   private String brandName;

    @XmlElementWrapper(name="technologies")
    @XmlElement(name="technology") private List<String> technologiesNames=new ArrayList<>();

    @XmlElementWrapper(name="uses")
    @XmlElement(name="use") private List<String>usesNames=new ArrayList<>();

    @XmlElement   private String typeName;

    public ChosenHeadphonesCatalogue() {}

    public ChosenHeadphonesCatalogue(Headphones headphones){
        this.id = headphones.getId();
        this.yearEdition = headphones.getYearEdition();
        this.impedance = headphones.getImpedance();
        this.minFrequency = headphones.getMinFrequency();
        this.maxFrequency = headphones.getMaxFrequency();
        this.batteryLife = headphones.getBatteryLife();
        this.price = headphones.getPrice();
        this.description = headphones.getDescription();
        this.name = headphones.getName();
        this.brandName=headphones.getBrand().getName();
        this.typeName=headphones.getType().getName();
        headphones.getTechnologies().forEach(t->{
            if(t.getName()!=null) technologiesNames.add(t.getName());
        });
        headphones.getPossibleUsages().forEach(u->{
            if(u.getName()!=null) usesNames.add(u.getName());
        });
    }
}
