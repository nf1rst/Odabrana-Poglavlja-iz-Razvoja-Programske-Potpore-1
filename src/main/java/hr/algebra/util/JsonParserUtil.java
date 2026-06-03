package hr.algebra.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.exceptions.InvalidParseException;
import hr.algebra.models.*;
import hr.algebra.models.help.ParsedData;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class JsonParserUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonParserUtil() {}

    //https://stackoverflow.com/questions/52684545/returning-an-array-of-a-generic-type-in-java
    public static ParsedData parse(String json){
        List<Headphones>headphonesList = new ArrayList<>();
        Set<Technology>technologySet = new LinkedHashSet<>();
        Set<Use> usesSet = new LinkedHashSet<>();
        Set<Brand> brandSet = new LinkedHashSet<>();
        Set<Type>typeSet = new LinkedHashSet<>();

        String description = "description";

        try{
            JsonNode root =  mapper.readTree(json);
            for(JsonNode node : root){
                Headphones headphones = new Headphones();
                Brand brand = new Brand();
                Type type = new Type();

                headphones.setName(node.get("name").asText());
                headphones.setYearEdition(node.get("yearEdition").asInt(0));
                headphones.setImpedance(node.get("impedance").asInt(0));
                headphones.setMinFrequency(node.get("minFrequency").asInt(0));
                headphones.setMaxFrequency(node.get("maxFrequency").asInt(0));
                headphones.setBatteryLife(node.get("batteryLife").asInt(0));
                headphones.setPrice(node.get("price").asDouble(0));
                headphones.setDescription(node.get(description).asText());

                JsonNode brandNode = node.get("brand");
                brand.setName(brandNode.get("name").asText());
                brand.setDescription(brandNode.get(description).asText());
                brandSet.add(brand);
                headphones.setBrand(brand);

                //ovdje vraca listu tehcnologies
                JsonNode technologiesNode=node.get("technologies");
                for(JsonNode tNode:technologiesNode){
                    Technology technology = new Technology();
                    technology.setName(tNode.get("name").asText());
                    technology.setDescription(tNode.get(description).asText());
                    technologySet.add(technology);
                    headphones.getTechnologies().add(technology);
                }

                JsonNode usesNode=node.get("uses");
                for(JsonNode uNode:usesNode){
                    Use use = new Use();
                    use.setName(uNode.get("name").asText());
                    usesSet.add(use);
                    headphones.getPossibleUsages().add(use);
                }

                JsonNode typeNode = node.get("type");
                type.setName(typeNode.get("name").asText());
                typeSet.add(type);
                headphones.setType(type);

                headphonesList.add(headphones);
            }
        }
        catch (Exception e){
            LoggingUtil.logError("Parse error", e);
            throw new InvalidParseException("Cannot parse API data");
        }
        return new ParsedData(brandSet, technologySet, usesSet, typeSet, headphonesList);
    }
}
