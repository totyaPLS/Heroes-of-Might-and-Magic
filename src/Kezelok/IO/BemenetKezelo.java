package Kezelok.IO;

import Csatater.Csatater;
import Kivetelek.JatekKivetel;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static KonzolSzinek.KonzolSzinek.*;

/**
 * Bekért adatok kezelése.
 */
public class BemenetKezelo {

    /**
     * @return a helyesen beírt koordinátákat adja vissza.
     */
    public static int[] koordinatakBekerese() {
        int y = koordinatatBeker("x");
        int x = koordinatatBeker("y");

        return new int[]{x, y};
    }

    public static int koordinatatBeker(String koordinata) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print(koordinata + ": ");
            return sc.nextInt();
        } catch (InputMismatchException ime) {
            System.out.println(RED_ITALIC + "Helytelen formátum a koordináta típusnál!\n" + RESET);
            return -1; // szándékosan -1 -be futtatom, mert az kint van biztosan a pályán
        }
    }

    public static void valaszottKoordinatakACsataterenBelulVannak(int x, int y) throws JatekKivetel {
        if (x < 0 || x >= Csatater.MAGASSAG || y < 0 || y >= Csatater.SZELESSEG) {
            throw new JatekKivetel(String.format("A válaszott koordináták nincsenek a csatéren: [%d, %d] (x,y)\n", y, x));
        }
    }

    public static void valaszottKoordinatakAzEgysegSebessegenBelulVannak(int e_x, int e_y, int x, int y, int sebesseg) throws JatekKivetel {
        for (int _x = e_x - sebesseg; _x <= e_x + sebesseg; _x++) {
            for (int _y = e_y - sebesseg; _y <= e_y + sebesseg; _y++) {
                if (_x == x && _y == y) {
                    return;
                }
            }
        }

        throw new JatekKivetel(String.format("A válaszott koordinátákra már nem tud eljutni az egység! [%d, %d] -> [%d, %d](x,y)\n", e_y, e_x, y, x));
    }



    public static void valasztottKoordinatakAzElsoKetOszloponBelulVannak(int x, int y) throws JatekKivetel {
        if (y != 0 && y != 1) {
            throw new JatekKivetel("Csak az első két oszlopban helyezhető el az egység!");
        }
    }
}
