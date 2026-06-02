package hr.algebra.xml.export;
import hr.algebra.models.Headphones;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.File;
import java.util.List;


//https://www.ibm.com/docs/en/was/8.5.5?topic=ujxdb-using-jaxb-schemagen-tooling-generate-xml-schema-file-from-java-class
public class XMLExporter {
    private XMLExporter(){}

    public static void marshall (List<Headphones> headphones) throws JAXBException {
        HeadphonesBackupCatalog headphonesCatalogue = new HeadphonesBackupCatalog(headphones);
        JAXBContext context = JAXBContext.newInstance(HeadphonesBackupCatalog.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(headphonesCatalogue, new File("backup.xml"));
    }

    public static void marshallSelected (Headphones headphones) throws JAXBException {
        ChosenHeadphonesCatalogue chosenHeadphones = new ChosenHeadphonesCatalogue(headphones);
        JAXBContext context = JAXBContext.newInstance(ChosenHeadphonesCatalogue.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(chosenHeadphones, new File("chosenHeadphones.xml"));
    }



}
