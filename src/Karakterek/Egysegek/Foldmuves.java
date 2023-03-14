package Karakterek.Egysegek;

import Karakterek.Egyseg;

import java.util.ArrayList;

/**
 * Földműves: speciális Egyseg. Ára: 2 arany<br>
 * Default adatok:<br>
 * + sebzése: [1, 1]<br>
 * + életereje: 3<br>
 * + sebesség: 4<br>
 * + kezdeményezés: 7<br>
 * Speciális képessége: nincs.
 */
public class Foldmuves extends Egyseg {
    public Foldmuves() {
        nev = "Földműves";
        ar = 2;
        sebzes = new ArrayList<>() {{
            add(1);
            add(1);
        }};
        eletero = 3;
        sebesseg = 4;
        kezdemenyezes = 7;

        jeloles = "\uD83D\uDDE1";
    }
}
