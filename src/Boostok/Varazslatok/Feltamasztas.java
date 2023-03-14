package Boostok.Varazslatok;

import Boostok.Varazslat;

/**
 * Feltamasztas: egy kivalasztott sajat egyseg feltamasztasa.
 * Kepessege:
 * Ha meghalt, akkor ujra lehet eleszteni a halott egyseget, ha keves
 * az elete, akkor az eleterejet lehet novelni vele.
 */
public class Feltamasztas extends Varazslat {
    public Feltamasztas() {
        nev = "Feltámasztás";
        ar = 120;
        manna = 6;
    }

    @Override
    public String varazslatLeirasa() {
        return "Egy kiválasztott saját egység feltámasztása";
    }
}
