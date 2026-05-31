package hr.algebra.models.help;

import hr.algebra.models.*;

import java.util.List;
import java.util.Set;

public class ParsedData {
    private Set<Brand> brand;
    private Set<Technology> technologies;
    private Set<Use> uses;
    private Set<Type> types;
    private List<Headphones> headphones;

    public ParsedData(Set<Brand> brand, Set<Technology> technologies, Set<Use> uses, Set<Type> types, List<Headphones> headphones) {
        this.brand = brand;
        this.technologies = technologies;
        this.uses = uses;
        this.types = types;
        this.headphones = headphones;
    }

    public Set<Brand> getBrand() {
        return brand;
    }

    public Set<Technology> getTechnologies() {
        return technologies;
    }

    public Set<Use> getUses() {
        return uses;
    }

    public Set<Type> getTypes() {
        return types;
    }

    public List<Headphones> getHeadphones() {
        return headphones;
    }
}
