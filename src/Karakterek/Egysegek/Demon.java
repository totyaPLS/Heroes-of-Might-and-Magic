package Karakterek.Egysegek;

import Karakterek.Egyseg;

import java.util.ArrayList;

/**
 * Démon: speciális Egyseg. Ára: 10 arany<br>
 * Default adatok:<br>
 * + sebzése: [3, 6]<br>
 * + életereje: 15<br>
 * + sebesség: 4<br>
 * + kezdeményezés: 10<br>
 * Speciális képessége: az általa kiosztható sebzés maximális értékét viszi be
 */
public class Demon extends Egyseg {
    public Demon() {
        nev = "Démon";
        ar = 10;
        sebzes = new ArrayList<>() {{
            add(3);
            add(6);
        }};
        eletero = 15;
        sebesseg = 4;
        kezdemenyezes = 10;

        jeloles = "\uD83D\uDE08";
    }
}
