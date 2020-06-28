package model;
import java.util.Date;

public class Kursas {
    private Integer ID;
    private Date REQUEST_DATE;
    private Date RATE_DATE;
    private String FROM_CURR;
    private double FROM_AMOUNT;
    private String TO_CURR;
    private String PAVADINIMAS;
    private double TO_AMOUNT;
    private java.sql.Date CREATED_DATE;

    public Kursas() {
        this.ID = ID;
        this.REQUEST_DATE = REQUEST_DATE;
        this.RATE_DATE = RATE_DATE;
        this.FROM_CURR = FROM_CURR;
        this.FROM_AMOUNT = FROM_AMOUNT;
        this.TO_CURR = TO_CURR;
        this.PAVADINIMAS = PAVADINIMAS;
        this.TO_AMOUNT = TO_AMOUNT;
        this.CREATED_DATE = CREATED_DATE;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getREQUEST_DATE() {
        return REQUEST_DATE;
    }

    public void setREQUEST_DATE(Date REQUEST_DATE) {
        this.REQUEST_DATE = REQUEST_DATE;
    }

    public Date getRATE_DATE() {
        return RATE_DATE;
    }

    public void setRATE_DATE(Date RATE_DATE) {
        this.RATE_DATE = RATE_DATE;
    }

    public String getFROM_CURR() {
        return FROM_CURR;
    }

    public void setFROM_CURR(String FROM_CURR) {
        this.FROM_CURR = FROM_CURR;
    }

    public double getFROM_AMOUNT() {
        return FROM_AMOUNT;
    }

    public void setFROM_AMOUNT(double FROM_AMOUNT) {
        this.FROM_AMOUNT = FROM_AMOUNT;
    }

    public String getTO_CURR() {
        return TO_CURR;
    }

    public void setTO_CURR(String TO_CURR) {
        this.TO_CURR = TO_CURR;
    }
    public String getPAVADINIMAS() {
        return PAVADINIMAS;
    }

    public void setPAVADINIMAS(String PAVADINIMAS) {
        this.PAVADINIMAS = PAVADINIMAS;
    }

    public double getTO_AMOUNT() {
        return TO_AMOUNT;
    }

    public void setTO_AMOUNT(double TO_AMOUNT) {
        this.TO_AMOUNT = TO_AMOUNT;
    }

    public java.sql.Date getCREATED_DATE() {
        return CREATED_DATE;
    }

    public void setCREATED_DATE(java.sql.Date CREATED_DATE) {
        this.CREATED_DATE = CREATED_DATE;
    }

}
