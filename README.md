# IDDE laborfeladatok

Adatbázis használatakor a táblák létrejönnek. Egy ``tour`` nevű adatbázis szükséges hozzá.

# paim1949-desktop futtatása:
+ Navigáljunk a projekt könyvtárába.
+ Futtatni a ``gradle run`` paranccsal tudunk
+ A parancs alapértelmezetten ``dev`` profilt használ
+ A ``-Pprofile`` kapcsolóval megadhatjuk a profilt:
  + ``dev`` profil: ``gradle -Pprofile=dev run``
  + ``prod`` profil: ``gradle -Pprofile=prod run``
# paim1949-web kitelepítése és futtatása:
+ Navigáljunk a projekt könyvtárába.
+ Kitelepíteni az applikációt a ``gradle deploy`` paranccsal tudjuk.
+ Alapértelmezetten ``dev`` profillal telepíti ki.
+ A ``-Pprofile`` kapcsolóval megadhatjuk a profilt:
    + ``dev`` profil: ``gradle -Pprofile=dev deploy``
    + ``prod`` profil: ``gradle -Pprofile=prod deploy``
+ Futtatáshoz a ``catalina run`` vagy ``catalina start`` parancs szükséges
+ Futtatáskor szintén alapértelmezett a ``dev`` profil
+ A profilt be tudjuk állítani a ``CATALINA_HOME/conf/catalina.properties`` állományban egy sor hozzáfűzésével:
  + ``dev`` profil: ``profile=dev``
  + ``prod`` profil: ``profile=prod``