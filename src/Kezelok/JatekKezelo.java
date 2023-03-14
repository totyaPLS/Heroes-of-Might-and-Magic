package Kezelok;

import Jatekosok.Ember;
import Jatekosok.Gep;
import KonzolSzinek.KonzolSzinek;

/**
 * Későbbi bővítés esetén az esetleges játékmódok kezelése.
 * Jelenleg csak a normál van elkészítve.
 */
public class JatekKezelo {
    public Ember ember;
    public Gep gep;

    public void jatekosokInicializalasa() {
        System.out.println("     ╙──────────────╖ Normál játékmód ╓────────────────╜");
        System.out.println("\t\t\t\t\t╙─────────────────╜");
        normal();
    }

    private void normal() {
        this.gep = new Gep(KonzolSzinek.RED_BOLD);
        this.ember = new Ember(KonzolSzinek.BLUE_BOLD);
    }
}
