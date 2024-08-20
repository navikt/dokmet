# Dokmet
Dokmet tilbyr fylgjande Rest-tenester
* tkat020 - les-operasjonar for metadata om dokument produsert av metaforce.
* tkat021 - operasjonar for oversikt og oppdatering av varselmalar.
* tkat020 (basic auth) - operasjonar for henting, oppdatering og oppretting av dokumenttypeinfo.
* tkat030 - validering av ei XML-fil opp mot ei dokumenttype sitt XSD-skjema.

Rest-endepunktet med basic auth skal <ins>kun</ins> brukast av aura-maven-plugin og løpet
for deploy av brevpakkar.

Dokmet tilbyr også eit Varseladmin-GUI for å administrere varselmalar, i tillegg til ei pakke med API-klasser som blir 
publisert til Github Packages.

Meir informasjon om korleis appen fungerer finn du på [Confluence-sida for dokmet (dokumentkatalog)](https://confluence.adeo.no/display/BOA/Dokumentkatalog+-+Tjenester+som+tilbys).

## Førespurnadar
Spørsmål om koda eller prosjektet kan stillast på [Slack-kanalen for \#Team  Dokumentløsninger](https://nav-it.slack.com/archives/C6W9E5GPJ)