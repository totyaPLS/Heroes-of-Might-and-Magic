# Feladatleírás
## A játékról
A feladat megvalósítani a Heroes of Might & Magic játékban szereplő harcrendszer egy leegyszerűsített változatát. A harcban két fél vesz részt, mindegyik rendelkezik egy hőssel, illetve
néhány egységgel. A harc egy téglalap alakú pályán játszódik. A játék során az egységek, illetve
a hősök támadhatnak. A cél, hogy az ellenfél összes egységét legyőzzük. Az egyik csapatot a
játékos, míg a másik csapatot a számítógép fogja irányítani.

## Előkészítési szakasz
A játékos a játék elején kiválasztja a nehézségi szintet (könnyű, közepes, nehéz).<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A csata előtt a játékosok kialakítják a hősük tulajdonságait, illetve összerakják a hadsereget.
Mind a hős fejlesztése, mind az egységek aranyba kerülnek. A játékosnak nehézségi szinttől
függően 1300/1000/700 arany áll rendelkezésre. Az ellenfél minden esetben 1000 arannyal
rendelkezik.

## Hősök
Minden hősnek 6 tulajdonsága van: támadás, védekezés, varázserő, tudás, morál és szerencse.
Ezen tulajdonságok az egységek tulajdonságát, illetve magát a hőst befolyásolják az alábbi
módon:
- Támadás: az egységek sebzését növeli meg, tulajdonságpontonként 10%-kal.
- Védekezés: az egységeket ért sebzést csökkenti, tulajdonságpontonként 5%-kal.
- Varázserő: a hős által idézett varázslatok erősségét növeli.
- Tudás: a hős maximális mannapontjait növeli, tulajdonságpontonként 10-zel.
- Morál: az egységek kezdeményezését növeli, tulajdonságpontonként 1-gyel.
- Szerencse: az egységek kritikus támadásának esélyét növeli, tulajdonságpontonként 5%-
  kal.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Alapértelmezetten a hős minden tulajdonságára 1-1 tudáspont van elosztva (ez ingyenes),
onnantól kezdve a játékos tetszőlegesen növelheti őket, de egy adott tulajdonság maximális
értéke 10 lehet.<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A legelső tulajdonságpont elköltése 5 aranyba kerül. Onnantól kezdve minden további tulajdons
ágpont ára 10%-kal magasabb lesz. Az árak mindig egész értékek lesznek, és minden
tulajdonságpont esetében felfelé kerekítünk. (tehát 5, 6, 7, 8, 9, 10, 11, 13. . . )

## Varázslatok
A hős tud varázsolni, ám ehhez varázslatokra van szüksége, melyeket szintén a rendelkezésre
álló aranyból tudunk vásárolni. A hős tetszőlegesen sok varázslattal rendelkezhet. Szükséges
legalább 5 különböző jellegű varázslat megvalósítása. Minden varázslatnak van egy ára, egy
mannaköltsége, illetve nyilván valamilyen hatása az egységekre. Egy varázslatot a csata során
tetszőlegesen sokszor használhatunk, amennyiben a hős rendelkezik elegendő mannaponttal.

Varázslatok listája:

| Név          | Ár  | Manna | Varázslat leírása                                                                                                                             |
|--------------|-----|-------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| Villámcsapás | 60  | 5     | Egy kiválasztott ellenséges egységre<br>(varázserő * 30) sebzés okozása                                                                       |
| Tűzlabda     | 120 | 9     | Egy kiválasztott mező körüli 3x3-as területen lévő<br>összes (saját, illetve ellenséges) egységre<br>(varázserő * 20) sebzés okozása          |
| Feltámasztás | 120 | 6     | Egy kiválasztott saját egység feltámasztása.<br>Maximális gyógyítás mértéke: (varázserő * 50)<br>(de az eredeti egységszámnál több nem lehet) |

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A negyedik, ötödik (illetve esetlegesen további) varázslatok megvalósítása saját (játékban
meglévő, vagy nem meglévő) ötlet alapján történik.

## Egységek
A hős tulajdonságpointjainak elosztása, illetve a varázslatok kiválasztása után a játékos összeállítja a seregét. A játéknak tartalmaznia kell legalább 5 fajta egységet. A játékos kiválaszthatja,
hogy melyikből mennyit szeretne magával vinni a harcba. Természetesen az egységeknek is van
áruk. Egy adott típusú egységből tetszőlegesen sokat tud magával vinni (amíg van a játékosnak
rendelkezésre álló aranya), ezeket egyben kell kezelni (tehát 1 területet foglalnak el, és együtt
is támadnak). Összesen legalább 1 darabot vinni kell valamelyik típusból (tehát sereg nélkül
nem mehet a harcba). Nem muszáj minden típusú egységből vinni, akár lehet csak egy fajtát
vinni.

