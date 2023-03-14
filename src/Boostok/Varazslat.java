package Boostok;

import Boostok.Varazslatok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A varázslatok implementálása. Ezekkel rendelkezik a hős.
 */
public abstract class Varazslat {
    protected String nev;
    protected int ar;
    protected int manna;
    public static List<Varazslat> varazslatok = setVarazslatok();

    /**
     * Varazslatok lista: elemeit példányosítjuk a Varazslat típusokból.
     * @return egy Varazslat típusú lista, benne a varázslatokkal.
     */
    private static List<Varazslat> setVarazslatok() {
        List<Varazslat> varazslatok = new ArrayList<>();
        varazslatok.add(new Villamcsapas());
        varazslatok.add(new Tuzladba());
        varazslatok.add(new Feltamasztas());
        varazslatok.add(new Armageddon());
        varazslatok.add(new Teleport());
        return varazslatok;
    }

    public String getNev() {
        return nev;
    }

    public int getAr() {
        return ar;
    }

    public int getManna() {
        return manna;
    }

    public abstract String varazslatLeirasa();

    @Override
    public String toString() {
        return  "Név: " + nev +
                ", Ár: " + ar +
                ", Manna-költsége: " + manna;
    }
}
