package Jatek;

import Figyelok.KorFigyelo;
import Karakterek.Egyseg;
import Karakterek.Egysegek.Demon;
import Karakterek.Egysegek.Ijasz;
import Kezelok.CsataterKezelo;
import Kezelok.IO.KimenetKezelo;
import Kezelok.JatekKezelo;
import Kezelok.VasarlasKezelo;
import Kivetelek.JatekKivetel;

import java.util.Scanner;

import static Kezelok.EgysegKezelo.halottEgysegKezelo;
import static KonzolSzinek.KonzolSzinek.*;

/**
 * Itt valósulnak meg a fő metódusok: <br>
 * A játék indítása, nehézségi szint és kezdő aranymennyiség beállítás, csata setup, vásárlások.
 */
public class Jatek {
    public String nehezsegiSzint;
    private final JatekKezelo jatekosKezelo;
    private final CsataterKezelo csataterKezelo;
    private final VasarlasKezelo vasarlasKezelo;
    private final KorFigyelo korFigyelo;

    /**
     * Konstruktor: Játék incializálása
     */
    public Jatek() {
        jatekosKezelo = new JatekKezelo();
        csataterKezelo = new CsataterKezelo();
        vasarlasKezelo = new VasarlasKezelo();
        korFigyelo = new KorFigyelo();
    }

    /**
     * Játék indítása
     * - setup() - egységek, tulajdonságok vásárlása stb.
     * - fő csata elindítása
     * @throws InterruptedException
     */
    public void jatekotIndit() throws InterruptedException {
        setup();

        csata();
    }

    /**
     * Játék alapjainak beállítása:<br>
     * Bevezető szöveg kiírása, játékosok inicializálása, arany kiosztás, vásárlások.
     */
    private void setup() throws InterruptedException {
        bevezotoKiiratas();

        // Játékosok meghatározása
        jatekosKezelo.jatekosokInicializalasa();

        // Arany kiosztása
        while (true) {
            try {
                nehezsegiSzintBeallitasa();
                aranyKiosztasa();
                aranyatKiir();
                break;
            } catch (JatekKivetel jatekKivetel) {
                System.out.println(jatekKivetel.getMessage());
            }
        }

        // Hősök kiosztása
        jatekosokHoseinekInicializalasa();

        // Vásárlások
        jatekosokHosenekTulajdonsagVasarlasa();
        jatekosokHosenekVarazslatVasarlasa();
        jatekosokHosenekEgysegekVasarlasa();
    }

