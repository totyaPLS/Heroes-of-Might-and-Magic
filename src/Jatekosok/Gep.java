package Jatekosok;

import Boostok.Varazslat;
import Csatater.Csatater;
import Figyelok.KorFigyelo;
import Karakterek.Egyseg;
import Karakterek.Egysegek.NullEgyseg;
import Karakterek.Hos;
import Kezelok.EgysegKezelo;
import Kezelok.IO.BemenetKezelo;
import Kezelok.IO.KimenetKezelo;
import Kivetelek.JatekKivetel;
import KonzolSzinek.KonzolSzinek;

import java.util.*;

import static Kezelok.EgysegKezelo.halottEgysegKezelo;
import static Kezelok.EgysegKezelo.tamadasnalEleteroLetszamKezelo;
import static KonzolSzinek.KonzolSzinek.*;

/**
 * Speciális Jatekos osztály. Ez a játékos játszik az ember ellen.
 */
public class Gep extends Jatekos {

    public Gep(String betuszin) {
        super(betuszin);
    }

    @Override
    public void hosInicializalasa() {
        super.hos = new Hos("Gep");
    }

    /**
     * Választ, hogy mit szeretne csinálni a kör alatt random algoritmussal.<br>
     *  hős 40%<br>
     *  varázslás 50%<br>
     *  skip 10%<br>
     * @param csatater az adott cselekvés a pályára lesz hatással, így tovább adódik paraméterben.
     */
    @Override
    public void korLejatszasa(Csatater csatater, KorFigyelo korFigyelo, Hos jatekosHos, Jatekos ellenfel) throws InterruptedException, JatekKivetel {
        System.out.println(RED_BOLD + "\n [== Gép köre következik ==]" + RESET);
        Thread.sleep(500);

        for (Egyseg egyseg : getHos().getEgysegek()) {
            egyseg.setVoltEmar(false);
        }

        int random = (int) (Math.random() * 100);

        if (random < 10) {
            System.out.println("Gép átadja a kört.");
        }

        if (random < 30) {
            hosInterakcio(csatater);
            korFigyelo.kiNyerteMegAJatekot((Ember) ellenfel, this);
        } else {
            egysegInterakcio(csatater, korFigyelo, jatekosHos, ellenfel);
        }


    }

    private void hosInterakcio(Csatater csatater)  {
        System.out.println(RED_BOLD  + " [- Gép hős interakció -] " + RESET);

        int tamadasRandom = (int) (Math.random() * 100);

        if (tamadasRandom < 99) {
            tamadas(csatater);
        }
    }


    // Hős interakció

    /**
     * 1. Csatatér bejárása -> ellenfél egységeinek összegyűjtése (ArrayList<Egyseg> emberEgysegei)
     * 2. emberEgysei közül kiválaszt egy random egységet
     * 3. sebzés kiszámítása
     * 4. hallottEgysegekFrissitese
     * @param csatater
     */
    private void tamadas(Csatater csatater) {
        System.out.println(RED_BOLD + "\t[ Gép Támadás \n]" + RESET);


        List<Egyseg> emberEgysegei = new ArrayList<>();

        for (int x = 0; x < csatater.palya.length; x++) {
            for (int y = 0; y < csatater.palya[x].length; y++) {
                if (!(csatater.palya[x][y] instanceof NullEgyseg)) {
                    if (csatater.palya[x][y].getTulajdonos().equals("Ember")) {
                        emberEgysegei.add(csatater.palya[x][y]);
                    }
                }
            }
        }
        int index = (int) (Math.random() * emberEgysegei.size() );

        if (emberEgysegei.size() == 0) return;

        Egyseg randomKivalasztottEgyseg = emberEgysegei.get(index);
        System.out.println(RED + "Ezen az egységeden akar sebezni: " + randomKivalasztottEgyseg.getNev() + RESET);
        tamadasnalEleteroLetszamKezelo(randomKivalasztottEgyseg, hos.getTulajdonsagok().get("tamadas"));

        for (int x = 0; x < csatater.palya.length; x++) {
            for (int y = 0; y < csatater.palya[x].length; y++) {
                if (!(csatater.palya[x][y] instanceof NullEgyseg)) {
                    if (csatater.palya[x][y].getTulajdonos().equals("Ember") && csatater.palya[x][y].getNev().equals(randomKivalasztottEgyseg.getNev())) {
                        csatater.palya[x][y] = randomKivalasztottEgyseg;
                        System.out.println(RED + "Sebzés utáni adatai:\n" + csatater.palya[x][y] + RESET);
                    }
                }
            }
        }
    }

