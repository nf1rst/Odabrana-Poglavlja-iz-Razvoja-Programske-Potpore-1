package hr.algebra.services;
import hr.algebra.models.Brand;
import hr.algebra.models.Headphones;
import hr.algebra.repositories.HeadphonesRepository;
import hr.algebra.repositories.implementation.HeadphonesRepositoryImpl;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HeadphonesService {
    private final static HeadphonesService INSTANCE=new HeadphonesService();
    public static HeadphonesService getInstance(){return INSTANCE;}
    private final HeadphonesRepository repo;

    public HeadphonesService() {
        this.repo= HeadphonesRepositoryImpl.getInstace();
    }

    public List<Headphones>getAllHeadphones(){return repo.getAll();}
    public Optional<Headphones> getHeadphonesById(int id){return repo.getById(id);}
    public void save(Headphones headphones){repo.save(headphones);}
    public void delete(int id){repo.delete(id);}

    //service sloj se ne brine o tome da to prode kroz bazu to se radi tamo u repo implementaion
    public void addTechnology(int headphonesId, int technologyId){
        repo.addTechnology(headphonesId,technologyId);
    }
    public void removeTechnology(int headphonesId, int technologyId){
        repo.removeTechnology(headphonesId,technologyId);
    }
    public void addUse(int headphonesId, int useId){
        repo.addUse(headphonesId,useId);
    }
    public void removeUse(int headphonesId, int useId){
        repo.removeUse(headphonesId, useId);
    }


    public void forEach(Consumer<Headphones>action){
        repo.getAll().forEach(action);
    }

    public static final Predicate<Headphones>isWireless=h->h.getBatteryLife()>0;
    public static final Predicate<Headphones>isCheap = h->h.getPrice()<100.0;
    public static final Predicate<Headphones>isPreminum=h->h.getPrice()>300.0;

    public List<Headphones>filter(Predicate<Headphones>predicate){
        return repo.getAll()
                .stream()
                .filter(predicate)
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Headphones>getWirelessAndWithinBudget(double maxPrice){
        Predicate<Headphones>wireless= h->h.getBatteryLife()>0;
        Predicate<Headphones>withinBudget = h->h.getPrice()<=maxPrice;

        return repo.getAll()
                .stream()
                .filter(wireless.and(withinBudget))
                .collect(Collectors.toList());
    }

    public List<Headphones>getWired(){
        return repo.getAll()
                .stream()
                .filter(isWireless.negate())
                .collect(Collectors.toList());
    }

    public List<String> getDisplayNames(){
        Function<Headphones,String> toDisplay= h->h.getBrand().getName() + " " + h.getName() + " [ " + h.getYearEdition() + " ]";
        return repo.getAll()
                .stream()
                .map(toDisplay)
                .collect(Collectors.toList());
    }

    public List<String> getFormattedPrice(){
        Function<Headphones, Double> toPrice = Headphones::getPrice;
        Function<Double, String> formatPrice = d->String.format("%.2f EUR", d);

        //moglo se sve odjednom u string
        return repo.getAll()
                .stream()
                .map(toPrice.andThen(formatPrice))
                .collect(Collectors.toList());
    }

    public Optional<Headphones>getCheapestHeadphones(){
        return repo.getAll()
                .stream()
                .min(Comparator.comparingDouble(Headphones::getPrice));
    }

    public Optional<Headphones>getMostExpensive(){
        return repo.getAll()
                .stream()
                .max(Comparator.comparingDouble(Headphones::getPrice));
    }

    public Optional<Headphones> getFirstWireless(){
        return repo.getAll()
                .stream()
                .filter(isWireless)
                .findFirst();
    }

    public Headphones getByIdWithException(int id){
        return repo.getById(id)
                .orElseThrow(()->new RuntimeException("Cannot find those headphones"));
    }


    //filteri za UI

    public List<Headphones>getByBrand(int brandId){
        return repo.getByBrandID(brandId);
    }
    public List<Headphones>getByType(int typeId){
        return repo.getByTypeID(typeId);
    }
    public List<Headphones>getByUse(int useId){
        return repo.getByUseID(useId);
    }
    public List<Headphones>getByMaxPrice(double maxPrice){
        return repo.getByMaxPrice(maxPrice);
    }
    //pretraga po brand, name ili desc
    public List<Headphones> search(String query){

        String queryNoCapitals=query.toLowerCase();
        Predicate<Headphones>nameMatches = h->h.getName().contains(queryNoCapitals) ;
        Predicate<Headphones>descriptionMatches = h->h.getDescription()!=null && h.getDescription().contains(queryNoCapitals);
        Predicate<Headphones>brandMatches = h->h.getBrand()!=null && h.getBrand().getName().contains(queryNoCapitals) ;

        return repo.getAll()
                .stream()
                .filter(nameMatches.or(descriptionMatches).or(brandMatches))
                .sorted()
                .collect(Collectors.toList());
    }
    public List<Headphones>getPage(int page, int size){
        return repo.getAll()
                .stream()
                .sorted()
                .skip((long)page * size) //casting rezultat u long
                .limit(size)
                .collect(Collectors.toList());
    }
    public boolean hasWireless () {
        return repo.getAll()
                .stream()
                .anyMatch(isWireless);
    }
    public Map<Brand, List<Headphones>> groupByBrand(){
        return repo.getAll()
                .stream()
                .collect(Collectors.groupingBy(Headphones::getBrand));
    }
    public boolean noneUnderPrice(double price){
        return repo.getAll()
                .stream()
                .noneMatch(h->h.getPrice()<price); // za sto ovo tocno sluzi
    }
    public double getAevragePrice(){
        return repo.getAll()
                .stream()
                .collect(Collectors.averagingDouble(Headphones::getPrice));
    }
    //summarizing statistics fali
    // i sorted by price then year


    //usporeduje 2 headhponesa s compareForm i vraca mapu razlika
    public Map<String,String>compareHeadphones(Headphones h1, Headphones h2){
        Map<String,String>differences = new LinkedHashMap<>(); // zasto linked hash map??
        differences.put("Name", h1.getName() + " vs " + h2.getName());
        differences.put("Brand", h1.getBrand().getName() + " vs " + h2.getBrand().getName());
        differences.put("Type", h1.getType().getName() + " vs " + h2.getType().getName());
        differences.put("Battery(Hours)", h1.getBatteryLife() + " vs " + h2.getBatteryLife());
        differences.put("Price(EUR)", h1.getPrice() + " vs " + h2.getPrice());
        differences.put("Min frequency(Herz)", h1.getMinFrequency() + " vs " + h2.getMinFrequency());
        differences.put("Max frequency(Herz)", h1.getMaxFrequency() + " vs " + h2.getMaxFrequency());
        differences.put("Year", h1.getYearEdition() + " vs " + h2.getYearEdition());
        differences.put("Impedance(Ohm)", h1.getImpedance() + " vs " + h2.getImpedance());
        return differences;
    }

}
