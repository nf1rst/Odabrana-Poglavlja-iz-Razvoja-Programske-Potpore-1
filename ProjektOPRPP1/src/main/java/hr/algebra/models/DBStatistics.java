package hr.algebra.models;

public class DBStatistics {
    //headphones, brand, technology, use, type, users

    private int headphonesEntriesNum;
    private int brandEntriesNum;
    private int techEntriesNum;
    private int useEntriesNum;
    private int typeEntriesNum;
    private int usersEntriesNum;

    public void setHeadphonesEntriesNum(int headphonesEntriesNum) {
        this.headphonesEntriesNum = headphonesEntriesNum;
    }

    public void setBrandEntriesNum(int brandEntriesNum) {
        this.brandEntriesNum = brandEntriesNum;
    }

    public void setTechEntriesNum(int techEntriesNum) {
        this.techEntriesNum = techEntriesNum;
    }

    public void setUseEntriesNum(int useEntriesNum) {
        this.useEntriesNum = useEntriesNum;
    }

    public void setTypeEntriesNum(int typeEntriesNum) {
        this.typeEntriesNum = typeEntriesNum;
    }

    public void setUsersEntriesNum(int usersEntriesNum) {
        this.usersEntriesNum = usersEntriesNum;
    }

    public int getHeadphonesEntriesNum() {
        return headphonesEntriesNum;
    }

    public int getBrandEntriesNum() {
        return brandEntriesNum;
    }

    public int getTechEntriesNum() {
        return techEntriesNum;
    }

    public int getUseEntriesNum() {
        return useEntriesNum;
    }

    public int getTypeEntriesNum() {
        return typeEntriesNum;
    }

    public int getUsersEntriesNum() {
        return usersEntriesNum;
    }

    public DBStatistics(int headphonesEntriesNum, int brandEntriesNum, int techEntriesNum1, int useEntriesNum, int typeEntriesNum, int usersEntriesNum) {
        this.headphonesEntriesNum = headphonesEntriesNum;
        this.brandEntriesNum = brandEntriesNum;
        this.techEntriesNum = techEntriesNum1;
        this.useEntriesNum = useEntriesNum;
        this.typeEntriesNum = typeEntriesNum;
        this.usersEntriesNum = usersEntriesNum;
    }


}
