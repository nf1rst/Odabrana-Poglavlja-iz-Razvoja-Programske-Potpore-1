package hr.algebra.repositories.factory;
import hr.algebra.repositories.*;
import hr.algebra.repositories.implementation.*;

public class RepositoryFactory {
    private RepositoryFactory() {}

    public static AdminRepository getAdminRespository(){
        return AdminRepositoryImpl.getInstance();
    }

    public static HeadphonesRepository getHeadphonesRespository(){
        return HeadphonesRepositoryImpl.getInstace();
    }

    public static BrandRepository getBrandRespository(){
        return BrandRepositoryImpl.getInstance();
    }

    public static TechnologyRepository getTechnologyRespository(){
        return TechnologyRepositoryImpl.getInstance();
    }

    public static TypeRepository getTypeRespository(){
        return TypeRepositoryImpl.getInstance();
    }

    public static UseRepository getUseRespository(){
        return UseRepositoryImpl.getInstance();
    }

    public static UserRepository getUserRespository(){
        return UserRepositoryImpl.getInstance();
    }


}