    // varázslás
    private void varazslas() {
        System.out.println(RED_BOLD + "  [ Gép Varázslás ] \n" + RESET);


        System.out.println(RED + " ...random varázslás \n" + RESET);
        // 1. armageddon ok
        // 2. villámcsapás
        // 3. tűzlabda ok

        // new Armageddon()
        gepArmageddon();
        gepVillamcsapas();
        gepTuzlabda();
        // ...

        // setMana
    }

    // varázslatok

    private void gepTuzlabda() {

    }

    private void gepVillamcsapas() {

    }

    private void gepArmageddon() {

    }

    // egységinterkació

    private void egysegInterakcio(Csatater csatater, KorFigyelo korfigyelo, Hos jatekosHos, Jatekos ember) throws JatekKivetel {
        System.out.println(RED_BOLD  + " [- Gép egység interakció -] " + RESET);

        while (true) {
            Egyseg kovetkezoEgyseg = korfigyelo.melyikEgysegKovetkezik(this);

            if (kovetkezoEgyseg instanceof NullEgyseg) return;

            Egyseg ellenfel = getLeggyengebbEgyseg(1, kovetkezoEgyseg.getX(), kovetkezoEgyseg.getY(), csatater, kovetkezoEgyseg.tulajdonos);

            if (ellenfel != null) {
                System.out.println(RED + "[Gép] " + kovetkezoEgyseg.getNev() + " egysége támad!" + RESET);
                gepEgysegTamadas(kovetkezoEgyseg, csatater, jatekosHos);
                kovetkezoEgyseg.setVoltEmar(true);
            } else {
                int random = (int) (Math.random() * 100);

                if (random < 10) {
                    System.out.println(RED + "[Gép] " + kovetkezoEgyseg.getNev() + " egysége várakozik!" + RESET);
                    kovetkezoEgyseg.setVoltEmar(true);
                } else {
                    System.out.println(RED + "[Gép] " + kovetkezoEgyseg.getNev() + " egysége mozog!" + RESET);
                    gepEgysegMozgatasa(kovetkezoEgyseg, csatater);
                    kovetkezoEgyseg.setVoltEmar(true);
                }
            }

            halottEgysegKezelo(getHos().getEgysegek(), csatater, getHos().getHalottEgysegek());
            KimenetKezelo.csatateretKiir(csatater);

            korfigyelo.kiNyerteMegAJatekot((Ember) ember, this);
        }
    }

    private void gepEgysegTamadas(Egyseg kovetkezoEgyseg, Csatater csatater, Hos jatekoshos) {
        int e_x = kovetkezoEgyseg.getX();
        int e_y = kovetkezoEgyseg.getY();
        Egyseg ellenfel = getLeggyengebbEgyseg(1, e_x, e_y, csatater, kovetkezoEgyseg.tulajdonos);

        if (ellenfel == null) {
            System.out.println(RED + "Ez az egység most nem támad mert nincs közelében senki:(" + RESET);
            return;
        }

        System.out.println(RED_BOLD + "[Gép] az ellenfél leggyengébb egységét támadja!");

        System.out.println(CYAN + ellenfel + RESET);

        kovetkezoEgyseg.tamadasAlgoritmus(ellenfel, jatekoshos, this.getHos());

        // Halott egységek eltávolítása
        EgysegKezelo.jatekosEgysegenekTorlese(jatekoshos.getEgysegek());

        System.out.println(RED + ellenfel + RESET);
    }

