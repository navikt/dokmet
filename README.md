# Dokmet
Dokmet tilbyr fylgjande Rest-tenester
* tkat020 - les-operasjonar for metadata om dokument produsert av metaforce.
* tkat021 - operasjonar for oversikt og oppdatering av varselmalar.
* tkat020 (basic auth) - operasjonar for henting, oppdatering og oppretting av dokumenttypeinfo.
* tkat030 - validering av ei XML-fil opp mot ei dokumenttype sitt XSD-skjema.

Rest-endepunkta i tkat020 (basic auth) skal <ins>kun</ins> brukast av aura-maven-plugin og løpet
for deploy av brevpakkar.

Dokmet tilbyr også eit Varseladmin-GUI for å administrere varselmalar, i tillegg til ei pakke med API-klasser som blir 
publisert til Github Packages.

Varseladmin-GUIet kan opnast i vanleg nettlesar dersom ein er kopla til Naisdevice, og er tilgjengeleg i alle dev-miljø (også q0, q4 og q5):
* [Varseladmin i q1](https://dokmet-q1.dev.intern.nav.no/dokmet/varseladmin)
* [Varseladmin i q2](https://dokmet.dev.intern.nav.no/dokmet/varseladmin)

Meir informasjon om korleis appen fungerer finn du på [Confluence-sida for dokmet](https://confluence.adeo.no/display/BOA/Dokmet+-+Tjenester).

## Førespurnadar
Spørsmål om koda eller prosjektet kan stillast på [Slack-kanalen for \#Team  Dokumentløsninger](https://nav-it.slack.com/archives/C6W9E5GPJ)