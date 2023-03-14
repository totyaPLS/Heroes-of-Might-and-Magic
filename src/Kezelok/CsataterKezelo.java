package Kezelok;

import Csatater.Csatater;
import Figyelok.KorFigyelo;
import Jatekosok.Ember;
import Jatekosok.Gep;
import Jatekosok.Jatekos;
import Karakterek.Egyseg;
import Karakterek.Egysegek.NullEgyseg;
import Karakterek.Hos;
import Kezelok.IO.KimenetKezelo;
import Kivetelek.JatekKivetel;
import KonzolSzinek.KonzolSzinek;

import java.util.*;

import static Csatater.Csatater.SZELESSEG;
import static Kezelok.IO.BemenetKezelo.valaszottKoordinatakACsataterenBelulVannak;
import static Kezelok.IO.BemenetKezelo.valasztottKoordinatakAzElsoKetOszloponBelulVannak;
import static KonzolSzinek.KonzolSzinek.GREEN_ITALIC;
import static KonzolSzinek.KonzolSzinek.RESET;

/**
 * Csatatéren való interakciók lekezelése:
 * + Egységek felhelyezése (inicializálása)
 * + Egységek léptetése
 * + Egységek törlése
 */
public class CsataterKezelo {
    public Csatater csatater;

    public void csataterInit() {
        csatater = new Csatater();
    }

    public void egysegekFelhelyezeseEmber(Ember ember) {
        ember.egysegekKiirasa();
        int vasaroltEgysegekMerete = ember.getHos().getEgysegek().size();
        List<Egyseg> hasznaltEgysegek = new ArrayList<>();

        while (vasaroltEgysegekMerete != hasznaltEgysegek.size()) {
            Scanner sc = new Scanner(System.in);

            KimenetKezelo.csatateretKiir(csatater);

            System.out.println("Add meg melyik egységet szeretnéd lehelyezni és hova!");
            for (int i = 0; i < ember.getHos().getEgysegek().size(); i++) {
                System.out.println(i+1 + " - " + ember.getHos().getEgysegek().get(i).getNev());
            }
            System.out.print("[Sorszám] > ");

            try {
                int valasztottEgysegSorszama = sc.nextInt();

                String valasztottEgysegNeve = ""; // A beütött számhoz hozzárendelek egy egységet
                for (int i = 0; i < ember.getHos().getEgysegek().size(); i++) {
                    if (valasztottEgysegSorszama-1 == i) {
                        valasztottEgysegNeve += ember.getHos().getEgysegek().get(i).getNev();
                    }
                }

                azEgysegLerakhatoE(hasznaltEgysegek, valasztottEgysegNeve);

                System.out.print("x: ");
                int y = sc.nextInt();
                System.out.print("y: ");
                int x = sc.nextInt();

                valaszottKoordinatakACsataterenBelulVannak(x, y);
                valasztottKoordinatakAzElsoKetOszloponBelulVannak(x, y);
                vaneEgysegAMezon(x, y);


                Egyseg egyseg = getLehelyezniKivantEgyseg(ember, valasztottEgysegNeve);
                csatater.palya[x][y] = egyseg;
                egyseg.setX(x);
                egyseg.setY(y);

                System.out.println(GREEN_ITALIC + valasztottEgysegNeve + " sikeresen lehelyezve!\n" + RESET);
                hasznaltEgysegek.add(csatater.palya[x][y]);
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage()); // itt throw-oljuk a hibákat a hívott metódusokból
            } catch (InputMismatchException ime) {
                System.out.println(KonzolSzinek.RED_ITALIC + "Használj megfelelő formátumot!" + KonzolSzinek.RESET);
            }
        }
    }

    public void azEgysegLerakhatoE(List<Egyseg> hasznaltEgysegek, String valasztottEgysegNeve) throws JatekKivetel {
        for (Egyseg hasznaltEgyseg : hasznaltEgysegek) {
            if (hasznaltEgyseg.getNev().equals(valasztottEgysegNeve)) {
                throw new JatekKivetel("Egy egységet nem rakhatsz le mégegyszer!");
            }
        }
    }

    public void egysegekFelhelyezeseGep(Gep gep) {

        for (Egyseg egyseg : gep.getHos().getEgysegek()) {
            int y, x;
            do {
                Random random = new Random();
                int lowY = SZELESSEG - 2;
                int highY = SZELESSEG;
                y = random.nextInt(highY - lowY) + lowY;

                int lowX = 0;
                int highX = Csatater.MAGASSAG;
                x = random.nextInt(highX - lowX) + lowX;

            } while (!(csatater.palya[x][y] instanceof NullEgyseg));

            csatater.palya[x][y] = egyseg;
            egyseg.setX(x);
            egyseg.setY(y);

            // System.out.println(KonzolSzinek.GREEN + egyseg.getNev() + " sikeresen lehelyezve [" + y + ", " + x + "] helyre" + KonzolSzinek.RESET);
        }
    }

    public void vaneEgysegAMezon(int x, int y) throws JatekKivetel {
        if (!(csatater.palya[x][y] instanceof NullEgyseg)) {
            throw new JatekKivetel("HIBA! Ezen a koordinátán már van egy egységed!\n" );
        }
    }


    // Cselekvések

    public void emberKorlejatszas(Ember ember, KorFigyelo korfigyelo, Hos gephos, Jatekos jatekos) {
        ember.korLejatszasa(csatater, korfigyelo, gephos, jatekos);
    }

    public void gepKorlejatszas(Gep gep, KorFigyelo korFigyelo, Hos emberHos, Jatekos jatekos) throws InterruptedException, JatekKivetel {
        gep.korLejatszasa(csatater, korFigyelo, emberHos, jatekos);
    }

    private Egyseg getLehelyezniKivantEgyseg(Ember ember, String valasztottEgysegNeve) throws JatekKivetel {
        for (Egyseg egyseg : ember.getHos().getEgysegek()) {
            if (egyseg.getNev().equals(valasztottEgysegNeve)) {
                return egyseg;
            }
        }
        throw new JatekKivetel("Ilyen egységed nincs: " + valasztottEgysegNeve + "\n");
    }

    public void szinekKiosztasa(JatekKezelo jatekKezelo) {
        egysegekKiszinezese(jatekKezelo.ember);
        egysegekKiszinezese(jatekKezelo.gep);
    }

    private void egysegekKiszinezese(Jatekos jatekos) {
        for (Egyseg egyseg : jatekos.getHos().getEgysegek()) {
            egyseg.setBetuszin(jatekos.getBetuszin());
        }
    }

}
