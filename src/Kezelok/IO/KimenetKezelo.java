package Kezelok.IO;

import Csatater.Csatater;
import Karakterek.Egyseg;
import KonzolSzinek.KonzolSzinek;

/**
 * Alapértelmezett kimentre való kiírásokat kezeli.
 */
public class KimenetKezelo {
    public static void csatateretKiir(Csatater csatater) {
        System.out.println("\n  ═══════════════════[ Csatatér ]═══════════════════");
        System.out.println("X┌──────────────────────────────────────────────────┐");

        for (int x = 0; x < Csatater.MAGASSAG; x++) {
            System.out.print(x + "│\t");
            for (int y = 0; y < Csatater.SZELESSEG; y++) {
                Egyseg egyseg = csatater.palya[x][y];
                if (y != Csatater.SZELESSEG - 1) {
                    System.out.print(egyseg.getBetuszin() + egyseg.getJeloles() + "\t" + KonzolSzinek.RESET);
                } else {
                    System.out.print(egyseg.getBetuszin() + egyseg.getJeloles() + KonzolSzinek.RESET);
                }
            }
            System.out.print("\t│\n");
        }

        System.out.println("X└──────────────────────────────────────────────────┘");

        System.out.print("  Y ");
        for (int i = 0; i < Csatater.SZELESSEG; i++) {
            System.out.print(i + "\t");
        }
        System.out.print(" <- Y");
        System.out.println();
    }
}
