package hr.algebra.xml.export;

import hr.algebra.models.Headphones;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name="catalog")
public class HeadphonesCatalog {

    private List<Headphones> headphones;

    public HeadphonesCatalog(List<Headphones> headphones) {
        this.headphones = headphones;
    }

    public HeadphonesCatalog() {}

   @XmlElement (name="headphones")
    public List<Headphones> getHeadphones(){return headphones;}

    public void setHeadphones(List<Headphones> headphones) {
        this.headphones = headphones;
    }
}
