package sample.models;

public class Client {
    private Integer id;
    private String FullName;
    private String MobilePhone;
    private String DateBorn;

    public Client(int id, String FullName, String MobilePhone, String DateBorn) {
        this.id = id;
        this.FullName = FullName;
        this.MobilePhone = MobilePhone;
        this.DateBorn = DateBorn;
    }

    public Integer getId() {
        return id;
    }

    public String getFullName() {
        return FullName;
    }

    public String getMobilePhone() {
        return MobilePhone;
    }

    public String getDateBorn() {
        return DateBorn;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setMobilePhone(String mobilePhone) {
        MobilePhone = mobilePhone;
    }

    public void setDateBorn(String dateBorn) {
        DateBorn = dateBorn;
    }
}
