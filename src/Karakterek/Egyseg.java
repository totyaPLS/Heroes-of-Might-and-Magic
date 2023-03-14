package Karakterek;
import Csatater.Csatater;
import Jatekosok.Jatekos;
import Karakterek.Egysegek.*;
import Kezelok.IO.BemenetKezelo;
import Kezelok.IO.KimenetKezelo;
import Kivetelek.JatekKivetel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Csatater.Csatater.SZELESSEG;
import static Kezelok.EgysegKezelo.tamadasnalEleteroLetszamKezelo;
import static KonzolSzinek.KonzolSzinek.*;

/**
 * Az egységek absztrakt szülő osztálya.
 */
public abstract class Egyseg {
    public String tulajdonos;
    protected int letszam = 0;
    protected String nev;
    protected int ar;
    protected List<Integer> sebzes;
    protected int eletero;
    protected int sebesseg;
    protected int kezdemenyezes;

    protected boolean voltEmar;

    protected int x;
    protected int y;

    protected String jeloles;
    protected String hatterszin;

    protected String betuszin;

    public static List<Egyseg> egysegek = setEgysegek();

    /**
     * Inicializál egy új egységet a kapott paraméter alapján.
     * @param egysegNeve a létrehozni kívánt egység neve.
     * @return Egyseg típusú egységgel tér vissza a paraméter alapján.
     */
    public static Egyseg createEgyseg (String egysegNeve) {
        switch (egysegNeve) {
            case "Földműves" -> {return new Foldmuves();}
            case "Íjász" -> {return new Ijasz();}
            case "Griff" -> {return new Griff();}
            case "Démon" -> {return new Demon();}
            case "Sárkány" -> {return new Sarkany();}
            default -> {return new NullEgyseg();}
        }
    }

    /**
     * Inicializálja az Egysegek típusú listát a statikus "egysegek" adattagba.
     * @return Egyseg típusú listával tér vissza.
     */
    public static List<Egyseg> setEgysegek() {
        List<Egyseg> egysegek = new ArrayList<>();
        egysegek.add(new Foldmuves());
        egysegek.add(new Ijasz());
        egysegek.add(new Griff());
        egysegek.add(new Demon());
        egysegek.add(new Sarkany());

        return egysegek;
    }

    /**
     * Ember Egység mozgásának megvalósítása:
     * - Koordináták bekérése
     * - Csak üres mező lépés ellenőrzése
     */
    public void mozgas(Csatater csatater) {
        KimenetKezelo.csatateretKiir(csatater);

        System.out.println("== Mozgás ==");
        System.out.println(BLUE + "Egység: " + this.getNev() + RESET);
        System.out.println(BLUE + "Sebesség: " + sebesseg + RESET);

        int regi_x;
        int regi_y;

        while (true) {
            System.out.println("Add meg hova szeretnéd mozgatni! [x, y] (mégse: '[-1, -1]'");
            int[] koordinatak = BemenetKezelo.koordinatakBekerese();

            int x = koordinatak[0];
            int y = koordinatak[1];

            if (x == -1 || y == -1) {
                return;
            }

            try {
                BemenetKezelo.valaszottKoordinatakACsataterenBelulVannak(x, y);
                csatater.nemUresEAMezo(x, y);
                BemenetKezelo.valaszottKoordinatakAzEgysegSebessegenBelulVannak(this.x, this.y, x, y, sebesseg);

                regi_x = this.x;
                regi_y = this.y;

                egysegMozgatasa(csatater, x, y);


                break;
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage());
            }
        }


        KimenetKezelo.csatateretKiir(csatater);

