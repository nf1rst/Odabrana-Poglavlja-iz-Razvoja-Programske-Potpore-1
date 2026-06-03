package hr.algebra.services;

import hr.algebra.exceptions.EntityExistantException;
import hr.algebra.models.*;
import hr.algebra.models.help.ParsedData;
import hr.algebra.util.AlertUtil;
import hr.algebra.util.HttpUtil;
import hr.algebra.util.JsonParserUtil;
import hr.algebra.util.LoggingUtil;
import javafx.application.Platform;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public enum DBInitiatorService {
    INSTANCE;

    private final HeadphonesService headphonesService=HeadphonesService.getInstance();
    private final BrandService brandService =  BrandService.getInstance();
    private final TypeService  typeService =  TypeService.getInstance();
    private final UseService useService =  UseService.getInstance();
    private final UserService userService =  UserService.getInstance();
    private final TechnologyService technologyService =  TechnologyService.getInstance();

    public void insertAdmin(){
        //ovaj ide u try catch, da dopusti inserAPIData da se izvrsi ukoliko admin vec postoji
        try{
            userService.registerAdmin("admin", "asdfasdf");
            LoggingUtil.logInfo("Admin successfully inserted");
        }
        catch (EntityExistantException e){
            LoggingUtil.logError("Admin already exists", e);
        }
    }

    public void insetAPIData(){
        Optional<String> jsonData = HttpUtil.getJSON();
        if(jsonData.isEmpty()){
            Platform.runLater(()->AlertUtil.showErrorAlert("Start data from API could not be loaded, you can still use the application"));
            return;
        }

        ParsedData parsedData = JsonParserUtil.parse(jsonData.get());
        insertBrand(parsedData.getBrand());
        insertTechnologies(parsedData.getTechnologies());
        insertUses(parsedData.getUses());
        insertTypes(parsedData.getTypes());
        insertHeadphones(parsedData.getHeadphones());
        LoggingUtil.logInfo("API data successfully inserted");
    }

    private void insertHeadphones(List<Headphones> headphones) {
        //treba se napravit da za svaki entitet po imenu izvadi njegov id pa da se onda tako dalje azurira
        headphones.forEach(b->{
            int brandId=brandService.getBrandIdByName(b.getBrand().getName());
            b.getBrand().setId(brandId);

            int typeId =typeService.getTypeIdByName(b.getType().getName());
            b.getType().setId(typeId);

                headphonesService.save(b);

                List<Technology> technologies = b.getTechnologies();
                technologies.forEach(t->{
                    int technologyId= technologyService.getTechnologyIdByName(t.getName());

                    headphonesService.addTechnology(b.getId(), technologyId);

                });

                List<Use>uses = b.getPossibleUsages();
                uses.forEach(a->{
                    int useId= useService.getUseIdByName(a.getName());
                    headphonesService.addUse(b.getId(), useId);
                });
        });
    }

    private void insertTypes(Set<Type> types) {
        types.forEach(typeService::save);
    }

    private void insertUses(Set<Use> uses) {
        uses.forEach(useService::save);
    }

    private void insertTechnologies(Set<Technology> technologies)  {
        technologies.forEach(technologyService::save);
    }

    private void insertBrand(Set<Brand> brandSet) {
        brandSet.forEach(brandService::save);
    }

}
