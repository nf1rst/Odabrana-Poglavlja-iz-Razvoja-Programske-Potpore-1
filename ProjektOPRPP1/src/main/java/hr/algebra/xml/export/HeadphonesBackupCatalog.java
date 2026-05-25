package hr.algebra.xml.export;
import hr.algebra.models.Headphones;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

//https://www.baeldung.com/jaxb

@XmlRootElement(name="HeadphonesCatalog")
public class HeadphonesBackupCatalog {

    private List<Headphones> headphones;

    public HeadphonesBackupCatalog(List<Headphones> headphones) {
        this.headphones = headphones;
    }

    public HeadphonesBackupCatalog() {}

   @XmlElement (name="headphones")
    public List<Headphones> getHeadphones(){return headphones;}

    public void setHeadphones(List<Headphones> headphones) {
        this.headphones = headphones;
    }
}
