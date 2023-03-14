package Boostok.Varazslatok;

import Boostok.Varazslat;

/**
 * Teleport: tetszoleges helyre leptetheto a sajat egyseg
 */
public class Teleport extends Varazslat {
    public Teleport() {
        nev = "Teleport";
        ar = 180;
        manna = 12;
    }

    @Override
    public String varazslatLeirasa() {
        return "Tetszőleges helyre léptetése egy egységnek";
    }
}
