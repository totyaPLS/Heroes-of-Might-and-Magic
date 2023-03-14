package Boostok.Varazslatok;

import Boostok.Varazslat;

/**
 * Tuzlabda: egy kivalasztott mezo koruli 3x3-mas teruleten levo osszes egysegre okoz sebzest
 */
public class Tuzladba extends Varazslat {
    public Tuzladba() {
        nev = "Tűzlabda";
        ar = 120;
        manna = 9;
    }

    @Override
    public String varazslatLeirasa() {
        return "Egy kiválasztott mező körüli 3x3-as területen lévő összes egységre sebzés okozása";
    }
}
