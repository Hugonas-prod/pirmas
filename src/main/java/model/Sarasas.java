package model;

import java.util.Date;

public class Sarasas {
     private String Kodas;
    private  String PavadinimasVal;
    private Date IrasoData;

    public Sarasas(){
        //default
    }
    public Sarasas(String kodas, String pavadinimasVal, Date irasoData) {
        Kodas = kodas;
        PavadinimasVal = pavadinimasVal;
        IrasoData = irasoData;
    }

    public String getKodas() {
        return Kodas;
    }

    public void setKodas(String kodas) {
        Kodas = kodas;
    }

    public String getPavadinimasVal() {
        return PavadinimasVal;
    }

    public void setPavadinimasVal(String pavadinimasVal) {
        PavadinimasVal = pavadinimasVal;
    }

    public Date getIrasoData() {
        return IrasoData;
    }

    public void setIrasoData(Date irasoData) {
        IrasoData = irasoData;
    }


}