        System.out.println(GREEN + "\nSikeres lépés: " + nev + String.format("[%d, %d] -> [%d, %d]! \n", regi_x, regi_y, x, y) +  RESET);
        this.setVoltEmar(true);
    }

    /**
     * Ember Egység várakozásának megvalósítása
     */
    public void varakozas() {
        System.out.println("== Egység várakozik :) ===");
    }

    /**
     * Ember Egység támadásának a folyamata:
     * - koordináták bekérése
     * - majd adott koordinátán lévő egység megtámadása az al-algoritmusok alapján
     * - ellenőrizve, hogy van-e egység az adott mezőn + ellenfél-e
     * @param csatater
     * @param emberHos
     * @param gepHos
     */
    public void tamadas(Csatater csatater, Hos emberHos, Hos gepHos) {
        while (true) {
            System.out.println("Add meg melyik ellenfelet szeretnéd megtámadni? (x, y) (mégse: [-1, -1])");

            try {
                int[] koordinatak = BemenetKezelo.koordinatakBekerese();

                int x = koordinatak[0];
                int y = koordinatak[1];

                if (x == -1 || y == -1) {
                    return;
                }

                csatater.uresEAMezo(x, y);

                Egyseg ellenfelEgyseg = csatater.palya[x][y];
                BemenetKezelo.valaszottKoordinatakAzEgysegSebessegenBelulVannak(this.x, this.y, x, y, 1);
                sajatEgysegemetTamadomE(ellenfelEgyseg);

                tamadasAlgoritmus(ellenfelEgyseg, emberHos, gepHos);

                System.out.println(GREEN + "Sikeres támadás!" + RESET);
                System.out.println(ellenfelEgyseg);
                System.out.println(BLUE + "\nKiosztott sebzés: \n" + 100);
                System.out.println(ellenfelEgyseg);
                System.out.println();


                this.setVoltEmar(true);
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage());
            }
        }
    }


    // Seged metódusok

    /**
     * Sebzés kiszámtásának módja
     * @param ellenfelEgyseg
     * @param sajatHos
     * @param ellenfelHos
     */
    public void tamadasAlgoritmus(Egyseg ellenfelEgyseg, Hos sajatHos, Hos ellenfelHos) {
        int kiszamoltSebzes = sebzestKiszamol(sebzes, sajatHos, ellenfelHos);
        tamadasnalEleteroLetszamKezelo(ellenfelEgyseg, kiszamoltSebzes);
    }

    /**
     * Visszatámadási algoritmus megvalósítása
     * @param ellenfelEgyseg
     * @param sajatHos
     * @param ellenfelHos
     */
    public void visszaTamadasAlgoritmus(Egyseg ellenfelEgyseg, Hos sajatHos, Hos ellenfelHos) {
        int kiszamoltSebzes = ((int) Math.round(((double) sebzestKiszamol(sebzes, sajatHos, ellenfelHos)) / 2.0));
        tamadasnalEleteroLetszamKezelo(ellenfelEgyseg, kiszamoltSebzes);
    }

    /**
     * Sebzés értékének kiszámítása, beleérteve a kritikus sebzést is (szerencse alapján)
     * @param sebzesIntervallum
     * @param sajatHos
     * @param ellenfelHos
     * @return
     */
    public int sebzestKiszamol(List<Integer> sebzesIntervallum, Hos sajatHos, Hos ellenfelHos) {
        Random random = new Random();
        int alsoSebzes = sebzesIntervallum.get(0);
        int felsoSebzes = sebzesIntervallum.get(1);

        int bound = felsoSebzes - alsoSebzes;
        if (bound < 0) {
            bound = 0;
        }
        int randomSebzes = random.nextInt(bound) + alsoSebzes;


        // összeadott sebzés létszám függvényében
        int randomSebzesSum = getLetszam()*randomSebzes;

        // sebzés módosítása a hős támadás tulajdonsága függvényében
        double sebzesHossel = ((double) randomSebzesSum) * ( 1.0 + 0.1*((double) sajatHos.getTulajdonsagok().get("tamadas")) );

        // sebzés módosítása az ellenséges hős védekezésének figyelembevételével
        double sebzesGyengitett = sebzesHossel * ( (0.1 * (ellenfelHos.getTulajdonsagok().get("vedekezes"))) / 2 );

        // sebzés kerekítés
        int sebzesKerekitve = ((int) Math.round(sebzesGyengitett));

        // sebzés szerencsével
        int hosSzerencsePontja = sajatHos.getTulajdonsagok().get("szerencse") * 5;
        int szerencse = (int) (Math.random()*101);

        if (szerencse <= 50 + hosSzerencsePontja) {
            sebzesKerekitve *= 2;
        }

        return sebzesKerekitve;
    }

    /**
     * Segéd metódus annak eldöntésére, hogy a támadni kívánt egység a sajátom-e
     * @param kivalasztottEgyseg
     * @throws JatekKivetel
     */
    protected void sajatEgysegemetTamadomE(Egyseg kivalasztottEgyseg) throws JatekKivetel {
        if (this.tulajdonos.equals(kivalasztottEgyseg.tulajdonos)) {
            throw new JatekKivetel("A saját egységedet nem tudod támadni!");
        }
    }

    /**
     * Egy egység mozgatása a csatatéren
     * @param csatater
     * @param x
     * @param y
     */
    public void egysegMozgatasa(Csatater csatater, int x, int y) {
        csatater.palya[this.x][this.y] = new NullEgyseg();
        this.x = x;
        this.y = y;
        csatater.palya[x][y] = this;
    }

    // Getters & Setters

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isVoltEmar() {
        return voltEmar;
    }

    public void setVoltEmar(boolean voltEmar) {
        this.voltEmar = voltEmar;
    }

    public String getTulajdonos() {
        return tulajdonos;
    }

    public void setTulajdonos(String tulajdonos) {
        this.tulajdonos = tulajdonos;
    }

    public int getAr() {
        return ar;
    }

    public int getLetszam() {
        return letszam;
    }

    public void setLetszam(int letszam) {
        this.letszam = Math.max(letszam, 0);
    }

    public void setBetuszin(String betuszin) {
        this.betuszin = betuszin;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public void setAr(int ar) {
        this.ar = ar;
    }

    public List<Integer> getSebzes() {
        return sebzes;
    }

    public void setSebzes(int ertek1, int ertek2) {
        this.sebzes.set(0, Math.max(ertek1, 0));
        this.sebzes.set(1, Math.max(ertek2, 0));
    }

    public int getEletero() {
        return eletero;
    }

    public void setEletero(int eletero) {
        this.eletero = Math.max(eletero, 0);
    }

    public int getSebesseg() {
        return sebesseg;
    }

    public void setSebesseg(int sebesseg) {
        this.sebesseg = sebesseg;
    }

    public int getKezdemenyezes() {
        return kezdemenyezes;
    }

    public void setKezdemenyezes(int kezdemenyezes) {
        this.kezdemenyezes = kezdemenyezes;
    }

    public String getJeloles() {
        return jeloles;
    }

    public String getBetuszin() {
        return betuszin;
    }

    public static List<Egyseg> getEgysegek() {
        return egysegek;
    }

    public static void setEgysegek(List<Egyseg> egysegek) {
        Egyseg.egysegek = egysegek;
    }

    @Override
    public String toString() {
        return "Egység:  " + jeloles + " " +  nev + " "  + jeloles + "\n" +
                "Létszám: " + letszam + "\n" +
                "Életerő: " + eletero + "\n" +
                "Sebzés:  " + sebzes + "\n";
    }
}
