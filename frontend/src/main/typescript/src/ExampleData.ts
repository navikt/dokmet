import raw_json_respose from './raw_response.json';

const userDataExample = {
    "displayName" : "Zorro Friedrich Burkersen",
    "username" : "Z123591",
    "dn" : "CN=Z123591,OU=Users,OU=NAV,OU=BusinessUnits,DC=adeo,DC=no",
    "roles" : [ "ROLE_VARSELADMIN", "ROLE_PERSON" ]
};

const varselinfoExamples = [
    {
        varseltypeId: 'pensjo1',
        varselNavn: 'Førstegangsvarsel Pensjon',
        varselKategori: 'SERVICEMELDING',
        varselForDistribusjonKanal: "ja",
        inaktiv: false,
        revarslingIntervall: 1,
        antallRevarslinger: 1,
        varselURL: 'URL',
        preferertKanal: ['SMS', 'EPOST', 'DITT_NAV'],
        varselmals: [
            {
                kanal: 'SMS',
                varselTittel: 'SMS-varsel',
                foerstegangsvarselTekst: "Du har fått en ny melding om Pensjon på SMSMSMS",
                revarslingTekst: ""
            },
            {
                kanal: 'EPOST',
                varselTittel: 'Epost-varsel til deg',
                foerstegangsvarselTekst: "Du har fått en ny melding om Pensjon på EPOST",
                revarslingTekst: ""
            },
            {
                kanal: 'DITT_NAV',
                varselTittel: 'MAVMP- -varsel',
                foerstegangsvarselTekst: "Du har fått en ny melding om Pensjon på DITT_NAV",
                revarslingTekst: ""
            }
        ]
    },
    {
        varseltypeId: 'pensjo2',
        varselNavn: 'Andregangsvarsel Pensjon',
        varselKategori: 'SERVICEMELDING',
        varselForDistribusjonKanal: "ja",
        inaktiv: false,
        revarslingIntervall: 1,
        antallRevarslinger: 1,
        varselURL: 'URL',
        preferertKanal: ['SMS', 'EPOST', 'DITT_NAV'],
        varselmals: [
            {
                kanal: 'SMS',
                varselTittel: 'SMS-varsel',
                foerstegangsvarselTekst: "Du har tidligere fått en gammel melding om Pensjon på SMSMSMS",
                revarslingTekst: ""
            },
            {
                kanal: 'EPOST',
                varselTittel: 'Epost-varsel til deg',
                foerstegangsvarselTekst: "Du har tidligere fått en gammel melding om Pensjon på EPOST",
                revarslingTekst: ""
            },
            {
                kanal: 'DITT_NAV',
                varselTittel: 'DITT_NAV - varsel',
                foerstegangsvarselTekst: "Du har tidligere fått en gammel melding om Pensjon på nav.no, på tide å lese den!",
                revarslingTekst: ""
            }
        ]
    }
];

const createVarselinfoPromise = () => new Promise(resolve => setTimeout(resolve, 1000)).then(() => varselinfoExamples)
const createRealisticVarselinfoPromise = () => new Promise(resolve => setTimeout(resolve, 1000)).then(() => raw_json_respose)
const simulateLoginPromise = () => new Promise(resolve => setTimeout(resolve, 1000)).then(() => userDataExample)

export {varselinfoExamples, createVarselinfoPromise, raw_json_respose, createRealisticVarselinfoPromise, userDataExample, simulateLoginPromise};