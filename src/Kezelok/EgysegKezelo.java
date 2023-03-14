package Kezelok;

import Csatater.Csatater;
import Karakterek.Egyseg;
import Karakterek.Egysegek.NullEgyseg;
import KonzolSzinek.KonzolSzinek;

import java.util.ArrayList;
import java.util.List;

import static Karakterek.Egyseg.createEgyseg;
import static Karakterek.Egyseg.egysegek;

/**
 * Kezeli: az egységek vásárlásai & támadás & gyógyítás alatt módosult tulajdsonságait,
 * a listából való eltávolításokat, a halott egységeket.
 */
public class EgysegKezelo {

    public static void vasarlasnalEleteroLetszamKezelo(Egyseg alanyEgyseg, Egyseg eredetiEgyseg, int valasztottMennyiseg) {
        // létszám
        alanyEgyseg.setLetszam(alanyEgyseg.getLetszam() + valasztottMennyiseg);

        // életerő
        alanyEgyseg.setEletero(eredetiEgyseg.getEletero() * alanyEgyseg.getLetszam());
    }

    public static void tamadasnalEleteroLetszamKezelo(Egyseg tamadottEgyseg, int bevittSebzes) {
        for (Egyseg eredetiEgyseg : egysegek) {
            if (eredetiEgyseg.getNev().equals(tamadottEgyseg.getNev())) {
                // életerő
                tamadottEgyseg.setEletero(tamadottEgyseg.getEletero() - bevittSebzes);

                // létszám
                tamadottEgyseg.setLetszam( ((int) Math.ceil(((double) tamadottEgyseg.getEletero()) / (double) eredetiEgyseg.getEletero())) );
            }
        }

        // return tamadottEgyseg.getLetszam() <= 0;     // ha nincs több tagja az egységnek
    }

    /**
     * Először a feltámasztandó/ gyógyítandó egység maximális értékeit tárolja, amit még vásárláskor kaptunk.
     * Ezután a feltámasztandó/ gyógyítandó egység default adatait tartalmazó egységet is tároljuk.
     * Ha a gyógyító életerő mennyiség az egységnek maximum megengedett életereje fölé esne, akkor egyszerűen
     * csak mindent a maximum értékre állít. Ha viszont a maximum alatt van, akkor a megfelelő számításokkal
     * módosítja azokat az életerőt, létszámot.
     * @param feltamasztottEgyseg feltámasztandó/ gyógyítandó egység
     * @param egysegekMaxErtekeik feltámasztandó/ gyógyítandó egység maximális tulajdonság értékei
     * @param healMertek gyógyító életerő
     */
    public static void feltamasztasnalEleterotLetszamotKezel(Egyseg feltamasztottEgyseg, List<Egyseg> egysegekMaxErtekeik, int healMertek) {

        // a feltámasztandó egység maximális értékeit tároljuk, amit még vásárláskor kaptunk
        Egyseg egysegMaxErtekeivel = modositandoEgysegMaximalisErtekei(feltamasztottEgyseg, egysegekMaxErtekeik);

        // a feltámasztandó egység default adatait tartalmazó egységet is tároljuk
        Egyseg eredetiEgyseg = eredetiEgysegDefaultErtekeivel(feltamasztottEgyseg, egysegMaxErtekeivel);

        // ha a healelt életerő a maximum fölé esne
        if (feltamasztottEgyseg.getEletero() + healMertek > egysegMaxErtekeivel.getEletero()) {
            // életerő
            feltamasztottEgyseg.setEletero(egysegMaxErtekeivel.getEletero());

            // létszám
            feltamasztottEgyseg.setLetszam(egysegMaxErtekeivel.getLetszam());

            System.out.println(KonzolSzinek.GREEN + "A healelt életerő a maximum fölé esett feltétel" + KonzolSzinek.RESET);
        } else {
            // életerő
            feltamasztottEgyseg.setEletero(feltamasztottEgyseg.getEletero() + healMertek);

            // létszám
            feltamasztottEgyseg.setLetszam( ((int) Math.ceil(((double) feltamasztottEgyseg.getEletero()) / (double) eredetiEgyseg.getEletero())) );

            System.out.println(KonzolSzinek.GREEN + "A healelt életerő NEM ESETT maximum fölé feltétel" + KonzolSzinek.RESET);
        }

        // ujEgyseg.setLetszam(10);
        // ujEgyseg.setEletero(10);
        // ujEgyseg.tulajdonos = "Ember";
        // ujEgyseg.betuszin = BLUE;
    }

    public static Egyseg modositandoEgysegMaximalisErtekei(Egyseg healeltEgyseg, List<Egyseg> egysegMaxErtekek) {
        // Bejárom az egységeknek a maximum értékeit tároló tömböt
        for (Egyseg egyseg : egysegMaxErtekek) {
            if (egyseg.getNev().equals(healeltEgyseg.getNev())) {
                return egyseg;
            }
        }
        return createEgyseg("null");
    }

    public static Egyseg eredetiEgysegDefaultErtekeivel(Egyseg feltamasztottEgyseg, Egyseg egysegMaxErtekeivel) {
        for (Egyseg eredetiEgyseg : egysegek) {
            if (eredetiEgyseg.getNev().equals(feltamasztottEgyseg.getNev()) && !(egysegMaxErtekeivel instanceof NullEgyseg)) {
                // System.out.println(KonzolSzinek.GREEN + "Megvannak a fő párosítások!" + KonzolSzinek.RESET);
                return eredetiEgyseg;
            }
        }
        return createEgyseg("null");
    }

    public static void halottEgysegKezelo(List<Egyseg> egysegek, Csatater csatater, ArrayList<Egyseg> halottEgysegek) {
        halottEgysegekFrissitese(egysegek, halottEgysegek);
        jatekosEgysegenekTorlese(egysegek);
        csatater.halottEgysegketTorleseACsataterrol();
    }

    private static void halottEgysegekFrissitese(List<Egyseg> egysegek, ArrayList<Egyseg> halottEgysegek) {
        for (Egyseg egyseg : egysegek) {
            if (egyseg.getEletero() == 0 || egyseg.getLetszam() == 0) {
                halottEgysegek.add(egyseg);
            } else {
                halottEgysegek.remove(egyseg);
            }
        }
    }

    public static void jatekosEgysegenekTorlese(List<Egyseg> egysegek) {
        egysegek.removeIf(egyseg -> egyseg.getEletero() == 0 || egyseg.getLetszam() == 0);

    }

}
