package sample.models;

import java.sql.Timestamp;

public class Journal {
    private Integer id;
    private String clientName;
    private String lawyerName;
    private Timestamp date;
    private String oldValue;

    public Journal (Integer id, String clientName, String lawyerName, Timestamp date, String oldValue) {
        this.id = id;
        this.lawyerName = lawyerName;
        this.clientName = clientName;
        this.date = date;
        this.oldValue = oldValue;
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

    public Timestamp getDate() {
        return date;
    }

    public String getOldValue() {
        return oldValue;
    }
}
