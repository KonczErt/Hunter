# Hunter

A megvalósításnak felhasználóbarátnak és könnyen kezelhetőnek kell lennie. Törekedni kell
az objektumorientált megoldásra, de nem kötelező a többrétegű architektúra alkalmazása

A megjelenítéshez lehet vezérlőket használni, vagy elemi grafikát. Egyes feladatoknál
különböző méretű játéktábla létrehozását kell megvalósítani, ekkor ügyelni kell arra, hogy az
ablakméret mindig alkalmazkodjon a játéktábla méretéhez.

Készítsünk programot, amellyel a következő két személyes játékot lehet játszani. Adott egy
n × n mezőből álló tábla, ahol egy menekülő és egy támadó játékos helyezkedik el.
Kezdetben a menekülő játékos figurája középen van, míg a támadó figurái a négy sarokban
helyezkednek el. A játékosok felváltva lépnek. A figurák vízszintesen, illetve függőlegesen
mozoghatnak 1-1 mezőt, de egymásra nem léphetnek. A támadó játékos célja, hogy adott
lépésszámon (4n) belül bekerítse a menekülő figurát, azaz a menekülő ne tudjon lépni.
A program biztosítson lehetőséget új játék kezdésére a táblaméret (3×3, 5×5, 7×7) és így a
lépésszám (12, 20, 28) megadásával, folyamatosan jelenítse meg a lépések számát, és ismerje
fel, ha vége a játéknak. Ekkor jelenítse meg, melyik játékos győzött, majd kezdjen
automatikusan új játékot
