package Figyelok;

import Jatekosok.Ember;
import Jatekosok.Gep;
import Jatekosok.Jatekos;
import Karakterek.Egyseg;
import Kezelok.EgysegKezelo;
import Kivetelek.JatekKivetel;

/**
 * A kör lejátszások közbeni eseményeket figyeli.
 */
public class KorFigyelo {

    /**
     * Kivétel dobása, ha valamelyik játékosnak üresek az egységi.
     * @param ember ember-t megvizsgáljuk vesztett-e.
     * @param gep gep egységeit megvizsgáljuk üres-e. Ha igen, akkor megnyertük a játékot.
     * @throws JatekKivetel saját kivétel osztály.
     */
    public void kiNyerteMegAJatekot(Ember ember, Gep gep) throws JatekKivetel {
        EgysegKezelo.jatekosEgysegenekTorlese(ember.getHos().getEgysegek());
        EgysegKezelo.jatekosEgysegenekTorlese(gep.getHos().getEgysegek());

        if (ember.getHos().getEgysegek().size() == 0) {
            throw new JatekKivetel("Sajnáljuk, vesztettél. Próbálj ki még egy kört!");
        }

        if (gep.getHos().getEgysegek().size() == 0) {
            throw new JatekKivetel("Gratulálunk! Megnyerted a játékot!");
        }
    }


    /**
     * @param jatekos az adott játékos egysége.
     * @return a soron következő egységgel tér vissza.
     */
    public Egyseg melyikEgysegKovetkezik(Jatekos jatekos) {

        Egyseg kovetkezoEgyseg = Egyseg.createEgyseg("nullEgyseg");
        int legnagyobbKezdemEgyseg = 0;
        for (Egyseg egyseg : jatekos.getHos().getEgysegek()) {
            if (egyseg.isVoltEmar()) {
                continue;
            }

            if (egyseg.getKezdemenyezes() > legnagyobbKezdemEgyseg) {
                legnagyobbKezdemEgyseg = egyseg.getKezdemenyezes();
                kovetkezoEgyseg = egyseg;
            }
        }

        return kovetkezoEgyseg;

    }
}
