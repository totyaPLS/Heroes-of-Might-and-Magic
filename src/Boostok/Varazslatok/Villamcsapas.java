package Boostok.Varazslatok;

import Boostok.Varazslat;

/**
 * Villamcsapas: az ellenfel egysegenek koordinatajat megadva megfelelo mennyisegu sebzest okoz ra
 */
public class Villamcsapas extends Varazslat {
    public Villamcsapas() {
        nev = "Villámcsapás";
        ar = 60;
        manna = 5;
    }

    @Override
    public String varazslatLeirasa() {
        return "Egy kiválasztott ellenséges egységre sebzés okozása";
    }
}
