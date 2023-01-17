interface VarselInfo {
    varseltypeId: string,
    varselNavn: string,
    varselKategori: string,
    varselForDistribusjonKanal: string,
    inaktiv: boolean,
    revarslingIntervall: Number,
    antallRevarslinger: Number,
    varselURL: string,
    preferertKanal: string[],
    varselmals: VarselMal[]
}

interface VarselMal {
    kanal: string,
    varselTittel: string,
    foerstegangsvarselTekst: string,
    revarslingTekst: string
}

export default VarselInfo;
export {VarselMal};