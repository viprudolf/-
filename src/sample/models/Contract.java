package sample.models;

import java.util.Date;

public class Contract {
    private Integer id;
    private String clientName;
    private String lawyerName;
    private Date startDate;
    private Date endDate;
    private Double cost;

    public Contract (Integer id, String clientName, String lawyerName, Date startDate, Date endDate, Double cost) {
        this.id = id;
        this.clientName = clientName;
        this.lawyerName = lawyerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cost = cost;
    }

    public Integer getId() {
        return id;
    }

    public String getLawyerName() {
        return lawyerName;
    }

    public String getClientName() {
        return clientName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Double getCost() {
        return cost;
    }
}
