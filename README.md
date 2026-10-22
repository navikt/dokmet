# Dokmet

Dokmet tilbyr fylgjande Rest-tenester
* tkat020 - les-operasjonar for metadata om dokument produsert av metaforce.
* tkat021 - operasjon for henting av varselmal.
* tkat020 (basic auth) - operasjonar for henting, oppdatering og oppretting av dokumenttypeinfo.
* tkat030 - validering av ei XML-fil opp mot ei dokumenttype sitt XSD-skjema.

Rest-endepunkta i tkat020 (basic auth) skal <ins>kun</ins> brukast av aura-maven-plugin og løpet
for deploy av brevpakkar.

Dokmet tilbyr også ei pakke med API-klasser som blir publisert til Github Packages.

Meir informasjon om korleis appen fungerer finn du på [Confluence-sida for dokmet (Nav-internt)](https://confluence.adeo.no/display/BOA/Dokmet+-+Tjenester).

## Kome i gong

Køyr testar og bygg appen.

```
mvn clean verify
```

---

## Førespurnadar

Lag ein issue i repository.

### For Nav-ansatte

Spørsmål om appen kan stillast på [#team_dokumentløsninger](https://nav-it.slack.com/archives/C6W9E5GPJ)

## Lisens

[MIT](LICENSE.md)