Egységek listája:

| Név       | Ár  | Sebzés | Életerő | Sebesség | Kezdeményezés | Speciális képesség     |
|-----------|-----|--------|---------|----------|---------------|------------------------|
| Földműves | 2   | 1-1    | 3       | 4        | 8             | nincs                  |
| Íjász     | 6   | 2-4    | 7       | 4        | 9             | lövés                  |
| Griff     | 15  | 5-10   | 30      | 7        | 15            | végtelen visszatámadás |

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A negyedik, ötödik (illetve esetlegesen további) egységek megvalósítása saját (játékban megl
évő, vagy nem meglévő) ötlet alapján történik. Mind a kettőnek rendelkeznie kell legalább 1-1
speciális képességgel, amelyek között lennie kell legalább 1 olyannak, ami a fenti táblázatban
nem szerepel. Minden egység 1x1-es méretű.

Speciális képességek magyarázata:
- Lövés: az egység távolsági támadást tud végrehajtani, de csak abban az esetben, ha nincs
ellenséges egység a közvetlen közelében.
- Végtelen visszatámadás: az egység tetszőlegesen sokszor vissza tud támadni

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Az azonos típusú egységeket együtt kezeljük. Ha például a játékos vásárolt 100 íjászt, akkor
azok mindig együtt vannak (ugyanazon a mezőn) és együtt is támadnak. Összesített életerejük
700, sebzésük 200-400. A sebesség és kezdeményezés nem adódik össze, tehát azok változatlanul
4, illetve 9.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Adott típusú egységekből mindig legfeljebb egy lehet sérült, amelyiknek az életereje nem maximális. Tehát ha volt 100 íjászunk, akik kaptak 5 sebzést, akkor az összesített életerejük 695, ami azt jelenti, hogy 99 íjászunk van 7 életerővel és 1 íjászunk van 2 életerővel. Ha ezután
kapnak az íjászok még 14 sebzést, akkor az összesített életerő 684 lesz. Ez azt jelenti, hogy 98
íjászunk marad, ebből 97 íjásznak lesz teljes életereje, míg 1 íjásznak lesz 5 életereje.

## Csata
A létrehozott két hős és seregeik ezután a csatatérre kerülnek. A csatatér bal oldalára kerül a
játékos, míg a jobb oldalára a gép. A csatatér egy négyzetrácsos terület, ami 12x10-es méretű,
azaz 12 széles és 10 magas.

### Taktikai fázis
A csata előtt el kell helyezni az egységeket a pályán. Mind a két félnek a terület felé eső részéből
az első két oszlop áll rendelkezésre elhelyezni az egységeket. Az egységeket tetszőlegesen el lehet
helyezni ezen területen belül, de nyilván nem lehet őket egymásra rakni.<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Miután a játékos és az ellenfél is elhelyezte az egységeket (tetszőleges módon), a csata
elkezdődik.

