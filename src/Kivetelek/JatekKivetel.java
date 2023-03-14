package Kivetelek;

import KonzolSzinek.KonzolSzinek;

/**
 * A játék sajátos kivételeit dobja.
 */
public class JatekKivetel extends Throwable {
    public JatekKivetel(String s) {
        super(KonzolSzinek.RED_ITALIC + s + KonzolSzinek.RESET);
    }
}
