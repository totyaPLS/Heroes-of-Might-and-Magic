package Kivetelek;

import KonzolSzinek.KonzolSzinek;

/**
 * A mannával kapcsolatos kivételeket dobja.
 */
public class MannaKivetel extends Throwable {
    public MannaKivetel(String s) {
        super(KonzolSzinek.RED_ITALIC + s + KonzolSzinek.RESET);
    }
}

