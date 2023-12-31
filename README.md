# Word Wonders
![readme_logo](https://github.com/Jxkume/LanguageApp/assets/104062080/e17200f4-be1d-4c37-bb88-7bed8f440b72)

## Visio

Kaavailemamme projekti on englannin kielen oppimiseen tarkoitettu mobiilisovellus, joka on kohdennettu 6–8-vuotiaille lapsille. Sovellus toimii erinomaisena apuvälineenä koulunkäynnissä, kun lapsi aloittaa ensimmäisen vieraan kielen opiskelun, tässä tapauksessa englannin kielen. Sovelluksesta hyötyvät lisäksi ihmiset, joille englannin kielen opiskelu on aina tuntunut haastavalta.

Nykytilanteessa lapsille suunnattuja mobiilisovelluksia on markkinoilla hyvin vähän ja uskomme sovelluksella olevan hyvät mahdollisuudet saavuttaa laaja asiakaskunta. Sovelluksen kehityksessä huomioidaan käyttäjien ikäryhmä pitämällä keskeiset ominaisuudet selkeinä ja helppokäyttöisinä. Värikäs ja kuvituksellinen käyttöliittymä pitävät kielen oppimisen lapselle mielekkäänä, ja motivoi sovelluksen säännölliseen käyttöön. Käyttäjän edistymisen seuranta innostaa käyttämään aktiivisesti sovellusta sen pelillistämisen kautta esimerkiksi kokemuspisteillä, tasoilla ja saavutuksilla.

Mobiilisovellus eroaa muista kielisovelluksista siten, että se on suunnattu nimenomaan lapsille ja on käytettävyydeltään selkeämpi. Lisäksi sovellus on täysin ilmainen eikä sisällä minkäänlaisia mikromaksuja. Asiakaskunta tulee hyvin todennäköisesti olemaan lapsipainotteinen, joka huomioidaan erityisesti tietoturvan näkökulmasta; käyttäjältä ei kysytä lupaa hyödyntää käyttäjän puhelimen kuvia tai muita arkaluontoisia tietoja. Kaiken kaikkiaan sovellus on turvallinen ja ensiluokkainen apuväline englannin kielen opiskeluun.

## Kehitysympäristö

Projektin kehitysympäristönä toimi Android Studio. Käyttöliittymän suunnittelu toteutettiin suunnittelutyökalu Figmassa. Projektin koodi kirjoitettiin Java-kielellä. Alla on lista kaikista projektissa käytetyistä kirjastoista ja ohjelmistokehyksistä:
- AndroidX
- Espresso Core
- Firebase Authentication
- Firebase Realtime Database
- JUnit
- Mockito Core

Projektissa käytettiin uusimpia versioita kaikista kirjastoista ja ohjelmistokehyksistä.
Seuraavasta osiosta löytyy ohjeet projektin asentamiseen ja konfigurointiin paikalliseen kehitysympäristöön sekä tarvittavien riippuvuuksien asentamiseen.

## Asennus ja konfigurointi

### Ennen aloittamista

Varmista, että seuraavat asiat ovat asennettuna ja käytettävissäsi ennen projektin asentamista:
-	[Android Studio](https://developer.android.com/studio) – Käytämme tätä kehitysympäristönä Android-sovelluksen rakentamiseen.
-	Java Development Kit (JDK) – Asenna JDK, jos sitä ei ole jo asennettu.
-	[Jenkins](https://www.jenkins.io) – Käytämme Jenkinsiä automatisoituihin testeihin.
-	[Firebase](https://firebase.google.com) – Käytämme Firebasea tietokantana. Luo Firebase-projekti ja hanki tarvittavat asetukset ja avaimet.

### Asennusohjeet

1.	Kloonaa tämä projekti omalle tietokoneellesi terminaalissa seuraavalla komennolla:
```
git clone https://github.com/Jxkume/LanguageApp.git
```
2.	Avaa projekti Android Studiossa.
3.	Määritä projektin riippuvuudet:
   
    - Lisää Firebase-projektin asetustiedostot (`google-services.json`) projektiin.
    - Tarvittaessa päivitä `build.gradle` -tiedostot määrittämään tarvittavat riippuvuudet.
4.	Konfiguroi Jenkins-testaus:
   
    - Asenna Jenkins ja konfiguroi se tarvittaessa.
    - Luo Jenkins-projekti ja määritä testaustyönkulku.
    - Käytä tarvittaessa tämän projektin Jenkinsfilea automatisoidaksesi testaustyönkulku.
5.	Käynnistä projekti Android Studiossa ja varmista, että se toimii ongelmitta paikallisesti.
   
### Lisätietoa

Tässä vaiheessa projekti tulisi olla asennettuna ja konfiguroituna paikallisesti. Voit nyt alkaa kehittää ja testata sovellusta Android Studiossa ja käyttää Jenkinsiä automatisoitujen testien suorittamiseen.
Lisätietoa projektiin liittyvistä toimista, kuten konfiguraatioista, ohjeista ja muista ressurseista, löytyy projektin dokumentaatiosta tai [linkki tietokannan hallintakonsoleihin](https://firebase.google.com/docs/database) Firebase-tietokantaa varten.

## Tiimi
Vera Finogenova  
Valtteri Kuitula  
Jhon Rastrojo López  
Korpi Tolonen
