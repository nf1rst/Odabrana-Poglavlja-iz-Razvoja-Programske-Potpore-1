package hr.algebra;
import hr.algebra.database.DBSingleton;
import hr.algebra.exceptions.CannotPerformActionException;
import hr.algebra.exceptions.EntityExistantException;
import hr.algebra.models.*;
import hr.algebra.models.help.ParsedData;
import hr.algebra.repositories.implementation.*;
import hr.algebra.services.*;
import hr.algebra.util.HttpUtil;
import hr.algebra.util.JsonParserUtil;
import hr.algebra.util.LoggingUtil;
import hr.algebra.util.SceneLoadingUtil;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public class MainApplication extends Application {
    public static void main(String[] args)  {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneLoadingUtil.loadNewCustomWindow("/hr/algebra/views/login-view.fxml","Headphones - login",null, 400,500);
        initBackground();
    }

    private void initBackground() {
        Task<Void> init = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                insertAdmin();
                return null;
            }
        };
        init.setOnSucceeded(e->LoggingUtil.logInfo("Admin added"));
        init.setOnFailed(e->LoggingUtil.logError("Admin already exists"));
        //failed se okine ako dode unhandled greska - EntityNotExistantException iz insertAdmin
        Thread bgThread = new Thread(init);
        bgThread.setDaemon(true);
        bgThread.start();

        Task<Void>initDb = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                insertStartData();
                return null;
            }
        };

        Thread dbThread = new Thread(initDb);
        dbThread.setDaemon(true);
        dbThread.start();

    }

    private void insertStartData() {
        try{
            Optional<String> headphonesJSON = HttpUtil.getJSON();
            ParsedData parsedData;
            if(!headphonesJSON.isEmpty()){
                parsedData= JsonParserUtil.parse(headphonesJSON.get());
                //id je mapirao dobro ali se ovdje opet salju objekti s IDem = 0 jer se nije updateala ovo lista
                insertBrand(parsedData.getBrand());
                insertTechnologies(parsedData.getTechnologies());
                insertUses(parsedData.getUses());
                insertTypes(parsedData.getTypes());
                insertHeadphones(parsedData.getHeadphones());
            }


        }
        catch (Exception e){
            LoggingUtil.logError("Data insert error", e);
        }

    }

    private void insertHeadphones(List<Headphones> headphones) {
        final HeadphonesService headphonesService= HeadphonesService.getInstance();
        final BrandService brandService = BrandService.getInstance();
        final TypeService typeService = TypeService.getInstance();
        final TechnologyService technologyService = TechnologyService.getInstance();
        final UseService useService = UseService.getInstance();

//treba se napravit da za svaki entitet po imenu izvadi njegov id pa da se onda tako dalje azurira
        headphones.forEach(b->{
            int brandId=brandService.getBrandIdByName(b.getBrand().getName());
            b.getBrand().setId(brandId);

            int typeId =typeService.getTypeIdByName(b.getType().getName());
            b.getType().setId(typeId);
                try{
                    headphonesService.save(b);

                    List<Technology> technologies = b.getTechnologies();
                    technologies.forEach(t->{
                        int technologyId= technologyService.getTechnologyIdByName(t.getName());
                        try{
                            headphonesService.addTechnology(b.getId(), technologyId);
                        }
                        catch (CannotPerformActionException e){
                            LoggingUtil.logError("Headphones technology insert error", e);
                        }
                    });

                    List<Use>uses = b.getPossibleUsages();
                    uses.forEach(a->{
                        try{
                            int useId= useService.getUseIdByName(a.getName());
                            headphonesService.addUse(b.getId(), useId);
                        }
                        catch (CannotPerformActionException e){
                            LoggingUtil.logError("Possible usage insert error", e);
                        }
                    });
                }
                catch (CannotPerformActionException e){
                    LoggingUtil.logError("Headphone insert error", e);
                }
        });
    }

    private void insertTypes(Set<Type> types) {
        final TypeRepositoryImpl typeRepo = TypeRepositoryImpl.getInstance();
        types.forEach(b->{
            try{
                typeRepo.save(b);
            }
            catch (SQLException e){
                LoggingUtil.logError("Type insert error", e);
            }
        });
    }

    private void insertUses(Set<Use> uses) {
        final UseRepositoryImpl useRepo = UseRepositoryImpl.getInstance();
        uses.forEach(b->{
            try{
                useRepo.save(b);
            }
            catch (SQLException e){
                LoggingUtil.logError("Use insert error", e);
            }
        });

    }

    private void insertTechnologies(Set<Technology> technologies)  {
        final TechnologyRepositoryImpl techRepo = TechnologyRepositoryImpl.getInstance();
        technologies.forEach(b->{
            try{
                techRepo.save(b);
            }
            catch (SQLException e){
                LoggingUtil.logError("Technology insert error", e);
            }
        });

    }

    private void insertBrand(Set<Brand> brandSet) {
        final BrandRepositoryImpl brandsRepo = BrandRepositoryImpl.getInstance();
        brandSet.forEach(b->{
            try{
                brandsRepo.save(b);
            }
            catch (SQLException e){
                LoggingUtil.logError("Brand init error", e);
            }
        });
    }

    private void insertAdmin() throws EntityExistantException {
        final UserService userService = UserService.getInstance();

        userService.registerAdmin("admin", "asdfasdf");
    }

    @Override
    public void stop() throws Exception {
        try {
            DBSingleton.INSTANCE.closeConnection();
            //da rucno brisanje u SQL skriptii
        } catch (Exception _) {
            //
        }
        super.stop();
    }
}
