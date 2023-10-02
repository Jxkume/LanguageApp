# Projektisuunnitelma

## Tavoitteiden määritys
Projektinamme on toteuttaa kieltenoppimissovellus, joka on suunnattu 6–8-vuotiaille lapsille. Kyseessä on mobiilisovellus, jossa vieraan kielen opiskelun pääsee aloittamaan ihan alkeista. Projektin yksi tärkeimmistä tavoitteista on luoda mahdollisimman helposti lähestyttävä ja navigoitava sovellus, jonka pelaajan palkitsemisjärjestelmä tuntuisi käyttäjälle mielekkäältä. Sovelluksen kaikki pelit toimivat samalla logiikalla, mutta ovat eri aihealueista. Käyttäjän on tarkoitus kerätä kokemuspisteitä peleistä ja edetä tasoittain. Jokainen peli kerryttää omaa kokemuspistepalkkia, jonka täytyttyä pelaaja voi siirtyä pelin seuraavalle tasolle. Pelien kokemuspisteet kertyvät myös pelaajan profiilin kokemuspisteisiin, joka nostaa pelaajan profiilin tasoa.

Ohjelmistotuotantoprojekti 1 -kurssin aikana toteuttavassa osuudessa tavoitteena on saada sovelluksen tärkeimmät ominaisuudet toteutettua ja huolehtia kattavan dokumentaation toteutuksesta. Sovelluksen tulee sisältää sen tärkeimmät elementit eli pelit ja sessioiden, eli käyttäjien kokemuspisteiden, tallentamisen. 

## Projektin rajat
Projektin tärkeimpänä ominaisuutena ovat pelit, niiden toiminnallisuus ja niistä käyttäjälle kerääntyvät kokemuspisteet. Nämä ovat välttämättömät ja tullaan toteuttamaan sovelluksen toimivuuden varmistamiseksi. 

Koska projekti toteutetaan 8 viikon aikana, johon sisältyy ideoiminen, sovelluksen ulkonäön ja toiminnallisuuksien luonnostelu ja toteuttaminen, testaus, sekä dokumentaatio, on selvää, että ajallinen budjetti on tiukka. Mikäli ominaisuuksia jää toteuttamatta aikarajan sisällä tullaan nämä implementoimaan sovellukseen myöhemmin.

## Vaatimukset
Tuotteen omistaja seuraa projektin etenemistä viikoittain Scrum-tapaamisissa. Tuotteen omistajan kanssa on päätetty, että peli sisältää projektin päättyessä vähintään kaksi toimivaa peliä, joissa on useampi taso, toimiva ja selkeä tietokanta, sekä sovelluksesta tehty kattava dokumentaatio. 

Sovelluksen tulevat käyttäjät otetaan huomioon sovelluksen kehityksessä, tässä tapauksessa 6–8-vuotiaat lapset. Sovellus ja sen tarjoamat pelit pyritään pitämään helppokäyttöisinä ja sovellus yksinkertaisena. Emme myöskään tule sisällyttämään sovelluksen sisäisiä maksuja.

## Toiminnalliset vaatimukset
Mobiilisovellus koostuu useista eri ominaisuuksista, jotka muodostavat selkeän kokonaisuuden. Käyttäjä voi luoda sovellukseen oman tallennuksen ja ainoat tietovaatimukset ovat profiilikuva sovelluksen tarjoamista kuvavaihtoehdoista, nimimerkki sekä käyttäjän ikä. Käyttäjän on myös mahdollista poistaa luomansa käyttäjä jälkikäteen sovelluksen asetuksista.
Sovelluksen perimmäisenä tarkoituksena on toimia opettavaisena kielisovelluksena käyttäjälle sovelluksen pelillistämisen kautta. Sovellus sisältää useita pelejä, joita käyttäjä voi pelata ja samalla oppia englanninkielisiä sanoja. Jokainen peli sisältää edistymispalkin, joka ilmaisee käyttäjän edistymisen kyseisessä pelissä. Lisäksi käyttäjän on mahdollista poistua pelistä kesken pelaamisen.

Käyttäjä ansaitsee sovelluksen peleistä kokemuspisteitä, jotka määräävät käyttäjän henkilökohtaisen tason. Kokemuspisteet näkyvät käyttäjälle edistyspalkin muodossa sovelluksen koti- ja asetusnäkymissä. Lisäksi käyttäjä voi profiilinäkymässä vaihtaa aikaisemmin asettamansa profiilikuvan uuteen.

## Ei-toiminnalliset vaatimukset
Sovelluksen kohderyhmänä ovat 6–8-vuotiaat lapset, joten yksi prioriteeteistamme on sovelluksen helppokäyttöisyys. Tämä pitää sisällään mm. selkeän käyttöliittymän, helposti opittavan pelilogiikan sekä yksinkertaisen käyttäjän luonnin. Sovellus on kokonaisuutena hyvin selkeä ja yksinkertainen, joka mahdollistaa uusien ominaisuuksien lisäämisen jatkokehitysvaiheessa.

Sovelluksen peleissä oikeat ja väärät vastaukset viestitään käyttäjälle selkeiden ponnahdusikkunoiden muodossa. Tämä parantaa käyttäjäkokemusta ja lisää kiinnostusta pelin suhteen. Lisäksi sovelluksen taustamusiikki luo rauhallista tunnelmaa ja vaikuttaa positiivisesti käyttäjän keskittymiskykyyn.

