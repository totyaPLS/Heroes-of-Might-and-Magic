package Karakterek;

import Boostok.Varazslat;
import Boostok.Varazslatok.*;
import Csatater.Csatater;
import Karakterek.Egysegek.*;
import Kivetelek.JatekKivetel;
import Kivetelek.MannaKivetel;

import java.util.*;

import static Csatater.Csatater.MAGASSAG;
import static Csatater.Csatater.SZELESSEG;
import static Kezelok.EgysegKezelo.*;
import static Kezelok.IO.BemenetKezelo.koordinatakBekerese;
import static Kezelok.IO.BemenetKezelo.valaszottKoordinatakACsataterenBelulVannak;
import static KonzolSzinek.KonzolSzinek.*;

/**
 * A játékban résztvevő játékosok hőse.
 */
public class Hos {
    protected Map<String, Integer> tulajdonsagok;
    protected List<Varazslat> varazslatok;
    protected List<Egyseg> egysegek;
    protected ArrayList<Egyseg> halottEgysegek;
    protected int manna;

    protected String tulajdonos;

    protected List<Egyseg> egysegekMaxErtekei;

    public Hos(String tulajdonos) {
        this.tulajdonsagok = tulajdonsagokFeltoltese();
        this.varazslatok = new ArrayList<>();
        this.halottEgysegek = new ArrayList<>();
        this.egysegek = new ArrayList<>();
        this.egysegekMaxErtekei = new ArrayList<>();
        this.manna = tulajdonsagok.get("tudas")*10;

        this.tulajdonos = tulajdonos;
    }


    /**
     * A Jatekos által beírt sorszámot alakítja át az annak megfelelő tulajdonság nevére.
     * @param valasztottTulajdonsag A Jatekos által választott tulajdonság.
     * @return a tulajdonság neve.
     * @throws JatekKivetel kivételt dob, ha helytelen sorszám érkezett paraméterben.
     */
    public String tulajdonsagLekerdezese(String valasztottTulajdonsag) throws JatekKivetel {
        String tulajdonsag = "";
        switch (valasztottTulajdonsag) {
            case "1" -> tulajdonsag = "tamadas";
            case "2" -> tulajdonsag = "vedekezes";
            case "3" -> tulajdonsag = "varazsero";
            case "4" -> tulajdonsag = "tudas";
            case "5" -> tulajdonsag = "moral";
            case "6" -> tulajdonsag = "szerencse";
            case "0" -> tulajdonsag = "kilepes";
            default -> throw new JatekKivetel("Helytelen értéket írtál be!");
        }
        return tulajdonsag;
    }

    /**
     * A tulajdonságok Map feltöltése alapértelmezett 1 értékkel.
     * @return a tulajdonságok Map-ban.
     */
    public Map<String, Integer> tulajdonsagokFeltoltese() {
        Map<String, Integer> tulajdonsagok = new TreeMap<>();

        tulajdonsagok.put("tamadas", 1);
        tulajdonsagok.put("vedekezes", 1);
        tulajdonsagok.put("varazsero", 1);
        tulajdonsagok.put("tudas", 1);
        tulajdonsagok.put("moral", 1);
        tulajdonsagok.put("szerencse", 1);
        return tulajdonsagok;
    }

    /**
     * Megvizsgálja, hogy 10-nél több tulajdonságunk lenne-e vásárlásnál.
     * @param tulajdonsag a hozzáadni kívánt tulajdonság neve.
     * @param mennyiseg a hozzáadni kívánt tulajdonság mennyisége.
     * @return igazzal tér vissza, ha elértük a maximális mennyiséget.
     */
    public boolean maxTulajdonsagMennyisegVizsgalata(String tulajdonsag, int mennyiseg) {
        return getTulajdonsagok().get(tulajdonsag) + mennyiseg > 10;
    }

