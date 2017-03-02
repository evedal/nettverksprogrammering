package entities;

import org.hibernate.annotations.NamedQuery;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * Created by evend on 3/1/2017.
 */

@Entity
@NamedQuery(name="finnAntallBoker", query="SELECT COUNT(k) from Konto k")

public class Konto implements Serializable{
    @Id
    private String kontonr;
    private double saldo;
    private String eier;
    @Version
    private int laas=0;

    public Konto(String kontonr, double saldo, String eier){
        this.kontonr = kontonr;
        this.saldo = saldo;
        this.eier = eier;
    }
    public void trekk(double beløp){
        this.saldo += beløp;
    }

    public Konto(){

    }

    public String getKontonr() {
        return kontonr;
    }

    public void setKontonr(String kontonr) {
        this.kontonr = kontonr;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getEier() {
        return eier;
    }

    public void setEier(String eier) {
        this.eier = eier;
    }

    @Override
    public String toString() {
        return "Konto{" +
                "kontonr='" + kontonr + '\'' +
                ", saldo=" + saldo +
                ", eier='" + eier + '\'' +
                '}';
    }
}
