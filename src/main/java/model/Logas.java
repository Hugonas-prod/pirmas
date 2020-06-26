package model;

import java.util.Date;

public class Logas {

    private Integer ID;
    private Date REQUEST_DATE;
    private String TO_CURR;
    private Date CREATED_DATE;
    private String PROBLEM;

    public Logas() {
        //default
    }

    public Logas(Integer ID, Date REQUEST_DATE, String TO_CURR, Date CREATED_DATE, String PROBLEM) {
        this.ID = ID;
        this.REQUEST_DATE = REQUEST_DATE;
        this.TO_CURR = TO_CURR;
        this.CREATED_DATE = CREATED_DATE;
        this.PROBLEM = PROBLEM;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Date getREQUEST_DATE() {
        return REQUEST_DATE;
    }

    public void setREQUEST_DATE(Date REQUEST_DATE) {
        this.REQUEST_DATE = REQUEST_DATE;
    }

    public String getTO_CURR() {
        return TO_CURR;
    }

    public void setTO_CURR(String TO_CURR) {
        this.TO_CURR = TO_CURR;
    }

    public Date getCREATED_DATE() {
        return CREATED_DATE;
    }

    public void setCREATED_DATE(Date CREATED_DATE) {
        this.CREATED_DATE = CREATED_DATE;
    }

    public String getPROBLEM() {
        return PROBLEM;
    }

    public void setPROBLEM(String PROBLEM) {
        this.PROBLEM = PROBLEM;
    }
}