    /**
     * A választott tulajdonságot hozzáadja a tulajdonsagok Map-hoz. Ha az adott
     * tulajdonság a "tudas", akkor annak megfelelően a mannát is beállítja.
     * @param valasztottTulajdonsag a választott tulajdonság.
     * @param valasztottMennyiseg a választott tulajdonság mennyisége.
     */
    public void tulajdonsagBeallitasa(String valasztottTulajdonsag, int valasztottMennyiseg) {

        tulajdonsagok.put(valasztottTulajdonsag, tulajdonsagok.get(valasztottTulajdonsag)+valasztottMennyiseg);

        if (valasztottTulajdonsag.equals("tudas")) {
            this.manna += valasztottMennyiseg*10;
        }
    }


    /**
     * A Jatekos által beírt sorszámnak megfelelően inicializál egy varázslatot.
     * @param valasztottVarazslat A Jatekos által választott varázslat.
     * @return Varazslat típusú lista.
     * @throws JatekKivetel kivételt dob, ha helytelen sorszám érkezett paraméterben.
     */
    public List<Varazslat> varazslatLekerdezese(String valasztottVarazslat) throws JatekKivetel {
        List<Varazslat> varazslat = new ArrayList<>();
        switch (valasztottVarazslat) {
            case "1" -> varazslat.add(new Villamcsapas());
            case "2" -> varazslat.add(new Tuzladba());
            case "3" -> varazslat.add(new Feltamasztas());
            case "4" -> varazslat.add(new Armageddon());
            case "5" -> varazslat.add(new Teleport());
            default -> throw new JatekKivetel("Helytelen értéket írtál be!");
        }
        return varazslat;
    }

    /**
     * Megvizsgálja, hogy lett-e vásárolva már az adott varázslat. Mivel mindenből
     * csak 1 darab lehet (de azon kívül bármennyi fajta varázslatunk), ezért ezt itt le lett kezelve.
     * @param varazslat a vásárolni kívánt varázslat
     * @return igazzal tér vissza, ha már van ilyen varázslatunk.
     */
    public boolean vaneIlyenVarazslat(List<Varazslat> varazslat) {
        boolean vaneBenne = false;
        for (Varazslat elem : varazslatok) {
            if (elem.getNev().equals(varazslat.get(0).getNev())) {
                vaneBenne = true;
                break;
            }
        }
        return vaneBenne;
    }

    /**
     * A választott varázslat hozzáadása a Hős varázslataihoz.
     * @param valasztottVarazslatList a választott varázslat.
     */
    public void varazslatBeallitasa(List<Varazslat> valasztottVarazslatList) {
        varazslatok.add(valasztottVarazslatList.get(0));
    }


    /**
     * A Jatekos által beírt sorszámnak megfelelően inicializál egy egységet.
     * @param valasztottEgyseg A Jatekos által választott egység.
     * @return Egyseg típusú lista.
     * @throws JatekKivetel kivételt dob, ha helytelen sorszám érkezett paraméterben.
     */
    public Egyseg egysegLekerdezese(String valasztottEgyseg) throws JatekKivetel {
        switch (valasztottEgyseg) {
            case "1" -> { return new Foldmuves(); }
            case "2" -> { return new Ijasz(); }
            case "3" -> { return new Griff(); }
            case "4" -> { return new Demon(); }
            case "5" -> { return new Sarkany(); }
            default -> throw new JatekKivetel("Helytelen értéket írtál be!");
        }
    }

    /**
     * Létrehoz default adatokkal egy egységet.
     * @param egysegNeve a létrehozni kívánt egység neve.
     * @return Egyseg típussal tér vissza.
     * @throws JatekKivetel kivételt dob, ha helytelen egység név érkezett paraméterben.
     */
    public Egyseg egysegFactory(String egysegNeve) throws JatekKivetel {
        switch (egysegNeve) {
            case "Földműves" -> { return new Foldmuves(); }
            case "Íjász" -> { return new Ijasz(); }
            case "Griff" -> { return new Griff(); }
            case "Démon" -> { return new Demon(); }
            case "Sárkány" -> { return new Sarkany(); }
            default -> throw new JatekKivetel("Helytelen értéket írtál be!");
        }
    }