    private Egyseg getLeggyengebbEgyseg(int sebesseg, int e_x, int e_y, Csatater csatater, String tulajdonos) {
        ArrayList<Egyseg> lehetsegesEgysegek = new ArrayList<>();

        for (int x = e_x - sebesseg; x < e_x + sebesseg; x++) {
            for (int y = e_y - sebesseg; y < e_y + sebesseg; y++) {
                try {
                    BemenetKezelo.valaszottKoordinatakACsataterenBelulVannak(x, y);
                    csatater.uresEAMezo(x, y);

                    // Ha saját egység akkor skipp
                    if (csatater.palya[x][y].tulajdonos.equals(tulajdonos)) continue;

                    lehetsegesEgysegek.add(csatater.palya[x][y]);
                } catch (JatekKivetel e) {

                }
            }
        }

        lehetsegesEgysegek.sort(new LegerosebbKomparator());

        if (lehetsegesEgysegek.size() == 0) {
            return null;
        }

        return lehetsegesEgysegek.get(0);
    }

    public class LegerosebbKomparator implements Comparator<Egyseg> {
        @Override
        public int compare(Egyseg o1, Egyseg o2) {
            if (o1.getEletero() == o2.getEletero()) {
                return 0;
            }

            if (o1.getEletero() < o2.getEletero()) {
                return -1;
            }

            return 1;
        }
    }

    private void gepEgysegMozgatasa(Egyseg kovetkezoEgyseg, Csatater csatater) {
        int sebesseg = kovetkezoEgyseg.getSebesseg();
        int e_x = kovetkezoEgyseg.getX();
        int e_y = kovetkezoEgyseg.getY();

        Integer[] legbalrabb = getLegbalrabbKoordinata(sebesseg, e_x, e_y, csatater);

        kovetkezoEgyseg.egysegMozgatasa(csatater, legbalrabb[0], legbalrabb[1]);
    }

    private Integer[] getLegbalrabbKoordinata(int sebesseg, int e_x, int e_y, Csatater csateter) {
        ArrayList<Integer[]> lehetsegesKoordinatak = new ArrayList<>();

        for (int x = e_x - sebesseg; x < e_x + sebesseg; x++) {
            for (int y = e_y - sebesseg; y < e_y + sebesseg; y++) {
                try {
                    BemenetKezelo.valaszottKoordinatakACsataterenBelulVannak(x, y);
                    csateter.nemUresEAMezo(x, y);

                    Integer[] koord = {x, y};
                    lehetsegesKoordinatak.add(koord);
                } catch (JatekKivetel e) {

                }
            }
        }

        lehetsegesKoordinatak.sort(new KoordinataComparator());

        return lehetsegesKoordinatak.get(0);
    }

    public class KoordinataComparator implements Comparator<Integer[]> {
        @Override
        public int compare(Integer[] o1, Integer[] o2) {
            if (o1[1].equals(o2[1])) {
                return 0;
            }

            if (o1[1] < o2[1]) {
                return -1;
            }

            return 1;
        }
    }

    @Override
    public void aranyEsMannaKiirasa() {
        // int kovetkTulajdAra = (int) Math.ceil(kovTulajdAra + (double) kovTulajdAra*0.1);
        System.out.println(
                        "╔═══════════════════════╗" +
                        "\n\t" + KonzolSzinek.YELLOW_BOLD + arany + RESET + " \uD83D\uDCB0\t║" +
                        "\t" + KonzolSzinek.BLUE_BOLD + getHos().getManna() + RESET + " \uD83E\uDDEA" +
                        "\n╚═══════════════════════╝");
    }

    @Override
    public void tulajdonsagokKiirasa() {
        System.out.println(SOLID_UNDERLINED + "Tulajdonságai:\n" + RED + getHos().getTulajdonsagok().toString() + RESET);
    }

    public void varazslatokKiirasa() {
        System.out.println(SOLID_UNDERLINED + "Varázslatai:" + RESET);
        for (Varazslat varazslat : getHos().getVarazslatok()) {
            System.out.println(RED + varazslat + RESET);
        }
    }

    @Override
    public void egysegekKiirasa() {
        System.out.println(SOLID_UNDERLINED + "Egységei:" + RESET);
        for (Egyseg egyseg : getHos().getEgysegek()) {
            System.out.println(RED + egyseg + RESET);
        }
    }


    @Override
    public String toString() {
        return "Gépnek ennyi aranya van: " + this.arany;
    }
}
