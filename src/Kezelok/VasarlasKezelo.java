package Kezelok;

import Boostok.Varazslat;
import Boostok.Varazslatok.*;
import Jatekosok.Ember;
import Jatekosok.Gep;
import Karakterek.Egyseg;
import Kivetelek.JatekKivetel;
import KonzolSzinek.KonzolSzinek;

import java.util.*;

import static Boostok.Varazslat.varazslatok;
import static Karakterek.Egyseg.egysegek;
import static Kezelok.EgysegKezelo.vasarlasnalEleteroLetszamKezelo;
import static KonzolSzinek.KonzolSzinek.*;

/**
 * Tulajdonságok, varázslatok, és egységek vásárlása ember és gép számára.
 * Az osztály ezeket hivatott kezelni.
 */
public class VasarlasKezelo {

    public void emberAdatainakKiiratasa(Ember ember) {
        System.out.println(
                BLUE + "\n============================[" + RESET + SOLID_BOLD +
                " EMBER adatai " + BLUE + "]============================" + RESET);
        ember.aranyEsMannaKiirasa();
        ember.tulajdonsagokKiirasa();
        ember.varazslatokKiirasa();
        ember.egysegekKiirasa();
    }

    public void gepAdatainakKiiratasa(Gep gep) {
        System.out.println(
                RED + "\n=============================[" + RESET + SOLID_BOLD +
                " GÉP adatai " + RED + "]=============================" + RESET);
        gep.aranyEsMannaKiirasa();
        gep.tulajdonsagokKiirasa();
        gep.varazslatokKiirasa();
        gep.egysegekKiirasa();
    }

    public String sorszamotKerdez() {
        Scanner bill = new Scanner(System.in);
        System.out.print("[Sorszám] > ");
        return bill.nextLine();
    }

    public int mennyisegetKerdez() {
        Scanner bill = new Scanner(System.in);
        System.out.print("[Mennyiség] > ");
        return bill.nextInt();
    }

    // Tulajdonsag

    public void vasarolhatoTulajdonsagKiiratasa() {
        System.out.print("""
            \nMilyen tulajdonságot vásárolnál és mennyit? Lehetőségek:
            1 - Támadás
            2 - Védekezés
            3 - Varázserő
            4 - Tudás
            5 - Morál
            6 - Szerencse
            0 - [Varázslatok vásárlása] ->
            """);
    }

    public void tulajdonsagVasarlasEmber(Ember ember) throws JatekKivetel {
        while (true) {
            try {
                emberAdatainakKiiratasa(ember);
                vasarolhatoTulajdonsagKiiratasa();

                String valasztottTulajdonsag = ember.getHos().tulajdonsagLekerdezese(sorszamotKerdez());
                if (valasztottTulajdonsag.equals("kilepes")) {return;}

                int valasztottMennyiseg = mennyisegetKerdez();
                if (valasztottMennyiseg == 0) {throw new JatekKivetel("0 tulajdonságot nem vehetsz!");}
                if (valasztottMennyiseg > 10 || ember.getHos().maxTulajdonsagMennyisegVizsgalata(valasztottTulajdonsag, valasztottMennyiseg)) {
                    throw new JatekKivetel("10-nél több tulajdonságod nem lehet ebből!");
                }
                int tulajdonsagAra = ember.aranyatSzamol(valasztottMennyiseg);

                if (!ember.vaneElegArany(tulajdonsagAra)) {throw new JatekKivetel("Nincs elég aranyad!");}

                ember.aranyatFrissit(tulajdonsagAra);
                ember.getHos().tulajdonsagBeallitasa(valasztottTulajdonsag, valasztottMennyiseg);

                System.out.println("Sikeresen megvettél " + valasztottMennyiseg + " darabot " + valasztottTulajdonsag + " tulajdonságból!");
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println(KonzolSzinek.RED_ITALIC + "Használj megfelelő formátumot!" + KonzolSzinek.RESET);
            }
        }
    }

    public void tulajdonsagVasarlasGep(Gep gep) {

        for (Map.Entry<String, Integer> tulajdonsag : gep.getHos().getTulajdonsagok().entrySet()) {
            String valasztottTulajdonsag = tulajdonsag.getKey();
            int valasztottMennyiseg = (int)(Math.random() * 5);

            int tulajdonsagAra = gep.aranyatSzamol(valasztottMennyiseg);

            if (!(gep.vaneElegArany(tulajdonsagAra))) {continue;}

            gep.aranyatFrissit(tulajdonsagAra);
            gep.getHos().tulajdonsagBeallitasa(valasztottTulajdonsag, valasztottMennyiseg);
        }
        gepAdatainakKiiratasa(gep);
    }


    // Varazslatok

    public void vasarolhatoVarazslatokKiiratasa() {
        System.out.println("\nMilyen varázslatot vásárolnál és mennyit? Lehetőségek:");
        for (int i = 0; i < varazslatok.size(); i++) {
            System.out.println(i+1 + " - " + varazslatok.get(i).getNev() +
                    " (ár: " + varazslatok.get(i).getAr() + " arany, mannaköltség: " + varazslatok.get(i).getManna() + ")");
        }
        System.out.println("0 - [Egységek vásárlása] ->");
    }

