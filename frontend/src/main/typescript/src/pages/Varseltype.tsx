import * as React from 'react';
import {Heading, Label, Switch, TextField, Textarea, Button, Panel} from "@navikt/ds-react";
import VarselInfo from "../VarselInfo";
import {useEffect, useState} from "react";

interface VarselTypeProps {
    loggedOut: boolean,
    varselinfos: VarselInfo[],
    currentVarselTypeId: string
}

function mapPrefertKanal(smsPreferertKanal: boolean, epostPreferertKanal: boolean, navNoPreferertKanal: boolean): string[] {
    const kanaler: string[] = [];
    if (smsPreferertKanal) kanaler.push('SMS');
    if (epostPreferertKanal) kanaler.push('EPOST');
    if (navNoPreferertKanal) kanaler.push('DITT_NAV');
    return kanaler;
}

const Varseltype: React.FC<VarselTypeProps> = ({loggedOut, varselinfos, currentVarselTypeId}) => {

    const [unsavedChanges, setUnsavedChanges] = useState<boolean>(false);
    const [varselNavn, setVarselNavn] = useState<string>("");
    const [smsTekst, setSmsTekst] = useState<string>("");
    const [smsPreferertKanal, setSmsPreferertKanal] = useState<boolean>(false);
    const [epostEmne, setEpostEmne] = useState<string>("");
    const [epostTekst, setEpostTekst] = useState<string>("");
    const [epostPreferertKanal, setEpostPreferertKanal] = useState<boolean>(false);
    const [navNoTekst, setNavNoTekst] = useState<string>("");
    const [navNoUrl, setNavNoUrl] = useState<string>("");
    const [navNoPreferertKanal, setNavNoPreferertKanal] = useState<boolean>(false);

    const resetFormToCurrentSelectedVarsel = () => {
        console.log('Reset form to current selected varsel');

        const currentVarsel = varselinfos.find((varselinfo) => varselinfo.varseltypeId?.toLowerCase() === currentVarselTypeId.toLowerCase());
        const currentVarselSms = currentVarsel?.varselmals?.find((mal) => mal.kanal === 'SMS');
        const currentVarselEpost = currentVarsel?.varselmals?.find((mal) => mal.kanal === 'EPOST');
        const currentVarselNavNo = currentVarsel?.varselmals?.find((mal) => mal.kanal === 'DITT_NAV');

        setNavNoPreferertKanal(currentVarsel?.preferertKanal?.find(item => item === 'DITT_NAV') !== undefined)
        setEpostPreferertKanal(currentVarsel?.preferertKanal?.find(item => item === 'EPOST') !== undefined)
        setSmsPreferertKanal(currentVarsel?.preferertKanal?.find(item => item === 'SMS') !== undefined)
        setVarselNavn(currentVarsel?.varselNavn || "")
        setSmsTekst(currentVarselSms?.foerstegangsvarselTekst || "");
        setEpostEmne(currentVarselEpost?.varselTittel || "");
        setEpostTekst(currentVarselEpost?.foerstegangsvarselTekst || "");
        setNavNoTekst(currentVarselNavNo?.foerstegangsvarselTekst || "");
        setNavNoUrl(currentVarsel?.varselURL || "");
        setUnsavedChanges(false);
    };

    const saveForm = () => {
        const currentVarsel = varselinfos.find((varselinfo) => varselinfo.varseltypeId?.toLowerCase() === currentVarselTypeId.toLowerCase());
        const save: VarselInfo = (
                {
                    varseltypeId: currentVarselTypeId,
                    varselNavn: varselNavn,
                    varselKategori: currentVarsel.varselKategori,
                    varselForDistribusjonKanal: currentVarsel.varselForDistribusjonKanal,
                    inaktiv: currentVarsel.inaktiv,
                    revarslingIntervall: currentVarsel.revarslingIntervall,
                    antallRevarslinger: currentVarsel.antallRevarslinger,
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
                    ]
                }
        );
        console.log(save);
    }

    useEffect(resetFormToCurrentSelectedVarsel, [currentVarselTypeId, varselinfos]);

    const count = smsTekst.length;
    return (<div className="">
                <TextField disabled={loggedOut} defaultValue={currentVarselTypeId} label={<Label>VarseltypeId</Label>}/>
                <TextField disabled={loggedOut} defaultValue={varselNavn} label={<Label>Varselnavn</Label>}/>
                <Heading size={'small'}>Varselkanal</Heading>
                <Panel border>
                    <Switch disabled={loggedOut} checked={smsPreferertKanal}
                            onClick={() => setSmsPreferertKanal(!smsPreferertKanal)}>
                        <Label>SMS som preferert kanal</Label>
                    </Switch>
                    <Textarea value={smsTekst} onChange={event => {
                        setUnsavedChanges(true);
                        setSmsTekst(event.target.value);
                    }} label={<Label>SMS</Label>}></Textarea>
                    <span>Antall tegn: {count}</span>
                </Panel>
                <Panel border>
                    <Switch disabled={loggedOut} checked={epostPreferertKanal}
                            onClick={() => setEpostPreferertKanal(!epostPreferertKanal)}>
                        <Label>Epost som preferert kanal</Label>
                    </Switch>
                    <TextField value={epostEmne} onChange={event => {
                        setUnsavedChanges(true);
                        setEpostEmne(event.target.value);
                    }} label={<Label>Emne</Label>}></TextField>
                    <Textarea value={epostTekst} onChange={event => {
                        setUnsavedChanges(true);
                        setEpostTekst(event.target.value);
                    }} label={<Label>Epost</Label>}></Textarea>
                </Panel>
                <Panel border>
                    <Switch disabled={loggedOut} checked={navNoPreferertKanal}
                            onClick={() => setNavNoPreferertKanal(!navNoPreferertKanal)}>
                        <Label>DittNav som preferert kanal</Label>
                    </Switch>
                    <TextField value={navNoUrl} onChange={event => {
                        setUnsavedChanges(true);
                        setNavNoUrl(event.target.value);
                    }} label={<Label>URL</Label>}></TextField>
                    <Textarea value={navNoTekst} onChange={event => {
                        setUnsavedChanges(true);
                        setNavNoTekst(event.target.value);
                    }} label={<Label>Nav.no</Label>}></Textarea>
                </Panel>
                {loggedOut ? '' :
                        (<>
                            <Button disabled={!unsavedChanges} variant={'secondary'}
                                    onClick={resetFormToCurrentSelectedVarsel}>Avbryt</Button>
                            <Button disabled={!unsavedChanges} onClick={saveForm}>Opprett</Button>
                        </>)
                }
            </div>
    )
};

export default Varseltype;
