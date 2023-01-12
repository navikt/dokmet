import VarselInfo from "./VarselInfo";

function mapPrefertKanal(smsPreferertKanal: boolean, epostPreferertKanal: boolean, navNoPreferertKanal: boolean): string[] {
    const kanaler: string[] = [];
    if (smsPreferertKanal) kanaler.push('SMS');
    if (epostPreferertKanal) kanaler.push('EPOST');
    if (navNoPreferertKanal) kanaler.push('DITT_NAV');
    return kanaler;
}

const createNewVarselInfo = (varselTypeId: string, varselNavn: string,
                             smsPreferertKanal: boolean, smsTekst: string,
                             epostPreferertKanal: boolean, epostEmne: string, epostTekst: string,
                             navNoPreferertKanal: boolean, navNoUrl: string, navNoTekst: string): VarselInfo => {

    return ({
        varseltypeId: varselTypeId,
        varselNavn: varselNavn,
        varselKategori: 'SERVICEMELDING',
        varselForDistribusjonKanal: null,
        inaktiv: true,
        revarslingIntervall: null,
        antallRevarslinger: null,
        varselURL: navNoUrl,
        preferertKanal: mapPrefertKanal(smsPreferertKanal, epostPreferertKanal, navNoPreferertKanal),
        varselmals: [
            {
                kanal: 'EPOST',
                varselTittel: epostEmne,
                foerstegangsvarselTekst: epostTekst,
                revarslingTekst: null
            },
            {
                kanal: 'SMS',
                varselTittel: null,
                foerstegangsvarselTekst: smsTekst,
                revarslingTekst: null
            },
            {
                kanal: 'DITT_NAV',
                varselTittel: null,
                foerstegangsvarselTekst: navNoTekst,
                revarslingTekst: null
            }
        ].filter(varselmal => !!varselmal.foerstegangsvarselTekst)
    })

}

const updateExistingVarselInfo = (existingVarsel: VarselInfo, currentVarselTypeId: string, varselNavn: string, deaktivert: boolean,
                                  smsPreferertKanal: boolean, smsTekst: string,
                                  epostPreferertKanal: boolean, epostEmne: string, epostTekst: string,
                                  navNoPreferertKanal: boolean, navNoUrl: string, navNoTekst: string): VarselInfo => {
    const updatedVarselInfo: VarselInfo = (
        {
            varseltypeId: currentVarselTypeId,
            varselNavn: varselNavn,
            varselKategori: existingVarsel.varselKategori,
            varselForDistribusjonKanal: existingVarsel.varselForDistribusjonKanal,
            inaktiv: deaktivert,
            revarslingIntervall: existingVarsel.revarslingIntervall,
            antallRevarslinger: existingVarsel.antallRevarslinger,
            varselURL: navNoUrl,
            preferertKanal: mapPrefertKanal(smsPreferertKanal, epostPreferertKanal, navNoPreferertKanal),
            varselmals: []
        }
    );
    updatedVarselInfo.varselmals.push({
        kanal: 'EPOST',
        varselTittel: epostEmne || "n/a",
        foerstegangsvarselTekst: epostTekst || "n/a",
        revarslingTekst: null
    });
    updatedVarselInfo.varselmals.push({
        kanal: 'SMS',
        varselTittel: null,
        foerstegangsvarselTekst: smsTekst || "n/a",
        revarslingTekst: null
    });
    updatedVarselInfo.varselmals.push({
        kanal: 'DITT_NAV',
        varselTittel: null,
        foerstegangsvarselTekst: navNoTekst || "n/a",
        revarslingTekst: null
    });
    return updatedVarselInfo;
};

export {createNewVarselInfo, updateExistingVarselInfo};