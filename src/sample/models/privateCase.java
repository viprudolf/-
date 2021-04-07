package sample.models;

public class privateCase {
    private Integer id;
    private String clientName;
    private String lawyerName;
    private String datetime;
    private String result;
    private String about;

    public privateCase (Integer id, String clientName, String lawyerName, String datetime, String result, String about) {
        this.id = id;
        this.clientName = clientName;
        this.lawyerName = lawyerName;
        this.datetime = datetime;
        this.result = result;
        this.about = about;
    }

    public Integer getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public String getLawyerName() {
        return lawyerName;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getResult() {
        return result;
    }

    public String getAbout() {
        return about;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
