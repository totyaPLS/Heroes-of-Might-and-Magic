package Boostok.Varazslatok;

import Boostok.Varazslat;

/**
 * Armageddon: a palyan levo osszes egysegre sebzest okoz
 */
public class Armageddon extends Varazslat {
    public Armageddon() {
        nev = "Armageddon";
        ar = 150;
        manna = 8;
    }

    @Override
    public String varazslatLeirasa() {
        return "A pályán lévő összes egységre sebzés okozása";
    }
}
