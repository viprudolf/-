package sample.models;

public class Lawyer {
    private Integer id;
    private String FullName;
    private String MobilePhone;

    public Lawyer (Integer id, String FullName, String MobilePhone) {
        this.id = id;
        this.FullName = FullName;
        this.MobilePhone = MobilePhone;
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

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setMobilePhone(String mobilePhone) {
        MobilePhone = mobilePhone;
    }
}