### Csata
A csata körökre osztott, minden körben 1 egység 1 alkalommal lép. Először lépnek a magasabb
kezdeményezésű egységek, majd pedig az alacsonyabb kezdeményezésű egységek. Az azonos
kezdeményezésű egységek sorrendisége tetszőleges: lehet random, lehet az egyik játékost előnyben
részesíteni, vegyesen, elfoglalt pozíció alapján, stb.<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A hősök tetszőleges időpontban léphetnek, de körönként legfeljebb 1x. Tehát ha az adott
játékos egyik egysége következik, akkor dönthet úgy, hogy ő először a hősét használja, majd
utána lép az adott egységgel.<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A hős nem foglal helyet a csatatéren és nem lehet megölni. A hős kétféle cselekvést tud
végezni: támadás, illetve varázslás. Nyilván a kettő közül egy körben csak az egyiket tudja
elvégezni. Támadás esetén egy kiválasztott ellenséges egységre mér tisztán (támadás tulajdons
ág) * 10 mértékű sebzést (tehát erre nincs hatással az ellenséges hős védekezés tulajdonsága).
Varázslás esetén a játékos kiválasztja a használni kívánt varázslatot, és annak hatásai érvényes
ülnek (nyilván lennie kell elég mannának a varázslathoz). A varázslatok sebzés értékeire sincs
hatással az ellenséges hős védekezés tulajdonsága.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Egy adott egység, amikor rá kerül a sor, akkor három dolgot tud csinálni: mozogni, várakozni,
illetve támadni. Ezek közül egy körben csak az egyiket tudja csinálni.
- Mozgás: az egység pozíciót vált a játékmezőn (tehát "arrébb megy"). Legfeljebb annyi
mezőt tud mozogni, amennyi az egység sebessége. A mozgás iránya tetszőleges, akár
jobbra, balra, fel, le, átlósan, kacskaringósan (pl. ha más egységet kell kikerülni) történhet.
- Várakozás: az egység az adott körből kimarad.
- Támadás: az ellenséges egység megtámadása. Közelharci támadás csak szomszédos egys
ég ellen indítható, míg távolsági támadás csak akkor indítható, ha az egység közvetlen
környezetében nincs ellenséges egység.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A támadás során először kiszámoljuk az egység alap sebzését, ehhez hozzáadjuk a hős támadás tulajdonsága által adott bónuszt, majd levonjuk az ellenséges hős által nyújtott védekezési
bónuszt. Erre még rájön az esetlegesen kritikus sebzésből adódó sebzés. Ennyi sebzést fog
elszenvedni az ellenséges egység. Az alap sebzés kiszámításához generáljunk egy véletlenszerű
értéket az adott egység minimális és maximális sebzése között.<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Példa: vegyük azt az esetet, hogy 13 griffel támadunk meg 42 földművest. A hősünk támadás
tulajdonsága 7, míg az ellenséges hős védekezés tulajdonsága 10.
- A griff sebzése 5-10, tegyük fel, hogy ezen intervallumon belül egy 7-est generáltunk.
- Tehát az alap sebzés 13*7 (mivel 13 griffünk van), azaz 91.
- A hősünk támadás tulajdonsága 7, ami 70%-os sebzés bónuszt jelent, tehát 91 * 1.7 = 4.7.
- Az ellenséges hős védekezése 10, tehát 50%-os védekezést nyújt neki: 154.7 * 0.5 = 77.35.
- Ezt kerekítve megkapjuk, hogy a sebzés 77 lesz.
- A földműves életereje 3, azaz a 42 földművesből 25 meghal + az egyik még két sebzést
szenved el. Tehát marad 17 földműves, de ezek közül az egyiknek már csak 1 életereje
marad (ez a következő támadáskor lesz fontos)
- Ezután a maradék 17 földműves visszatámad. Hasonló képlettel számolva (ha hősünk
védekezés tulajdonsága 3, az ellenséges hős támadás tulajdonsága 1) megkapjuk, hogy a
sebzés értéke 16. Azonban mivel visszatámadásról van szó, így a sebzés értéket felezni
kell, tehát eredményként 8-at kapunk. Tehát egy griff sem fog meghalni, de az egyiknek
már 30 helyett csak 22 életereje marad.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Az egységek a hős szerencse tulajdonságától függően kritikus sebzést okozhatnak. Ilyenkor
a végső sebzést kell megkétszerezni.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A megtámadott egység visszatámad az őt megtámadó egységnek. Ilyenkor a végső sebzésnek
a felét sebzi az őt támadó egységre. Visszatámadni egy adott egység egy körön belül csak
egyszer tud (kivéve ha van ilyen speciális képessége). Visszatámadásnál nyilván az életben
maradó egységek fognak csak visszatámadni.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A csatát az a játékos nyeri, aki először legyőzi az ellenfél összes egységét. Ha esetleg egyszerre
hal meg mind a két hős serege (pl. egy tűzlabda miatt), akkor a csata döntetlennel zárul.

## Segítség, linkek
- [Játék leírása (magyar).](http://hommm.hu/archiv/heroes5/leirasok/leirasok.php)
- [Játék leírása (angol).](http://www.heroesofmightandmagic.com/heroes5/heroesofmightandmagic5v.shtml)
- [Játék linkje](https://store.steampowered.com/app/15170/Heroes_of_Might__Magic_V/)
- [Videó](https://www.youtube.com/watch?v=FZ88HjitzhY&ab_channel=Duy%C4%90%E1%BB%A9cNguy%E1%BB%85n)

# Megjegyzések az elkészített játékhoz
- Ez egy konzolos verzió
- A játékot az src mappában lévő Main osztály futtatásával kell elindítani IntelliJ IDEA, Java 17-es verzióban.
- Ha a test mappa nem lenne zöld, akkor jobb klikk -> Mark directory as.. -> Test Sources Root

## Játék irányítása:
- Leginkább sorszámot kérek be a program az egyszerűbb és gyorsabb használatért. Ez esetben ez általában adat bekérésénél jelölve is van, hogy ezt várja, illetve a választási lehetőségek is számozva vannak.
- Koordináták bekérésénél külön kéri be x és y koordinátát. Mind a kettőhöz egy számot kell megadni a csatatér számozása alapján.
- Van olyan eset is, hogy az adott alany nevét kell beírni ha hatást akarunk elérni. Ez általában egységekkel kapcsolatos szokott lenni (pl. feltámasztásnál).

## Játékban lévő karakterek jelölései és pontos nevei:
- 😈 - Démon
- 🗡 - Földműves
- 🦅 - Griff
- 🏹 - Íjász
- 🐲 - Sárkány
- _ - NullEgyseg

Az ellenfél egységei saját szabály szerint mozognak.