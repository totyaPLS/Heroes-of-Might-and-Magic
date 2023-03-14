package Jatekosok;

import Csatater.Csatater;
import Figyelok.KorFigyelo;
import Karakterek.Hos;
import Kivetelek.JatekKivetel;

/**
 * A játékban résztvevő játékosok absztrakt szülő osztálya.
 */
public abstract class Jatekos {
    protected int arany;
    protected int kovTulajdAra;
    protected Hos hos;
    protected String betuszin;

    protected Jatekos(String betuszin) {
        this.betuszin = betuszin;
    }

    /**
     * A hős inicializálásához szükséges absztrakt metódus.
     */
    public abstract void hosInicializalasa();

    /**
     * A kör lejátszásához szükséges absztrakt metódus.
     * @param csatater az adott cselekvés a pályára lesz hatással, így tovább adom paraméterben.
     * @param korFigyelo: a soron következő egység, amit a korFigyelo.melyikEgysegKovetkezik határozott meg.
     */
    public abstract void korLejatszasa(Csatater csatater, KorFigyelo korFigyelo, Hos ellenfelHos, Jatekos jatekos) throws InterruptedException, JatekKivetel;

    /**
     * Vásárlásnál az arany mennyiségének kezelése.
     * @param kivonniValo kivonni való arany mennyiség
     */
    public void aranyatFrissit(int kivonniValo) {
        arany -= kivonniValo;
    }

    /**
     * Tulajdonságok vásárlásánál az adott tulajdonság árát számolja ki.
     * @implNote A legelső tulajdonságpont elköltése 5 aranyba kerül. Onnantól kezdve minden további
     * tulajdonságpont ára 10%-kal magasabb lesz. Az árak mindig egész értékek lesznek, és minden
     * tulajdonságpont esetében felfelé kerekít. (tehát 5, 6, 7, 8, 9, 10, 11, 13. . . )
     * @param mennyiseg a tulajdonságból választott vásárolni kívánt mennyiség.
     * @return az adott mennyiségű tulajdonság ára, amit majd később felhasználunk az arany számításnál.
     */
    public int aranyatSzamol(int mennyiseg) {
        if (mennyiseg <= 0) return 0;

        int kivonniValo = 0;
        if (kovTulajdAra == 0) {   // és ha még nem vettünk tulajdonságot, ergo 0-t fizettünk
            kovTulajdAra = 5;
            kivonniValo = 5;
            for (int i = 1; i < mennyiseg; i++) {
                kivonniValo += (int) Math.ceil(kovTulajdAra + kovTulajdAra * 0.1);
                kovTulajdAra += (int) Math.ceil(kovTulajdAra * 0.1);
            }
            return kivonniValo;
        }

        for (int i = 0; i < mennyiseg; i++) {
            kivonniValo += (int) Math.ceil(kovTulajdAra + ((double) kovTulajdAra) * 0.1);
            kovTulajdAra += (int) Math.ceil(((double) kovTulajdAra) * 0.1);
        }
        return kivonniValo;
    }

    /**
     * Megvizsgálja, hogy van-e elég aranyunk a vásárláshoz.
     * @param szam levonni való arany mennyiség.
     * @return igazzal tér vissza, ha nem negatív szám az eredmény.
     */
    public boolean vaneElegArany(int szam) {
        return arany - szam >= 0;
    }

    /**
     * A játékos arany és manna mennyiség kiirásához szükséges absztrakt metódus.
     */
    public abstract void aranyEsMannaKiirasa();

    /**
     * A játékos tulajdonságainak kiirásához szükséges absztrakt metódus.
     */
    public abstract void tulajdonsagokKiirasa();

    /**
     * A játékos varázslatainak kiirásához szükséges absztrakt metódus.
     */
    public abstract void varazslatokKiirasa();

    /**
     * A játékos egységeinek kiirásához szükséges absztrakt metódus.
     */
    public abstract void egysegekKiirasa();

    // Setters & Getters

    public int getArany() {
        return arany;
    }

    public void setArany(int arany) {
        this.arany = arany;
    }

    public Hos getHos() {
        return hos;
    }

    public String getBetuszin() {
        return this.betuszin;
    }


}
