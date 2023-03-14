package Csatater;

import Karakterek.Egyseg;
import Karakterek.Egysegek.NullEgyseg;
import Kezelok.IO.BemenetKezelo;
import Kivetelek.JatekKivetel;

/**
 * A csatatér implementálása, és annak ellenőrző metódusai.
 */
public class Csatater {
    public static final int MAGASSAG = 10;
    public static final int SZELESSEG = 12;
    public Egyseg[][] palya;

    // G _ _ _ _ _ _ _ G A D
    // H H _ _ _ _ _ _ _ A E
    // K K _ _ _ _ _ _ _ X X
    // K K _ _ _ _ _ _ _ X X

    // melyik egység támadjon x, y: 1 1
    // mit  x, y: 1 3


    /**
     * A csatatér inicializálása.
     */
    public Csatater() {
        palyaInit();
    }

    /**
     * A pálya inicializálása az Egyseg-ek jelölése alapján.
     * NullEgyseg: jelölése "_". Ezek az alapértelmezett mezők.
     */
    private void palyaInit() {
        palya = new Egyseg[MAGASSAG][SZELESSEG];

        for (int x = 0; x < MAGASSAG; x++) {
            for (int y = 0; y < SZELESSEG; y++) {
                palya[x][y] = new NullEgyseg();
            }
        }

    }

    /**
     * JatekKivetel-t dob, ha az adott koordinátában üres a mező.
     * @param x használni kívánt x koordináta
     * @param y használni kívánt y koordináta
     * @throws JatekKivetel saját kivétel osztály
     */
    public void uresEAMezo(int x, int y) throws JatekKivetel {
        BemenetKezelo.valaszottKoordinatakACsataterenBelulVannak(x, y);

        if (palya[x][y] instanceof NullEgyseg) {
            throw new JatekKivetel("Ezen a koordinátán nincs egység!");
        }
    }

    /**
     * JatekKivetel-t dob, ha az adott koordinátában nem üres a mező.
     * @param x használni kívánt x koordináta
     * @param y használni kívánt y koordináta
     * @throws JatekKivetel saját kivétel osztály
     */
    public void nemUresEAMezo(int x, int y) throws JatekKivetel {
        if (!(palya[x][y] instanceof NullEgyseg)) {
            throw new JatekKivetel("Ezen a koordinátán van egység!");
        }
    }


    /**
     * JatekKivetel-t dob, ha az adott koordinátában foglalt a mező.
     * @param x használni kívánt x koordináta
     * @param y használni kívánt y koordináta
     * @throws JatekKivetel saját kivétel osztály
     */
    public void foglaltEAMezo(int x, int y) throws JatekKivetel {
        if (palya[x][y] instanceof NullEgyseg) {
            throw new JatekKivetel("Ezen a koordinátán már van egy egység!");
        }
    }

    /**
     * Megvizsgálja az egész pályát, és ha talál rajta 0 életerővel rendelkező
     * egységet, akkor azt törli a csatatérről, és NullEgyseg-et rak a helyébe.
     */
    public void halottEgysegketTorleseACsataterrol() {
        for (int x = 0; x < SZELESSEG; x++) {
            for (int y = 0; y < MAGASSAG; y++) {
                if (palya[y][x] instanceof NullEgyseg) continue;

                if (palya[y][x].getEletero() == 0) {
                    palya[y][x] = new NullEgyseg();
                }
            }
        }
    }

    /**
     * A paraméterben kapott egységet felhelyezi a pályára.
     * @param ujEgyseg a letenni kívánt egység a pályára.
     */
    public void egysetLehelyezElsoSzabadKoordinatara(Egyseg ujEgyseg) {
        for (int x = 0; x < SZELESSEG; x++) {
            for (int y = 0; y < MAGASSAG; y++) {
                if (palya[y][x] instanceof NullEgyseg) {
                    palya[y][x] = ujEgyseg;
                    return;
                }
            }
        }
    }


}
