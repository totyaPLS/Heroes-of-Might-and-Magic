package Jatekosok;

import Boostok.Varazslat;
import Csatater.Csatater;
import Figyelok.KorFigyelo;
import Karakterek.Egyseg;
import Karakterek.Egysegek.NullEgyseg;
import Karakterek.Hos;
import Kezelok.IO.KimenetKezelo;
import Kivetelek.JatekKivetel;
import Kivetelek.MannaKivetel;
import KonzolSzinek.KonzolSzinek;
import jdk.security.jarsigner.JarSignerException;

import java.util.InputMismatchException;
import java.util.Scanner;

import static Kezelok.EgysegKezelo.halottEgysegKezelo;
import static Kezelok.IO.BemenetKezelo.*;
import static KonzolSzinek.KonzolSzinek.*;

/**
 * Speciális Jatekos osztály. Ez a játékos játszik a gép ellen.
 */
public class Ember extends Jatekos {
    public Ember(String betuszin) {
        super(betuszin);
    }

    @Override
    public void hosInicializalasa() {
        super.hos = new Hos("Ember");
    }

    /**
     * Választhat, hogy mit szeretne csinálni a kör alatt.
     * @param csatater az adott cselekvés a pályára lesz hatással, így tovább adom paraméterben.
     */
    public void korLejatszasa(Csatater csatater, KorFigyelo korfigyelo, Hos ellenfelHos, Jatekos jatekos) {
        egysegekVolteMarFrissitese();

        boolean hostHasznaltE = false;
//        boolean egysegetHasznaltE = false;

        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Mit szeretnél csinálni?");
            System.out.print("""
                    1 - Hőst használok
                    2 - Egységet használok
                    0 - [Hőst és/vagy Egységet nem használva tovább a következő körre] ->
                    """);
            System.out.print("[Sorszám] > ");

            try {
                int valasz = sc.nextInt();
                switch (valasz) {
                    case 1:
                        hostValaszthatomE(hostHasznaltE);
                        cselekvesValasztas(csatater);
                        hostHasznaltE = true;
                        break;
                    case 2:
//                        egysegetValaszthatomE(egysegetHasznaltE);
                        egysegInterakcio(korfigyelo, csatater, ellenfelHos);

//                        egysegetHasznaltE = true;
                        break;
                    case 0:
                        //varakozas();
                        // tovább a csatára
                        return;
                    default:
                        System.out.println(RED_ITALIC + "Rossz válasz!" + RESET);
                }

                if (hostHasznaltE) {
                    return;
                }
            } catch (JatekKivetel jatekKivetel) {
                System.out.println(jatekKivetel.getMessage());
                hostHasznaltE = false;
//                egysegetHasznaltE = false;
            } catch (InputMismatchException mismatchException) {
                System.out.println(RED_ITALIC + "Helytelen formátum a fő cselekvés választásnál!\n" + RESET);
                hostHasznaltE = false;
//                egysegetHasznaltE = false;
            }
        }
    }

    private void egysegekVolteMarFrissitese() {
        for (Egyseg egyseg : getHos().getEgysegek()) {
            egyseg.setVoltEmar(false);
        }
    }

    private void egysegInterakcio(KorFigyelo korfigyelo, Csatater csatater, Hos gepHos) {
        Egyseg kovetkezoEgyseg = korfigyelo.melyikEgysegKovetkezik(this);

        if (kovetkezoEgyseg instanceof NullEgyseg) {
            System.out.println(RED + "Már minden egységeddel lépétél!" + RESET);
            return;
        }

        KimenetKezelo.csatateretKiir(csatater);

        System.out.println(BLUE + "A(z) " + kovetkezoEgyseg.getNev() +
                " egységed következik! Kezdeményezés morállal együtt: " + kovetkezoEgyseg.getKezdemenyezes() + RESET);

        System.out.println("Add meg mit szeretnél csinálni?");
        System.out.println("1 - Mozgás");
        System.out.println("2 - Várakozás");
        System.out.println("3 - Támadás");
        System.out.println("0 - Mégse");

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String valasz = sc.nextLine();

            switch (valasz) {
                case "1" -> {
                    kovetkezoEgyseg.mozgas(csatater);
                    return;
                }
                case "2" -> {
                    kovetkezoEgyseg.varakozas();
                    kovetkezoEgyseg.setVoltEmar(true);
                    return;
                }
                case "3" -> {
                    System.out.println("== Támadás ==");
                    System.out.println(BLUE + "Egység: " + kovetkezoEgyseg.getNev()  + RESET);
                    kovetkezoEgyseg.tamadas(csatater, this.hos, gepHos);

                    halottEgysegKezelo(getHos().getEgysegek(), csatater, getHos().getHalottEgysegek());
                    KimenetKezelo.csatateretKiir(csatater);

                    return;
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("Ilyen válaszlehetőség nincs");
            }
        }
    }

    /**
     * Támadás vagy Varázslás választása.
     * @param csatater az adott cselekvés a pályára lesz hatással, így tovább adom paraméterben.
     */
    public void cselekvesValasztas(Csatater csatater) {
        while (true) {
            System.out.println("\nMit szeretnél a hőssel csinálni?");
            System.out.print("""
                1 - Támadni
                2 - Varázsolni
                0 - Mégse
                """);

            System.out.print("[Sorszám] > ");

            Scanner sc = new Scanner(System.in);
            int valasz = sc.nextInt();

            try {
                KimenetKezelo.csatateretKiir(csatater);

                switch (valasz) {
                    case 1 -> tamadas(csatater);
                    case 2 -> varazslas(csatater);
                    case 0 -> throw new JatekKivetel("-> Vissza");
                    default -> throw new JatekKivetel(RED_ITALIC + "HIBA! Nincs ilyen választási lehetőség!\n" + RESET);
                }

                // Következő kör
                return;
            } catch (InputMismatchException ime) {
                System.out.println(RED_ITALIC + "Helytelen formátum hős cselekvés választásnál!\n" + RESET);
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage());
                return;
            }
        }


    }

    /**
     * A támadás megvalósítása, koordináták bekérésével.
     * @param csatater a támadás a pályára lesz hatással, így ezt várom paraméterben.
     */
    private void tamadas(Csatater csatater)  {
        while (true) {
            System.out.println("\nHova mérnéd a támadást?");
            int x = koordinatatBeker("x");
            int y = koordinatatBeker("y");
            try {
                csatater.uresEAMezo(x, y);
                getHos().sajatomatTamadomE(csatater.palya[x][y]);

                hos.tamad(csatater, x, y);
                break;
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage());
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                System.out.println(RED_ITALIC + "Maradj a pályán belül!" + RESET);
            }
        }
    }

    /**
     * Kivételt dob, ha nem választhatom mégegyszer a hőst.
     * @param hostHasznaltE ha true, akkor már használtuk a hőst, tehát dobódik a kivétel.
     * @throws JatekKivetel saját kivétel osztály.
     */
    public void hostValaszthatomE(boolean hostHasznaltE) throws JatekKivetel {
        if (hostHasznaltE) {
            throw new JatekKivetel("Egy körben csak egyszer használható a hős!\n");
        }
    }

    /**
     * Kivételt dob, ha nem választhatom mégegyszer az egységet.
     * @param egysegetHasznaltE ha true, akkor már használtuk az egységet, tehát dobódik a kivétel.
     * @throws JatekKivetel saját kivétel osztály.
     */
    public void egysegetValaszthatomE(boolean egysegetHasznaltE) throws JatekKivetel {
        if (egysegetHasznaltE) {
            throw new JatekKivetel("Egy körben csak egyszer lehet használni az egységeket!\n");
        }
    }

    /**
     * Megkérdezi a felhasználótól, hogy milyen varázslatot szeretne használni. A válasznak megfelelően
     * vannak meghívva a különböző varázslatok.
     * @param csatater az adott varázslat a pályára lesz hatással, így tovább adom paraméterben.
     * @throws JatekKivetel saját kivétel osztály.
     */
    public void varazslas(Csatater csatater) throws JatekKivetel {
        while (true) {
            try {
                boolean sikeresVoltAMetodus = true;
                Varazslat varazslat = varazslatKivalasztasa();

                if (varazslat == null) {
                    throw new JatekKivetel("");
                }

                getHos().vanEElegManna(varazslat);

                switch (varazslat.getNev()) {
                    case "Villámcsapás" -> sikeresVoltAMetodus = hos.villamcsapas(csatater, varazslat);
                    case "Tűzlabda" -> sikeresVoltAMetodus = hos.tuzlabda(csatater, varazslat);
                    case "Feltámasztás" -> sikeresVoltAMetodus = hos.feltamasztas(csatater, varazslat);
                    case "Armageddon" -> hos.armageddon(csatater, varazslat);
                    case "Teleport" -> sikeresVoltAMetodus = hos.teleportotKerdez(csatater, varazslat);
                    case "0" -> {}
                    default -> {System.out.println(RED_ITALIC + "HIBA! Nincs ilyen varázslatod!\n" + RESET);
                        sikeresVoltAMetodus = false;}
                }

                halottEgysegKezelo(getHos().getEgysegek(), csatater, getHos().getHalottEgysegek());

                if (sikeresVoltAMetodus) {
                    return;
                }

            } catch (MannaKivetel mk) {
                System.out.println(mk.getMessage());
                throw new JatekKivetel("Nincs eleg manad!");
            } catch (InputMismatchException input) {
                System.out.println(RED_ITALIC + "Helytelen formátum a varázslat választásnál!" + RESET);
            }
        }
    }

    /**
     * A választott varázslat sorszámának bekérése.
     * @return visszaadja annak a varázslatnak az indexét, amit választottunk.
     */
    private Varazslat varazslatKivalasztasa()  {
        System.out.println("\nMelyik varázslatod használnád?");


        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                varazslatokKiiratasa();

                System.out.print("[Sorszám] > ");
                int index = sc.nextInt() - 1;

                if (index == -1) {
                    return null;
                }

                vanEIlyenVarazslat(index);

                return hos.getVarazslatok().get(index);
            } catch (JatekKivetel e) {
                System.out.println("\n" + e.getMessage() + "\n");
            }
        }
    }

    /**
     * Kivételt dob, ha a választott sorszám (index) helytelenül lett megadva.
     * @param index választott sorszám.
     * @throws JatekKivetel saját kivétel osztály.
     */
    private void vanEIlyenVarazslat(int index) throws JatekKivetel {
        if (index < 0 || index >= hos.getVarazslatok().size()) {
            throw new JatekKivetel("Ilyen sorszámú varázslat nincs!");
        }
    }

    private void varazslatokKiiratasa() {
        for (int i = 0; i < hos.getVarazslatok().size(); i++) {
            System.out.println(i + 1 + " - " + hos.getVarazslatok().get(i).getNev() +
                    "\t(" + hos.getVarazslatok().get(i).varazslatLeirasa() + ")");
        }
        System.out.println("0 - Mégse");
    }


    @Override
    public void aranyEsMannaKiirasa() {
        int kovetkTulajdAra = (int) Math.ceil(kovTulajdAra + (double) kovTulajdAra * 0.1);
        System.out.println(
                "╔═══════════════════════╗" +
                        "\n\t" + KonzolSzinek.YELLOW_BOLD + arany + RESET + " \uD83D\uDCB0\t║" +
                        "\t" + KonzolSzinek.BLUE_BOLD + getHos().getManna() + RESET + " \uD83E\uDDEA" +
                        "\n╚═══════════════════════╝\n");
        System.out.println("Következő első tulajdonság ennyibe kerül: " + YELLOW_BOLD + kovetkTulajdAra + RESET);
    }

    @Override
    public void tulajdonsagokKiirasa() {
        System.out.println(SOLID_UNDERLINED + "Tulajdonságaid:\n" + RESET + BLUE_BOLD + getHos().getTulajdonsagok().toString() + RESET);
    }

    @Override
    public void varazslatokKiirasa() {
        System.out.println(SOLID_UNDERLINED + "Varázslataid:" + RESET);
        for (Varazslat varazslat : getHos().getVarazslatok()) {
            System.out.println(KonzolSzinek.BLUE_BOLD + varazslat + RESET);
        }
    }

    @Override
    public void egysegekKiirasa() {
        System.out.println(SOLID_UNDERLINED + "Egységeid:" + RESET);
        for (Egyseg egyseg : getHos().getEgysegek()) {
            System.out.println(BLUE_BOLD + egyseg + RESET);
        }
    }

    @Override
    public String toString() {
        String result = String.format(
                """
                %s=================[ %s%sEmber Statjai%s ]================= %s
                %s
                """, BLUE, RESET, SOLID_BOLD, BLUE, RESET, hos);

        return result;
    }

    public void aranyatKiir() {
        System.out.println("Neked ennyi aranyad van: " + arany);
    }
}
