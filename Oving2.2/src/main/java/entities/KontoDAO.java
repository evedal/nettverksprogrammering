package entities;

import javax.persistence.*;
import java.util.List;


/**
 * Created by evend on 3/1/2017.
 */
public class KontoDAO {
    private EntityManagerFactory emf;
    public KontoDAO(EntityManagerFactory emf){
        this.emf = emf;
    }
    private EntityManager getEm(){
        return emf.createEntityManager();
    }
    private void lukkEm(EntityManager em){
        if (em != null && em.isOpen())em.close();
    }
    public void lagreNyKonto(Konto konto){
        EntityManager em = getEm();
        try{
            em.getTransaction().begin();
            em.persist(konto);
            em.getTransaction().commit();
        }
        finally {
            lukkEm(em);
        }
    }
    public List<Konto> getKontoerOverSaldo(double saldo){
        EntityManager em = getEm();
        try{
            Query q  = em.createQuery("SELECT OBJECT(k) FROM Konto k WHERE k.saldo > :saldo");
            q.setParameter("saldo", saldo);

            return q.getResultList();
        }
        finally {
            lukkEm(em);
        }
    }
    public Konto finnKonto(String kontonr){
        EntityManager em = getEm();
        try{
            return em.find(Konto.class, kontonr);
        }
        finally {
            lukkEm(em);
        }
    }
    public void endreKonto(Konto konto){
        EntityManager em = getEm();
        try {
            em.getTransaction().begin();
            Konto k = em.merge(konto);
            em.getTransaction().commit();
        }
        finally {
            lukkEm(em);
        }
    }
    public void overførPenger(String fraKontoNr, String tilKontoNr, double beløp){
        EntityManager em = getEm();
        try {
            System.out.println("---------SKAL HENTE KONTO-----------"+Thread.currentThread());
            em.getTransaction().begin();
            Konto fra = finnKonto(fraKontoNr);
            Konto til = finnKonto(tilKontoNr);
            /*
                Lag en commit her for å få problemer med feil verdier i database
             */

           // em.getTransaction().commit();
            System.out.println("---------HENTET KONTO-----------");
            System.out.println(fra.toString() + til.toString());
            try {
                Thread.sleep(1000);
            } catch (Exception e){
                e.printStackTrace();
            }
        //    em.getTransaction().begin();
            fra.trekk(-beløp);
            til.trekk(beløp);
            em.merge(fra);
            em.merge(til);
            em.getTransaction().commit();
        }
        catch (RollbackException re){
            overførPenger(fraKontoNr,tilKontoNr,beløp);
        }
        finally {
            em.close();
        }
    }

}
