package Karakterek.Egysegek;

import Karakterek.Egyseg;

import java.util.ArrayList;

/**
 * Griff: speciális Egyseg. Ára: 15 arany<br>
 * Default adatok:<br>
 * + sebzése: [5, 10]<br>
 * + életereje: 30<br>
 * + sebesség: 7<br>
 * + kezdeményezés: 15<br>
 * Speciális képessége: végtelen visszatámadás.
 */
public class Griff extends Egyseg {
    public Griff() {
        nev = "Griff";
        ar = 15;
        sebzes = new ArrayList<>() {{
            add(5);
            add(10);
        }};
        eletero = 30;
        sebesseg = 7;
        kezdemenyezes = 15;

        jeloles = "\uD83E\uDD85";
    }
}