    /**
     * Ha létezik az adott egység az "egysegek" listában, akkor visszaadja annak indexét.
     * @param valasztottEgyseg a megnézni kívánt egység, hogy benne van-e az egysegek listába.
     * @return a létező egység indexe a listában.
     */
    public int haLetezikAkkorIndexVisszaad(Egyseg valasztottEgyseg) {
        int index = -1;

        for (Egyseg egyseg : egysegek) {
            // a neve alapján tudom csak megnézni (mert a létszám mindig különbözni fog, így az objektumok is)
            if (egyseg.nev.equals(valasztottEgyseg.nev)) {
                index = this.egysegek.indexOf(egyseg);
            }
        }
        return index;
    }

    /**
     * Berakja megfelelő létszámmal, életerővel, és sebzéssel az egységet a listába.
     * @param valasztottEgyseg amin a tulajdonságait módosítja.
     * @param valasztottMennyiseg ami függvényében módosulnak a tulajdonságai.
     * @param tulajdonos az egység tulajdonosa (ember/ gép)
     */
    public void egysegBeallitasa(Egyseg valasztottEgyseg, int valasztottMennyiseg, String tulajdonos) {
        // kért létszám beállítása
        valasztottEgyseg.setLetszam(valasztottMennyiseg);

        // beállított létszámmal és élettel az ember egységei közé rak
        valasztottEgyseg.setEletero(valasztottEgyseg.getEletero() * valasztottMennyiseg);

        valasztottEgyseg.tulajdonos = tulajdonos;

        egysegek.add(valasztottEgyseg);
    }


    // Csata cselekvések

    /**
     * A paraméterben kapott egység támadása, a tulajdonságai módosításával.
     * @param csatater a példányosított csatatér
     * @param x koordináta
     * @param y koordináta
     */
    public void tamad(Csatater csatater, int x, int y) {
        System.out.println(SOLID_UNDERLINED + "\nTámadni kívánt egység:" + RESET + RED + csatater.palya[x][y] + RESET +
                SOLID_UNDERLINED + "\nKapott sebzés:" + RESET + BLUE_BOLD + " -" + tulajdonsagok.get("tamadas") + RESET);
        tamadasnalEleteroLetszamKezelo(csatater.palya[x][y], tulajdonsagok.get("tamadas"));
        System.out.println(GREEN + "\nSikeres támadás utáni adatok az ellenfél egységéről:\n" + RESET + csatater.palya[x][y]);
    }


    // Varázslatok

    /**
     * Armageddon varázslat: minden egységre x erősségű csapást mér.
     * @param csatater a csatatér
     * @param varazslat a manna érték módosítás miatt kell
     */
    public void armageddon(Csatater csatater, Varazslat varazslat)  {
        System.out.println(RED + " ARMAGEDDON!!!");
        for (int x = 0; x < MAGASSAG; x++) {
            for (int y = 0; y < SZELESSEG; y++) {
                Egyseg tamadottEgyseg = csatater.palya[x][y];

                // Üres mezőt ne támadjunk
                if (tamadottEgyseg instanceof NullEgyseg) continue;

                tamadasnalEleteroLetszamKezelo(tamadottEgyseg, tulajdonsagok.get("varazsero") * 10);
                System.out.println(tamadottEgyseg);
            }
        }
        System.out.println(RESET);
        setManna(varazslat.getManna());
    }