    /**
     * A játékosok megveszik hőseiknek a tulajdonságokat.
     */
    private void jatekosokHosenekTulajdonsagVasarlasa() {
        while (true) {
            try {
                vasarlasKezelo.tulajdonsagVasarlasEmber(jatekosKezelo.ember);
                break;
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage());
            }
        }
        vasarlasKezelo.tulajdonsagVasarlasGep(jatekosKezelo.gep);
    }

    /**
     * A játékosok megveszik hőseiknek a varázslatokat.
     */
    private void jatekosokHosenekVarazslatVasarlasa() {
        vasarlasKezelo.varazslatVasarlasEmber(jatekosKezelo.ember);
        vasarlasKezelo.varazslatVasarlasGep(jatekosKezelo.gep);
    }

    /**
     * A játékosok megveszik hőseiknek az egységeket.
     */
    private void jatekosokHosenekEgysegekVasarlasa() throws InterruptedException {
        while (true) {
            try {
                vasarlasKezelo.egysegVasarlasEmber(jatekosKezelo.ember);

                // egységek kezdeményezésének beállítása morál alapján:
                for (Egyseg egyseg : jatekosKezelo.ember.getHos().getEgysegek()) {
                    egyseg.setKezdemenyezes(egyseg.getKezdemenyezes() + jatekosKezelo.ember.getHos().getTulajdonsagok().get("moral"));
                }

                break;
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage());
            }
        }
        // megvásárolt egységek maximális értékei embernek
        for (Egyseg egyseg : jatekosKezelo.ember.getHos().getEgysegek()) {
            Egyseg copy = Egyseg.createEgyseg(egyseg.getNev());
            copy.setEletero(egyseg.getEletero());
            copy.setLetszam(egyseg.getLetszam());
            copy.setSebzes(egyseg.getSebzes().get(0), egyseg.getSebzes().get(1));

            jatekosKezelo.ember.getHos().getEgysegekMaxErtekei().add(copy);
        }

        System.out.println(RED_BOLD + "A gép választ..." + RESET);
        Thread.sleep(1000);

        vasarlasKezelo.egysegVasarlasGep(jatekosKezelo.gep);

        // egységek kezdeményezésének beállítása morál alapján:
        for (Egyseg egyseg : jatekosKezelo.gep.getHos().getEgysegek()) {
            egyseg.setKezdemenyezes(egyseg.getKezdemenyezes() + jatekosKezelo.gep.getHos().getTulajdonsagok().get("moral"));
        }

        // megvásárolt egységek maximális értékei gépnek
        for (Egyseg egyseg : jatekosKezelo.gep.getHos().getEgysegek()) {
            Egyseg copy = Egyseg.createEgyseg(egyseg.getNev());
            copy.setEletero(egyseg.getEletero());
            copy.setLetszam(egyseg.getLetszam());
            copy.setSebzes(egyseg.getSebzes().get(0), egyseg.getSebzes().get(1));

            jatekosKezelo.gep.getHos().getEgysegekMaxErtekei().add(copy);
        }
    }


    /**
     * Minden játékosnak a nehzéségi szinthez megfelelő arany kiosztása
     */
    private void aranyKiosztasa() throws JatekKivetel {
        if (kezdoAranymennyisegBeallitas() == -1) {
            throw new JatekKivetel("Helytelen értéket írtál be!");
        }
        jatekosKezelo.ember.setArany(kezdoAranymennyisegBeallitas());
        jatekosKezelo.gep.setArany(1000);
    }

    /**
     * A nehézségi szintnek megfelelő aranymennyiség beállítása.
     * @return -1-gyel tér vissza, ha sikertelen volt a nehézségi szint választás,
     * különben az adott mennyiségű arannyal.
     */
    private int kezdoAranymennyisegBeallitas() {
        int arany = -1;
        switch (nehezsegiSzint) {
            case "1" -> arany = 1300;
            case "2" -> arany = 1000;
            case "3" -> arany = 700;
            default -> {}
        }
        return arany;
    }

    /**
     * Aranymennyiség kiírása a játékosoknak.
     */
    private void aranyatKiir() {
        System.out.println();
        System.out.println(jatekosKezelo.gep);
        jatekosKezelo.ember.aranyatKiir();
        System.out.println();
    }

    /**
     * Játékosok hőseinek inicializálása.
     */
    private void jatekosokHoseinekInicializalasa() {
        jatekosKezelo.gep.hosInicializalasa();
        jatekosKezelo.ember.hosInicializalasa();
    }

    /**
     * A játék üdvözlő szövege.
     */
    private void bevezotoKiiratas() {
        System.out.println("     ╓─────────────────────────────────────────────────╖");
        System.out.println("═════╣" + PURPLE_BOLD + " Üdvözöllek a Heroes of Might and Magic játékban " + RESET + "╠═════");
    }

    /**
     * A nehézségi szint kiválasztása.
     */
    public void nehezsegiSzintBeallitasa() {
        Scanner bill = new Scanner(System.in);
        System.out.println("\nVálassz nehézségi szintet:");
        System.out.println("1 - Könnyű");
        System.out.println("2 - Közepes");
        System.out.println("3 - Nehéz");
        System.out.print("> ");
        nehezsegiSzint = bill.nextLine();
    }


    /**
     * A csatateret inicializáló metódus meghívása, színek kiosztása, taktikai fázis.
     */
    private void csataSetup() {
        csataterKezelo.csataterInit();
        csataterKezelo.szinekKiosztasa(jatekosKezelo);

        taktikaiFazis();
    }

    /**
     * Taktikai fázis:<br>
     *  Egyes egységek lehelyezése a csatatéren.
     */
    private void taktikaiFazis() {
        System.out.println("\n═══════════════════════════════╡" + PURPLE_BOLD +
                " Taktikai Fázis " + RESET + "╞═══════════════════════════════");
        csataterKezelo.egysegekFelhelyezeseEmber(jatekosKezelo.ember);
        csataterKezelo.egysegekFelhelyezeseGep(jatekosKezelo.gep);
    }

    /**
     * Kiírja, hogy hanyadik körnél tartunk a játékban.
     * @param hanyadikKor a kör száma
     */
    public void kortKiir(int hanyadikKor) {
        System.out.println("\t\t\t\t\t   [" + BLUE_BACKGROUND + hanyadikKor + ". KÖR" + RESET + "]");
    }

    /**
     * A csata fő metódusa, ahol addig megy a ciklus, ameddig nem veszített valaki. Kör kiírások, figyelők is
     * meghívásra kerülnek.
     */
    private void csata() {
        csataSetup();

        int hanyadikkor = 1;
        while (true) {
            kortKiir(hanyadikkor);
            KimenetKezelo.csatateretKiir(csataterKezelo.csatater);
            System.out.print(jatekosKezelo.ember);

            try {
                // specialisKepessegFigyelo(jatekosKezelo.ember);
                csataterKezelo.emberKorlejatszas(jatekosKezelo.ember, korFigyelo, jatekosKezelo.gep.getHos(), jatekosKezelo.gep);

                halottEgysegKezelo(jatekosKezelo.gep.getHos().getEgysegek(), csataterKezelo.csatater, jatekosKezelo.gep.getHos().getHalottEgysegek());

                korFigyelo.kiNyerteMegAJatekot(jatekosKezelo.ember, jatekosKezelo.gep);

                // specialisKepessegFigyelo(jatekosKezelo.ember);

                csataterKezelo.gepKorlejatszas(jatekosKezelo.gep, korFigyelo, jatekosKezelo.ember.getHos(), jatekosKezelo.ember);
//                System.out.println(jatekosKezelo.gep);

                korFigyelo.kiNyerteMegAJatekot(jatekosKezelo.ember, jatekosKezelo.gep);

            } catch (JatekKivetel jatekKivetel) {
                System.out.println(jatekKivetel.getMessage());
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            hanyadikkor++;
        }
    }

    @Override
    public String toString() {
        return "jatek";
    }

}
