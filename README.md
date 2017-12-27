# Eerste Top 2000
In opdracht van de Universiteit van Amsterdam is deze app gemaakt voor het laatste deel van het AppStudio vak. Met deze app kan de 
gebruiker de eerste 100 nummers van de originele Top 2000 doorzoeken, waarbij elk nummer wordt de positie, titel, artiest en jaar van 
publicatie getoond. Met behulp van Firebase kan de gebruiker inloggen en zelf platen toevoegen aan persoonlijke favorieten. Deze favorieten
kunnen door andere gebruikers opgezocht worden. Voor het doorzoeken van de afspeellijst en de gebruikers is geen login vereist.

## Better Code Hub
[![BCH compliance](https://bettercodehub.com/edge/badge/baszwanenburg/AppStudio?branch=master)](https://bettercodehub.com/)

## Programma van Eisen
* De applicatie moet Firebase gebruiken om één of meerdere type data op te slaan.
* De app kan verschillende items en bijbehorende details weergeven, welke door de gebruiker kan worden toegevoegd aan zijn/haar favorieten.
* Favorieten kunnen tussen gebruikers onderling gedeeld worden. Hiervoor is geen login nodig.
* Als de app gekilled wordt, dient het zich in dezelfde state weer te hervatten.Even if killed, the app should generally resume in the same state as before.
* De code dient goed georganiseerd en gedocumenteerd te zijn.

## Peer review
* Er mist een button om uit te loggen
* Res files die niet (langer) gebruikt worden, zijn niet verwijderd
* Sommige stukjes code missen relevante informatie
* Overbodige code is niet weggehaald, comments van Android Studio worden genegeerd
* ID tags zijn af en toe niet logisch of onduidelijk
* Dubbele layout inflate in de MyDatabase activity: buttons moeten worden aangeroepen in de adapter
* In de fragmenten staan functies onnodig veel opgedeeld in kleinere 'deelfuncties'
* Vaak worden tussen functies te veel of te weinig witte regels toegepast
* Gebruik van tabs en plaatsing brackets niet consistent
* Classes hebben extra variabelen die niet gebruikt worden