    /**
     * Feltámasztás: a halott egység feltámasztása max. varázserő*50 értékkel. Ezen egység élete
     * ha így több lenne, mint volt vásárláskor, akkor azt a maximumot állítja be. Ha az egység
     * még él, akkor is ugyanez a számítás, tehát nála csak életerőt növel, nem hozza vissza a
     * halottak közül.
     * @param csatater a csatatér
     * @param varazslat a manna érték módosítás miatt kell
     * @return igazzal tér vissza, ha sikeres volt a feltámasztás
     * @throws JatekKivetel saját kivétel osztály
     */
    public boolean feltamasztas(Csatater csatater, Varazslat varazslat) throws JatekKivetel {
        System.out.println(" == Feltámasztás == ");
        
        String kivalasztottEgysegNeve;
        try {
            kivalasztottEgysegNeve = halottvagyEloEgysegNevenekKivalasztasa();

            halottVagyEloEgysegetFeleleszt(kivalasztottEgysegNeve, csatater);

            setManna(varazslat.getManna());
            return true;
        } catch (JatekKivetel e) {
            return false;
        }
    }

    /**
     * Az kiválasztott koordinátán lévő egységet át tudja helyezni a hős a
     * csatatéren bárhova, ahol nincs más egység.
     * @param csatater a csatater
     * @param x az átteleportálni kívánt egység x koordinátája
     * @param y az átteleportálni kívánt egység y koordinátája
     * @param hovaX az x koordináta, ahova szeretnénk teleportálni az egységet
     * @param hovaY az y koordináta, ahova szeretnénk teleportálni az egységet
     * @throws JatekKivetel sajat kivetel osztály
     */
    public void teleport(Csatater csatater, int x, int y, int hovaX, int hovaY) throws JatekKivetel {
        // gep miatt újra ellenőrzés:
        csatater.uresEAMezo(x, y);
        ellenfeletVarazslomE(csatater.palya[x][y]);
        csatater.foglaltEAMezo(x, y);

        csatater.palya[hovaX][hovaY] = csatater.palya[x][y];
        csatater.palya[x][y] = new NullEgyseg();
    }

    /**
     * Egy kiválasztott mező körüli 3x3-as területen lévő
     * összes (saját, illetve ellenséges) egységre
     * (varázserő * 20) sebzés okozása
     * @param csatater a csatatér
     * @param varazslat a manna érték módosítás miatt kell
     * @return igazzal tér vissza, ha sikeres volt a tűzlabda alkalmazása
     * @throws JatekKivetel sajat kivetel osztály
     */
    public boolean tuzlabda(Csatater csatater, Varazslat varazslat) throws JatekKivetel {
        System.out.println("Hova hatnál a varázslattal?");

        int[] koordinatak = koordinatakBekerese();
        int y = koordinatak[0];
        int x = koordinatak[1];
        valaszottKoordinatakACsataterenBelulVannak(x, y);

        int sugar = 1;
        int oldalHossz = 3;

        try {
            tuzlabdaKintVanEAPalyan(x, y);
        } catch (JatekKivetel e) {
            System.out.println(e.getMessage());
            return false;
        }

        System.out.println("Sebzés ELŐTTI adatok az egységekről:");
        for (int x2 = x-sugar; x2 < x+oldalHossz-1; x2++) {
            for (int y2 = y-sugar; y2 < y+oldalHossz-1; y2++) {
                if (!(csatater.palya[x2][y2] instanceof NullEgyseg)) {
                    System.out.println(csatater.palya[x2][y2]);
                }
            }
        }

        System.out.println("\nSebzés UTÁNI adatok az egységekről:");
        for (int x2 = x-sugar; x2 < x+oldalHossz-1; x2++) {
            for (int y2 = y-sugar; y2 < y+oldalHossz-1; y2++) {
                if (!(csatater.palya[x2][y2] instanceof NullEgyseg)) {
                    tamadasnalEleteroLetszamKezelo(csatater.palya[x2][y2], tulajdonsagok.get("varazsero")*20);
                    System.out.println(csatater.palya[x2][y2]);
                }
            }
        }
        setManna(varazslat.getManna());
        return true;
    }