    public void varazslatVasarlasEmber(Ember ember) {

        while (true) {
            try {
                emberAdatainakKiiratasa(ember);
                vasarolhatoVarazslatokKiiratasa();
                String sorszam = sorszamotKerdez();

                if (sorszam.equals("0")) {return;};
                List<Varazslat> valasztottVarazslatList = ember.getHos().varazslatLekerdezese(sorszam);

                if (ember.getHos().vaneIlyenVarazslat(valasztottVarazslatList)) {
                    throw new JatekKivetel("Egy varázslatból maximum 1db-od lehet!");
                }

                // Lista típusban kérem azt a tulajdonságot, amit választott a felhasználó
                int valasztottVarazslatAra = valasztottVarazslatList.get(0).getAr();

                if (!ember.vaneElegArany(valasztottVarazslatAra)) {throw new JatekKivetel("Nincs elég aranyad!");}

                ember.aranyatFrissit(valasztottVarazslatAra);
                ember.getHos().varazslatBeallitasa(valasztottVarazslatList);

                System.out.println(GREEN_ITALIC + "Sikeresen megvettél egy " + valasztottVarazslatList.get(0).getNev() + " varázslatot!" + RESET);
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void varazslatVasarlasGep(Gep gep) {

        for (Varazslat varazslat : varazslatok) {
            List<Varazslat> valasztottVarazslatList = new ArrayList<>();

            if (!gep.vaneElegArany(varazslat.getAr())) {continue;}

            boolean megvesziE = Math.random() < 0.5;

            if (megvesziE) {
                gep.aranyatFrissit(varazslat.getAr());
                valasztottVarazslatList.add(varazslat);
                gep.getHos().varazslatBeallitasa(valasztottVarazslatList);
            }
        }
        gepAdatainakKiiratasa(gep);
    }


    // Egységek

    public void vasarolhatoEgysegekKiiratasa() {
        System.out.print("""
            \nMilyen egységet vásárolnál és mennyit? Lehetőségek:
            1 - Földműves (2)
            2 - Íjász (6)
            3 - Griff (15)
            4 - Démon (10)
            5 - Sárkány (30)
            0 - [Játék kezdése] ->
            """);
    }

    public void egysegVasarlasEmber(Ember ember) throws JatekKivetel {
        while (true) {
            emberAdatainakKiiratasa(ember);
            vasarolhatoEgysegekKiiratasa();
            try {
                String sorszam = sorszamotKerdez();

                if (sorszam.equals("0") && ember.getHos().getEgysegek().isEmpty()) {throw new JatekKivetel("Egy egységet legalább muszáj venned!");}
                if (sorszam.equals("0") && !(ember.getHos().getEgysegek().isEmpty())) {return;}

                Egyseg eredetiEgyseg = ember.getHos().egysegLekerdezese(sorszam);
                Egyseg valasztottEgyseg = ember.getHos().egysegLekerdezese(sorszam);
                int valasztottMennyiseg = mennyisegetKerdez();
                if (valasztottMennyiseg == 0) {throw new JatekKivetel("0 egységet nem vehetsz!");}
                int valasztottEgysegAra = valasztottEgyseg.getAr()*valasztottMennyiseg;

                if (!ember.vaneElegArany(valasztottEgysegAra)) {throw new JatekKivetel("Nincs elég aranyad!");}

                int i = ember.getHos().haLetezikAkkorIndexVisszaad(valasztottEgyseg);

                ember.aranyatFrissit(valasztottEgysegAra);
                if (i == -1) {
                    ember.getHos().egysegBeallitasa(valasztottEgyseg, valasztottMennyiseg, "Ember");
                } else {
                    Egyseg letezoEgyseg = ember.getHos().getEgysegek().get(i);
                    vasarlasnalEleteroLetszamKezelo(letezoEgyseg, eredetiEgyseg, valasztottMennyiseg);
                }

                System.out.println(KonzolSzinek.GREEN + "Sikeresen megvettél " + valasztottEgyseg + " egységet!" + KonzolSzinek.RESET);
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println(KonzolSzinek.RED_ITALIC + "Használj megfelelő formátumot!" + KonzolSzinek.RESET);
            }
        }
    }

    public void egysegVasarlasGep(Gep gep) {

        int max = maxVehetoMennyiseg(gep);
        while (gep.getArany() > 1) {
            for (Egyseg eredetiEgyseg : egysegek) {
                Egyseg valasztottEgyseg = Egyseg.createEgyseg(eredetiEgyseg.getNev());

                Random r = new Random();
                int min = 1; // min = 0 ??

                int bound = max - min;
                if (max - min < 0) {
                    bound = 0;
                }

                int valasztottMennyiseg = r.nextInt(bound) + min;
                int valasztottEgysegAra = eredetiEgyseg.getAr()*valasztottMennyiseg;

                if (!gep.vaneElegArany(valasztottEgysegAra)) {continue;}

                int i = gep.getHos().haLetezikAkkorIndexVisszaad(valasztottEgyseg);
                gep.aranyatFrissit(valasztottEgysegAra);
                if (i == -1) {
                    gep.getHos().egysegBeallitasa(valasztottEgyseg, valasztottMennyiseg, "Gep");
                } else {
                    Egyseg letezoEgyseg = gep.getHos().getEgysegek().get(i);
                    vasarlasnalEleteroLetszamKezelo(letezoEgyseg, eredetiEgyseg, valasztottMennyiseg);
                }
            }
            max--;
        }

        gepAdatainakKiiratasa(gep);
    }

    public int maxVehetoMennyiseg(Gep gep) {
        List<Egyseg> arakPakolasos = egysegek;
        int max = 0;
        do {
            int largest = 0;
            for (int i = 1; i < arakPakolasos.size(); i++) {
                if ( arakPakolasos.get(i).getAr() > arakPakolasos.get(largest).getAr()) {
                    largest = i;
                    max = arakPakolasos.get(i).getAr();
                }
            }
            if (gep.getArany()/max < 1) {
                arakPakolasos.remove(largest);
            }
        } while (gep.getArany()/max < 1);

        return gep.getArany()/max;
    }


}
