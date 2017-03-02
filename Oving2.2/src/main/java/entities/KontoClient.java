package entities;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Created by evend on 3/2/2017.
 */
public class KontoClient {
    public static void main(String[] args) throws Exception{
        EntityManagerFactory emf = null;
        try {
            emf = Persistence.createEntityManagerFactory("kontoEntity");
            KontoDAO fasade = new KontoDAO(emf);

            //Testdata
            Konto konto1 = new Konto("1234", 100, "Even");
            Konto konto2 = new Konto("1233", 120, "Ole");

            fasade.lagreNyKonto(konto1);
            fasade.lagreNyKonto(konto2);

            List<Konto> kontoer = fasade.getKontoerOverSaldo(60);
            for(Konto k : kontoer){
                System.out.println(k);
            }
            Konto kontoEndret = new Konto(konto1.getKontonr(), konto1.getSaldo(), "Even Dalen");
            fasade.endreKonto(kontoEndret);

            kontoer = fasade.getKontoerOverSaldo(60);
            for(Konto k : kontoer){
                System.out.println(k);
            }
            System.out.println("---------- STARTER TRANSAKSJONER ------------");
            Thread t = new Thread(() -> {
                System.out.println("Inni thread");
                fasade.overførPenger("1234", "1233", 10);
            });
            t.start();
            System.out.println("Utenfor thread");
            fasade.overførPenger("1234","1233",10);
            t.join();

            kontoer = fasade.getKontoerOverSaldo(60);
            System.out.println("ext_res (saldo1 = 80, saldo2 = 140)");
            for(Konto k : kontoer){
                System.out.println(k);
            }

        }
        finally {
            try {
                emf.close();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}