    /**
     * Egy kiválasztott ellenséges egységre
     * (varázserő * 30) sebzés okozása
     * @param csatater a csatatér
     * @param varazslat a manna érték módosítás miatt kell
     * @return igazzal tér vissza, ha sikeres volt a villámcsapás alkalmazása
     * @throws JatekKivetel sajat kivetel osztály
     */
    public boolean villamcsapas(Csatater csatater, Varazslat varazslat) throws JatekKivetel {
        System.out.println("Hova hatnál a varázslattal?");

        int[] koordinatak = koordinatakBekerese();
        int y = koordinatak[0];
        int x = koordinatak[1];
        valaszottKoordinatakACsataterenBelulVannak(x, y);

        try {
            csatater.uresEAMezo(x, y);
            sajatomatTamadomE(csatater.palya[x][y]);

            System.out.println(SOLID_UNDERLINED + "\nTámadni kívánt ellenfél egysége:" + RESET + RED + csatater.palya[x][y] + RESET);
            tamadasnalEleteroLetszamKezelo(csatater.palya[x][y], tulajdonsagok.get("varazsero")*30);
            setManna(varazslat.getManna());
            System.out.println(GREEN + "Támadás utáni adatok az egységről:\n" + RESET + csatater.palya[x][y]);

            return true;
        } catch (JatekKivetel e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    // Segéd metódusok

    /**
     * Vizsgálja, hogy a kiválasztott egység halott, vagy élő-e.
     * @param kivalasztottEgysegNeve halott egységek között keresni kívánt egység
     * @throws JatekKivetel saját kivétel osztály
     */
    public void halottVagyEloEgysegetFeleleszt(String kivalasztottEgysegNeve, Csatater csatater) throws JatekKivetel {
        // ha halott
        for (Egyseg egyseg : halottEgysegek) {
            System.out.println(RED + "Halott egység gyógyítás előtti adatai:");
            System.out.println(egyseg + RESET);
            if (egyseg.getNev().equals(kivalasztottEgysegNeve)) {
                Egyseg ujEgyseg = egysegFactory(kivalasztottEgysegNeve); // egysegFactoryból új egységet kérünk
                halottEgysegetFeleleszt(ujEgyseg, csatater);
                return;
            }
        }

        // ha élő
        for (Egyseg egyseg : egysegek) {
            System.out.println(GREEN + "Élő egység gyógyítás előtti adatai:");
            System.out.println(egyseg + RESET);
            if (egyseg.getNev().equals(kivalasztottEgysegNeve)) {
                eloEgysegetGyogyit(egyseg);
                return;
            }
        }

        throw new JatekKivetel("Ilyen egységed nincs, és nem is volt!\n");
    }

    /**
     * Ha halott volt az választott egység, akkor kiszámolja neki a feltámasztáshoz szükséges
     * életerőt, létszámot, sebzést. Mivel csak ezen tulajdonságai változhatnak egy egységnek,
     * ezért a többit alapértelmezetten hagyjuk. Ezek után lehelyezzük az első szabad koordinátára
     * a csatatéren, majd az hős egységeihez adjuk hozzá.
     * @param ujEgyseg a már egysegFactory-ból létrehozott egység
     * @param csatater ahova le fogjuk helyezni a feltámasztott egységet
     */
    public void halottEgysegetFeleleszt(Egyseg ujEgyseg, Csatater csatater) {
        feltamasztasnalEleterotLetszamotKezel(ujEgyseg, egysegekMaxErtekei, tulajdonsagok.get("varazsero") * 50);
        csatater.egysetLehelyezElsoSzabadKoordinatara(ujEgyseg);
        egysegek.add(ujEgyseg);
        System.out.println(RED + "Feltámasztás utáni adatok az egységről:\n" + ujEgyseg + RESET);
    }

    /**
     * Ha élő volt az választott egység, akkor kiszámolja neki a gyógyításhoz szükséges
     * életerőt, létszámot, sebzést. Mivel csak ezen tulajdonságai változhatnak egy egységnek,
     * ezért a többit alapértelmezetten hagyja.
     * @param egyseg a gyógyítani kívánt egység
     */
    public void eloEgysegetGyogyit(Egyseg egyseg) {
        feltamasztasnalEleterotLetszamotKezel(egyseg, egysegekMaxErtekei, tulajdonsagok.get("varazsero") * 50);
        System.out.println(GREEN + "Feltámasztás utáni adatok az egységről:\n" + egyseg + RESET);
    }

    /**
     * Egy megadott egység feltámasztása. Ha tényleg halott, akkor visszatér annak a nevével.
     * @return halott egység neve
     * @throws JatekKivetel kivételt dob, ha nincs ilyen halott egységünk
     */
    private String halottvagyEloEgysegNevenekKivalasztasa() throws JatekKivetel {
        System.out.println("Add meg az egységet akit fel akarsz támaszatni!");

        if (halottEgysegek.size() > 0) {
            System.out.print("Halott egységeid: ");
            for (Egyseg egyseg : halottEgysegek) {
                System.out.println(CYAN + " - " + egyseg.getNev() + RESET);
            }
        }


        System.out.println("Sebzett egységeid: ");
        for (Egyseg _maxEgyseg : egysegekMaxErtekei) {
            for (Egyseg egyseg : egysegek) {
                if (egyseg.getNev().equals(_maxEgyseg.getNev())) {
                    // System.out.println("egyseg - max");
                    // System.out.println(egyseg.getLetszam() + " " + " " + _maxEgyseg.getLetszam());
                    // System.out.println(egyseg.getEletero() + " " + " " + _maxEgyseg.getEletero());

                    if (!(egyseg.getEletero() * egyseg.getLetszam() == _maxEgyseg.getEletero() * _maxEgyseg.getLetszam())) {
                        System.out.println(GREEN + egyseg.getNev() + RESET);
                    }
                }
            }
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("[Egység neve] > ");
        String valasztottEgysegNeve = sc.nextLine();

        ArrayList<String> eloEgysegekNevei = new ArrayList<>();
        for (Egyseg egyseg : egysegek) {
            eloEgysegekNevei.add(egyseg.getNev());
        }

        ArrayList<String> halottEgysegekNevei = new ArrayList<>();
        for (Egyseg egyseg : halottEgysegek) {
            halottEgysegekNevei.add(egyseg.getNev());
        }

        if (!halottEgysegekNevei.contains(valasztottEgysegNeve) && !eloEgysegekNevei.contains(valasztottEgysegNeve)) {
            throw new JatekKivetel("Ilyen egységed nincs!");
        }

        return valasztottEgysegNeve;
    }

    /**
     * Vizsgálás, hogy a tűzlabda hatása a pályán kívülre esik-e.
     * @param x koordináta, ahova a tűzlabdát szeretnénk elhelyezni
     * @param y koordináta, ahova a tűzlabdát szeretnénk elhelyezni
     * @throws JatekKivetel saját kivétel osztály
     */
    public void tuzlabdaKintVanEAPalyan(int x, int y) throws JatekKivetel {
        if (x == 0 || x == SZELESSEG - 1 || y == 0 || y == MAGASSAG - 1) {
            throw new JatekKivetel("A tűzlabda hatása a pályán kívülre is esett! Nem választhatod a pálya szélét vagy sarkait erre a célra!");
        }
    }

    public boolean teleportotKerdez(Csatater csatater, Varazslat varazslat) throws JatekKivetel {
        while (true) {
            boolean sikeresVoltAMetodus = true;

            System.out.println("Add meg az egység kezdő koordinátáit [x,y]?");

            int[] koordinatak = koordinatakBekerese();
            int y = koordinatak[0];
            int x = koordinatak[1];
            valaszottKoordinatakACsataterenBelulVannak(x, y);

            System.out.println("Add meg az egység vég koordinátáit [x,y]?");
            int[] koordinatak2 = koordinatakBekerese();
            int hovaY = koordinatak2[0];
            int hovaX = koordinatak2[1];

            csatater.uresEAMezo(x, y);
            ellenfeletVarazslomE(csatater.palya[x][y]);


            try {
                teleport(csatater, x, y, hovaX, hovaY);
            } catch (JatekKivetel e) {
                System.out.println(e.getMessage());
                sikeresVoltAMetodus = false;
            }

            if (sikeresVoltAMetodus) {
                setManna(varazslat.getManna());
                return true;
            }

        }
    }

    /**
     * Vizsgálja, hogy a saját egységemet támadom-e.
     * Ha a saját egységemet akarnám támadni, akkor kivételt dob.
     * @param kivalasztottEgyseg támadni kívánt egység
     * @throws JatekKivetel ha a saját egységemet akarnám támadni, akkor kivételt dob
     */
    public void sajatomatTamadomE(Egyseg kivalasztottEgyseg) throws JatekKivetel {
        if (this.tulajdonos.equals(kivalasztottEgyseg.tulajdonos)) {
            throw new JatekKivetel("A saját egységedet nem tudod támadni!");
        }
    }

    /**
     * Vizsgálja, hogy az ellenfél egységére próbálok-e hatni a varázslattal.
     * @param kivalasztottEgyseg varázsolni kívánt egység
     * @throws JatekKivetel kivételt dob, ha az ellenfél egységére próbálok hatni a varázslattal
     */
    public void ellenfeletVarazslomE(Egyseg kivalasztottEgyseg) throws JatekKivetel {
        if (!(this.tulajdonos.equals(kivalasztottEgyseg.tulajdonos))) {
            throw new JatekKivetel("Nem hathatsz az ellenfél egységére ezzel a varázslattal!");
        }
    }

    /**
     * Megnézi, van-e elég manna a varázsláshoz.
     * @param varazslat adott varázslat használása
     * @throws MannaKivetel kivételt dob, ha nincs elég manna a varázslat használásához
     */
    public void vanEElegManna(Varazslat varazslat) throws MannaKivetel {
        if (this.manna - varazslat.getManna() < 0) {
            throw new MannaKivetel("Nincs ehhez elég mannád!");
        }
    }


    /** Getters & Setters **/

    public int getManna() {
        return manna;
    }

    public Map<String, Integer> getTulajdonsagok() {
        return tulajdonsagok;
    }

    public List<Varazslat> getVarazslatok() {
        return varazslatok;
    }

    public List<Egyseg> getEgysegek() {
        return egysegek;
    }

    public List<Egyseg> getEgysegekMaxErtekei() {
        return egysegekMaxErtekei;
    }

    public String getTulajdonos() {
        return tulajdonos;
    }

    public void setManna(int kivonniValo) {
        this.manna = Math.max(manna - kivonniValo, 0);
    }

    public void setEredetiManna(int manna) {
        this.manna = manna;
    }

    public ArrayList<Egyseg> getHalottEgysegek() {
        return halottEgysegek;
    }

    public void setHalottEgysegek(ArrayList<Egyseg> halottEgysegek) {
        this.halottEgysegek = halottEgysegek;
    }

    public void setTulajdonos(String tulajdonos) {
        this.tulajdonos = tulajdonos;
    }

    public void setEgysegekMaxErtekei(List<Egyseg> egysegekMaxErtekei) {
        this.egysegekMaxErtekei = egysegekMaxErtekei;
    }

    public void setTulajdonsagok(Map<String, Integer> tulajdonsagok) {
        this.tulajdonsagok = tulajdonsagok;
    }

    public void setVarazslatok(List<Varazslat> varazslatok) {
        this.varazslatok = varazslatok;
    }

    public void setEgysegek(List<Egyseg> egysegek) {
        this.egysegek = egysegek;
    }



    @Override
    public String toString() {
        String result = SOLID_UNDERLINED + "Tulajdonságaid:\n" + RESET;
        result += BLUE_BOLD + tulajdonsagok.toString() + RESET + "\n\n";

        result += SOLID_UNDERLINED + "Egységeid:\n" + RESET;

        for (Egyseg egyseg : egysegek) {
            result += BLUE_BOLD + egyseg + RESET + "\n";
        }

        return result;
    }
}
