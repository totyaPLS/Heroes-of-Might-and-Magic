package Karakterek.Egysegek;

import Csatater.Csatater;
import Karakterek.Egyseg;
import Karakterek.Hos;
import Kezelok.IO.BemenetKezelo;
import Kivetelek.JatekKivetel;

import java.util.ArrayList;

import static KonzolSzinek.KonzolSzinek.*;
import static KonzolSzinek.KonzolSzinek.RESET;

/**
 * Sárkány: speciális Egyseg. Ára: 30 arany<br>
 * Default adatok:<br>
 * + sebzése: [8, 12]<br>
 * + életereje: 40<br>
 * + sebesség: 4<br>
 * + kezdeményezés: 7<br>
 * Speciális képessége: tűzokádás. Az sebzésének kétszeresét viszi be.
 */
public class Sarkany extends Egyseg {
    public Sarkany() {
        nev = "Sárkány";
        ar = 30;
        sebzes = new ArrayList<>() {{
            add(8);
            add(12);
        }};
        eletero = 40;
        sebesseg = 7;
        kezdemenyezes = 20;

        jeloles = "\uD83D\uDC32";
    }

    @Override
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

                // sebzés kiszámítása
                System.out.println(GREEN + "1. sikeres támadás!" + RESET);
                System.out.println(ellenfelEgyseg);

                tamadasAlgoritmus(ellenfelEgyseg, emberHos, gepHos);

                System.out.println(ellenfelEgyseg);
                System.out.println();

                if (ellenfelEgyseg.getEletero() == 0) {
                    this.setVoltEmar(true);
                    return;
                }

                System.out.println(GREEN + "2. sikeres támadás!" + RESET);
                System.out.println(ellenfelEgyseg);

                tamadasAlgoritmus(ellenfelEgyseg, emberHos, gepHos);

                System.out.println(ellenfelEgyseg);
                System.out.println();



                this.setVoltEmar(true);
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