## Resurssien kohdentaminen
Sovellusprojekti toteutetaan 4 hengen ryhmässä, johon kuuluvat opiskelijat Vera Finogenova, Valtteri Kuitula, Jhon Rastrojo ja Korpi Tolonen. Projekti on osa Metropolia-ammattikorkeakoulun tarjoamaa ohjelmistoprojektikurssia, joten projektilla ei virallisesti ole rahallisia resursseja. Kurssi kestää yhteensä 8 viikkoa ja jokaiselta ryhmän jäseneltä odotetaan noin 12 tunnin työpanosta per viikko.

Sovelluksen käyttöliittymän ideointi ja suunnittelu tapahtuu yhteiskäyttöisessä käyttöliittymäsuunnitteluohjelmisto Figmassa. Ohjelmointiympäristönä toimii Android Studio sekä koodin testauksessa hyödynnetään Jenkins-työkalua. Lisäksi sovelluksen tietokanta on Firebase-nimisellä pilvipalvelualustalla. Projektin versionhallinta toteutetaan versionhallintajärjestelmä Gitin sekä GitHub-verkkosivun välityksellä.

## Riskien arviointi
Tekemäämme projektiin liittyvät riskit voivat olla kahta eri tyyppiä. Ensimmäinen tyypeistä on projektin kehitysvaiheen riskit, ja toinen valmiin tuotteen jakeluvaiheen riskit. Kehitysvaiheeseen liittyviä riskejä ja haasteita voivat olla esimerkiksi virheet aikataulun suunnittelussa, jotka vaikeuttavat aikataulussa pysymistä. Tämän seurauksena osa alun perin suunnittelemista ominaisuuksista voi jäädä toteuttamatta. Myös kehitystiimin taitojen riittämättömyys joidenkin tärkeiden ominaisuuksien toteuttamiseen voi olla riittämättömällä tasolla. Lisäksi tiimin sisäiseen ilmapiiriin liittyvät mahdolliset ongelmat voivat vaikuttavat tiimin jäsenten suhtautumiseen toisiinsa ja vaikeuttavat tiimin jäsenten välistä vuorovaikutusta.

Jakeluvaiheen suurimpia riskejä ovat esimerkiksi kohderyhmän kiinnostuksen puute tai kilpailevien tuotteiden ilmestyminen markkinoille. Nämä riskit voidaan kuitenkin tässä projektissa olla ottamatta huomioon.

## Projektiryhmän muodostaminen
Projektimme kehitysryhmässä on neljä henkilöä. Ryhmien muodostamisvaiheessa päätimme olla ryhmä, sillä tavoitteemme ja osaamisemme sopivat yhteen. Jo ennen projektin vision luontia meillä oli ajatuksia siitä, että meistä tulisi hyvä projektiryhmä.

Jokainen ryhmän jäsen on Scrum Masterina ainakin viikon ajan, jotta jokaisella on edes vähän kokemusta tiiminvetäjän roolissa olemisesta tämän projektin jälkeen. Muuten pyrimme pitämään ryhmämme jäseniä mahdollisimman tasavertaisina. Kaikki tärkeät projektia koskevat päätökset teemme aina yhdessä.

## Kielet, kirjastot ja sekvensointi
Ainoa ohjelmointikieli, jota käytämme projektissamme, on Java. Hyödynnämme useita Javan kirjastoja, esimerkiksi Android Studioon sisäänrakennettua Androidx-kirjastoa sekä Googlen Firebase-kirjastoa, joka mahdollistaa Firebase-tietokannan käytön. Käyttöliittymän toteutuksessa käytetään XML-merkintäkieltä.

Projekti sekvensoidaan seuraavanlaisesti: ensin suunnitellaan käyttöliittymä ja toteutetaan se Android Studiossa. Seuraavaksi toteutetaan tietokannan suunnittelu ja rakentaminen. Lopuksi tapahtuu sovelluksen logiikan suunnittelu ja rakentaminen.

## Viestintäsuunnitelma
Projektin viestintäsuunnitelmaan sisältyy viestintästrategia, joka pitää sisällään päivittäisen vuorovaikutuksen Discordissa sekä viikoittaset tapaamiset projektiryhmän ja tuotteen omistajan kesken. Discordissa viestitään esimerkiksi tavoitteiden ja edistymisen tiimoilta, ja viikottaiset tapaamiset puolestaan tukevat keskustelua ja ongelmaratkaisua. Lisäksi projektin suunnittelussa hyödynnetään verkkosivu Trelloa, jossa tapahtuu eri työtehtävien jakaminen.

## Budjetointi ja kustannusarvio
Kuten aikaisemmin mainittu, sovellusprojekti on osa Metropolia-ammattikorkeakoulun tarjoamaa ohjelmointiprojektikurssia. Tästä syystä projektin budjetointi on erittäin hankalaa, sillä sovelluksen kehitykseen ei ole rahallisia resursseja ja sovelluksella ei ole tarkoitus tehdä liiketoiminnallista voittoa. Projektin kustannusarvion arviointi on myös haastavaa. Kustannusarvioon voisi sisällyttää esimerkiksi ryhmän tapaamisten virvokkeet (arvio 10 euroa per viikko).

## Asiakasyhteistyö ja oikeudellisuus
Ryhmän viikoittaisten tapaamisten lisäksi pidämme erillisiä asiakastapaamisia mobiilisovelluksen asiakkaan kanssa kerran viikossa. Näissä tapaamisissa tarkastelemme edellisen viikon saavutuksia ja keskustelemme mitä olemme saaneet aikaan ja mitä emme ole vielä ehtineet tehdä. Lisäksi suunnittelemme seuraavan viikon työtehtäviä, käymme läpi työn alla olevat asiat ja harkitsemme suunnitelmia.

Sovellusprojekti noudattaa tarkasti tekijänoikeuksia ja tietosuojaa sekä Metropolian sääntöjä. Varmistamme myös, että täytämme kurssin opettajan asettamat vaatimukset.
