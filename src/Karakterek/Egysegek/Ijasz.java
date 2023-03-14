package Karakterek.Egysegek;

import Csatater.Csatater;
import Karakterek.Egyseg;
import Karakterek.Hos;
import Kezelok.IO.BemenetKezelo;
import Kivetelek.JatekKivetel;

import java.util.ArrayList;

import static KonzolSzinek.KonzolSzinek.*;

/**
 * Íjász: speciális Egyseg. Ára: 6 arany<br>
 * Default adatok:<br>
 * + sebzése: [2, 4]<br>
 * + életereje: 3<br>
 * + sebesség: 4<br>
 * + kezdeményezés: 9<br>
 * Speciális képessége: távolra lövés.
 */
public class Ijasz extends Egyseg {
    public Ijasz() {
        nev = "Íjász";
        ar = 6;
        sebzes = new ArrayList<>() {{
            add(2);
            add(4);
        }};
        eletero = 3;
        sebesseg = 4;
        kezdemenyezes = 9;

        jeloles = "\uD83C\uDFF9";
    }

    @Override
    public void tamadas(Csatater csatater, Hos emberHos, Hos gepHos) {
        while (true) {
            System.out.println("Add meg melyik ellenfelet szeretnéd megtámadni? (x, y)");

            if (ijaszKorulVanEllenfelEEgyseg(csatater)) {
                System.out.println(" Közelharci támadás!");
            } else {
                System.out.println(" Távoli lövés!");
            }

            try {
                int[] koordinatak = BemenetKezelo.koordinatakBekerese();

                int y = koordinatak[0];
                int x = koordinatak[1];

                if (x == -1 || y == -1) {
                    return;
                }

                csatater.uresEAMezo(x, y);

                Egyseg ellenfelEgyseg = csatater.palya[x][y];
                sajatEgysegemetTamadomE(ellenfelEgyseg);

                if (ijaszKorulVanEllenfelEEgyseg(csatater)) {
                    BemenetKezelo.valaszottKoordinatakAzEgysegSebessegenBelulVannak(this.x, this.y, ellenfelEgyseg.getX(), ellenfelEgyseg.getY(), 1);
                    int sebzes = emberHos.getTulajdonsagok().get("tamadas") * 2;

                    // sebzés kiszámítása
                    System.out.println(GREEN + "Sikeres támadás!" + RESET);
                    System.out.println(ellenfelEgyseg);
                    ellenfelEgyseg.setEletero(ellenfelEgyseg.getEletero() - sebzes);
                } else {
                    System.out.println(GREEN + "Sikeres támadás!" + RESET);
                    System.out.println(ellenfelEgyseg);

                }

                tamadasAlgoritmus(ellenfelEgyseg, emberHos, gepHos);
                System.out.println(ellenfelEgyseg);
                System.out.println();


                this.setVoltEmar(true);

                return;
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean ijaszKorulVanEllenfelEEgyseg(Csatater csatater) {
        int tavolsag = 1;

        for (int x = this.x - tavolsag; x < this.x + tavolsag; x++) {
            for (int y = this.y - tavolsag; y < this.y + tavolsag; y++) {
                if (x < 0 || x >= Csatater.MAGASSAG || y < 0 || y >= Csatater.SZELESSEG) continue;

                Egyseg egyseg = csatater.palya[x][y];
                if (egyseg instanceof NullEgyseg) continue;

                if (!egyseg.tulajdonos.equals(this.tulajdonos)) return true;

            }
        }

        return false;
    }
}
