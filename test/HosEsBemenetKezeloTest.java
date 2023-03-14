import Boostok.Varazslatok.Tuzladba;
import Karakterek.Egyseg;
import Karakterek.Egysegek.Demon;
import Karakterek.Egysegek.Ijasz;
import Karakterek.Egysegek.Sarkany;
import Karakterek.Hos;
import Kezelok.IO.BemenetKezelo;
import Kivetelek.JatekKivetel;

import Kivetelek.MannaKivetel;
import org.junit.jupiter.api.*;

import static KonzolSzinek.KonzolSzinek.GREEN_ITALIC;
import static KonzolSzinek.KonzolSzinek.RESET;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class HosEsBemenetKezeloTest {
    Hos hos;

    @BeforeEach
    void setUp() {
       hos = new Hos("Teszt");
    }

    /**
     * 1. egysegFactory
     */

    @Test
    void test_egysegFactory_1() throws JatekKivetel {
        Egyseg eredmeny = hos.egysegFactory("Íjász");
        Egyseg elvart = new Ijasz();

        assertEquals(elvart instanceof Ijasz, eredmeny instanceof Ijasz);
    }

    @Test
    void test_egysegFactory_2() {
        try {
            Egyseg eredmeny = hos.egysegFactory("hibas_egysegnev");
            fail("Hibat kellett volna kapnunk!");
        } catch (JatekKivetel e) {
            assertTrue(true);
        }
    }

    @Test
    void test_egysegFactory_3() throws JatekKivetel {
        Egyseg eredmeny = hos.egysegFactory("Démon");

        assertInstanceOf(Demon.class, eredmeny);
    }


    /**
     * 2. getHalottEgyseg
     */

    @Test
    void test_get_halott_egyseg_1() {
        ArrayList<Egyseg> halottEgysegek = new ArrayList<>();
        halottEgysegek.add(new Demon());
        halottEgysegek.add(new Ijasz());

        assertInstanceOf(Demon.class, halottEgysegek.get(0));
    }

    @Test
    void test_get_halott_egyseg_2() {
        ArrayList<Egyseg> halottEgysegek = new ArrayList<>();
        halottEgysegek.add(new Sarkany());

        for (Egyseg egyseg : halottEgysegek) {
            assertNotNull(egyseg);
        }
    }

    @Test
    void test_get_halott_egyseg_3() {
        ArrayList<Egyseg> halottEgysegek = new ArrayList<>();
        halottEgysegek.add(new Sarkany());

        for (Egyseg egyseg : halottEgysegek) {
            assertEquals(0, egyseg.getLetszam());
        }
    }


    /**
     * 3. tuzlabdaKintVanEAPalyan<br>
     * szélsőséges tesztelés OK
     */

    @Test
    void test_tuzlabdaKintVanEAPalyan_1()  {
        try {
            hos.tuzlabdaKintVanEAPalyan(1, 1);
            assertTrue(true, "A [1, 1] bent van a pályán!");
        } catch (JatekKivetel e) {
            fail("A [0, 0] koordinátára a tűzlabda már kint lenne!");
        }
    }

    @Test
    void test_tuzlabdaKintVanEAPalyan_2() {
        try {
            hos.tuzlabdaKintVanEAPalyan(0, 0);
            fail("A [0, 0] koordináta kint van a pályán!");
        } catch (JatekKivetel e) {
            assertTrue(true, "A [0, 0] már kint van a pályán!");
        }
    }

    @Test
    void test_tuzlabdaKintVanEAPalyan_3() {
        try {
            hos.tuzlabdaKintVanEAPalyan(-5, 1);
            assertTrue(true, "A [-5, 1] kint van a pályán!");
        } catch (JatekKivetel e) {
            fail("Bent van!");
        }
    }


    /**
     * 4. valaszottKoordinatakAzEgysegSebessegenBelulVannak<br>
     * szélsőséges tesztelés OK
     */

    @Test
    void test_valaszottKoordinatakAzEgysegSebessegenBelulVannak_benne_van_1() {
        try {
            BemenetKezelo.valaszottKoordinatakAzEgysegSebessegenBelulVannak(0, 0, 2, 2,  2);
        } catch (JatekKivetel e) {
            fail(e.getMessage());
        }
    }

    @Test
    void test_valaszottKoordinatakAzEgysegSebessegenBelulVannak_nincs_benne_2() {
        try {
            BemenetKezelo.valaszottKoordinatakAzEgysegSebessegenBelulVannak(0, 0, 3, 3,  2);
            fail("A válaszott koordinátákra már nem tud eljutni az egység! [0, 0] -> [3, 3] sebesseg: 2");
        } catch (JatekKivetel ignored) {}
    }

    @Test
    void test_valaszottKoordinatakAzEgysegSebessegenBelulVannak_nincs_benne_3() {
        try {
            BemenetKezelo.valaszottKoordinatakAzEgysegSebessegenBelulVannak(0, 0, -3, -3,  0);
            fail("A válaszott koordinátákra már nem tud eljutni az egység! [0, 0] -> [-3, -3] sebesseg: 2");
        } catch (JatekKivetel ignored) {}
    }


    /**
     * 5. valasztottKoordinatakAzElsoKetOszloponBelulVannak<br>
     * szélsőséges tesztelés OK
     */

    @Test
    void test_valasztottKoordinatakAzElsoKetOszloponBelulVannak_1() {
        try {
            BemenetKezelo.valasztottKoordinatakAzElsoKetOszloponBelulVannak(0, 0);
        } catch (JatekKivetel e) {
            fail(e.getMessage());
        }
    }

    @Test
    void test_valasztottKoordinatakAzElsoKetOszloponBelulVannak_2() {
        try {
            BemenetKezelo.valasztottKoordinatakAzElsoKetOszloponBelulVannak(-1, -20);
            fail("Az első két oszlopon belül van a két koordináta");
        } catch (JatekKivetel ignored) {}
    }

    @Test
    void test_valasztottKoordinatakAzElsoKetOszloponBelulVannak_3() {
        try {
            BemenetKezelo.valasztottKoordinatakAzElsoKetOszloponBelulVannak(3, 3);
            fail("Az első két oszlopon belül van a két koordináta");
        } catch (JatekKivetel ignored) {}
    }


    /**
     * 6. valaszottKoordinatakACsataterenBelulVannak<br>
     * szélsőséges tesztelés OK
     */

    @Test
    void test_valaszottKoordinatakACsataterenBelulVannak_1() {
        try {
            BemenetKezelo.valaszottKoordinatakACsataterenBelulVannak(-1, -1);
            fail("Nem kéne a csatatéren lenniük..");
        } catch (JatekKivetel ignored) {}
    }

    @Test
    void test_valaszottKoordinatakACsataterenBelulVannak_2() {
        try {
            BemenetKezelo.valaszottKoordinatakACsataterenBelulVannak(10, 12);
            fail("Nem kéne a csatatéren lenniük..");
        } catch (JatekKivetel ignored) {}
    }

    @Test
    void test_valaszottKoordinatakACsataterenBelulVannak_3() {
        try {
            BemenetKezelo.valaszottKoordinatakACsataterenBelulVannak(9, 11);
        } catch (JatekKivetel e) {
            fail("A csatatéren kellene lenniük.");
        }
    }


    /**
     * 7. sajatomatTamadomE
     */

    @Test
    void test_sajatomatTamadomE_1() {
        try {
            Egyseg egyseg = Egyseg.createEgyseg("Sárkány");
            egyseg.setTulajdonos("Teszt");
            hos.sajatomatTamadomE(egyseg);
        } catch (JatekKivetel ignored) {}
    }

    @Test
    void test_sajatomatTamadomE_2() {
        try {
            Egyseg egyseg = Egyseg.createEgyseg("Sárkány");
            egyseg.setTulajdonos("valaki");
            hos.sajatomatTamadomE(egyseg);
        } catch (JatekKivetel e) {
            fail("Az ellenfélét nem tudod támadni.");
        }
    }

    @Test
    void test_sajatomatTamadomE_3() {
        try {
            Egyseg egyseg = Egyseg.createEgyseg("null");
            hos.sajatomatTamadomE(egyseg);
        } catch (JatekKivetel e) {
            fail("Null egység senkinek sem a tulajdona!");
        }
    }


    /**
     * 8. ellenfeletVarazslomE
     */

    @Test
    void test_ellenfeletVarazslomE_1() {
        try {
            Egyseg egyseg = Egyseg.createEgyseg("Sárkány");
            egyseg.setTulajdonos("Teszt");
            hos.ellenfeletVarazslomE(egyseg);
        } catch (JatekKivetel e) {
            fail(e.getMessage());
        }
    }

    @Test
    void test_ellenfeletVarazslomE_2() {
        try {
            Egyseg egyseg = Egyseg.createEgyseg("Sárkány");
            egyseg.setTulajdonos("valaki");
            hos.ellenfeletVarazslomE(egyseg);
            fail("Ellenséges egységre nem tudod ezt használni!");
        } catch (JatekKivetel ignored) {}
    }

    @Test
    void test_ellenfeletVarazslomE_3() {
        try {
            Egyseg egyseg = Egyseg.createEgyseg("null");
            hos.ellenfeletVarazslomE(egyseg);
            fail("Null egység senkinek sem a tulajdona!");
        } catch (JatekKivetel ignored) {}
    }


    /**
     * 9. vanEElegManna<br>
     * szélsőséges tesztelés OK
     */

    @Test
    void test_vanEElegManna_1() {
        try {
            hos.setEredetiManna(20);
            hos.vanEElegManna(new Tuzladba());
        } catch (MannaKivetel e) {
            fail(e.getMessage());
        }
    }

    @Test
    void test_vanEElegManna_2() {
        try {
            hos.setEredetiManna(9);
            hos.vanEElegManna(new Tuzladba());
        } catch (MannaKivetel e) {
            fail(e.getMessage());
        }
    }

    @Test
    void test_vanEElegManna_3() {
        try {
            hos.setEredetiManna(0);
            hos.vanEElegManna(new Tuzladba());
            fail("Sajnos erre is van elég manna :(");
        } catch (MannaKivetel ignored) {}
    }


    /**
     * 10. haLetezikAkkorIndexVisszaad
     */

    @Test
    void test_haLetezikAkkorIndexVisszaad_1() {
        hos.getEgysegek().add(new Sarkany());
        hos.getEgysegek().add(new Ijasz());
        int index = hos.haLetezikAkkorIndexVisszaad(new Demon());

        assertEquals(-1, index, "Nem kellene, hogy benne legyen.");
    }

    @Test
    void test_haLetezikAkkorIndexVisszaad_2() {
        hos.getEgysegek().add(new Sarkany());
        hos.getEgysegek().add(new Ijasz());
        int index = hos.haLetezikAkkorIndexVisszaad(new Sarkany());

        assertNotEquals(-1, index, "Benne kell, hogy legyen!" + index);
    }

    @Test
    void test_haLetezikAkkorIndexVisszaad_3() {
        hos.getEgysegek().add(new Sarkany());
        hos.getEgysegek().add(new Ijasz());

        hos.getEgysegek().remove(1);
        int index = hos.haLetezikAkkorIndexVisszaad(new Ijasz());

        assertEquals(-1, index, "Nem kellene, hogy benne legyen.");
    }



    @AfterAll
    public static void tearDown() {
        System.out.println(GREEN_ITALIC + "Tesztelés befejezve!" + RESET);
    }
}