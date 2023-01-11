import * as React from 'react';
import {useEffect, useState} from 'react';
import {Button, Heading, Label, Panel, Switch, Textarea, TextField} from "@navikt/ds-react";
import VarselInfo from "../domain/VarselInfo";
import {createNewVarselInfo, getSingleVarselInfo, updateVarselInfo} from "../Api";

interface VarselTypeProps {
    editDisabled: boolean,
    varselinfos: VarselInfo[],
    currentVarselTypeId: string,
    setCurrentVarselTypeId: (id: string) => void,
    editingNew: boolean,
    setEditingNew: (b: boolean) => void
}

function mapPrefertKanal(smsPreferertKanal: boolean, epostPreferertKanal: boolean, navNoPreferertKanal: boolean): string[] {
    const kanaler: string[] = [];
    if (smsPreferertKanal) kanaler.push('SMS');
    if (epostPreferertKanal) kanaler.push('EPOST');
    if (navNoPreferertKanal) kanaler.push('DITT_NAV');
    return kanaler;
}

const Varseltype: React.FC<VarselTypeProps> = ({
                                                   editDisabled,
                                                   varselinfos,
                                                   currentVarselTypeId,
                                                   setCurrentVarselTypeId,
                                                   editingNew,
                                                   setEditingNew
                                               }) => {

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
    const [currentVarselType, setCurrentVarselType] = useState<VarselInfo>();
    const [deaktivert, setDeaktivert] = useState<boolean>(false);

    const resetFormToCurrentSelectedVarsel = () => {
        if (editingNew && !currentVarselTypeId) {
            setCurrentVarselType(null);
            setNavNoPreferertKanal(false)
            setEpostPreferertKanal(false)
            setSmsPreferertKanal(false)
            setVarselNavn("")
            setSmsTekst("");
            setEpostEmne("");
            setEpostTekst("");
            setNavNoTekst("");
            setNavNoUrl("");
            setUnsavedChanges(false);
            setDeaktivert(true);
        } else if (!editingNew) {
            getSingleVarselInfo(currentVarselTypeId).then(currentVarsel => {
                setCurrentVarselType(currentVarsel);
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
                setDeaktivert(currentVarsel?.inaktiv)
            });
        }
    };

    const saveForm = () => {
        if (!currentVarselTypeId) {
            // TODO: feilhåndtering her
            return;
        }
        const save: VarselInfo = (
                {
                    varseltypeId: currentVarselTypeId,
                    varselNavn: varselNavn,
                    varselKategori: currentVarselType?.varselKategori,
                    varselForDistribusjonKanal: currentVarselType?.varselForDistribusjonKanal,
                    inaktiv: deaktivert,
                    revarslingIntervall: currentVarselType?.revarslingIntervall,
                    antallRevarslinger: currentVarselType?.antallRevarslinger,
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
                    ].filter(varselmal => !editingNew || !!varselmal.foerstegangsvarselTekst)
                }
        );
        if (editingNew) {
            save.varselKategori = 'SERVICEMELDING';
            createNewVarselInfo(save).then(() => setEditingNew(false)).then(() => resetFormToCurrentSelectedVarsel())
        } else {
            updateVarselInfo(save).then(() => resetFormToCurrentSelectedVarsel())
        }

    }

    useEffect(resetFormToCurrentSelectedVarsel, [currentVarselTypeId, varselinfos, editingNew]);

    const count = smsTekst.length;
    return (<div className="">
                <TextField disabled={editDisabled || !editingNew} value={currentVarselTypeId} onChange={event => {
                    if (editingNew) {
                        setUnsavedChanges(true);
                        setCurrentVarselTypeId(event.target.value);
                    }
                }}
                           label={<Label>VarseltypeId</Label>}/>
                <TextField disabled={editDisabled} value={varselNavn} onChange={event => {
                    if (editingNew) {
                        setUnsavedChanges(true);
                        setVarselNavn(event.target.value);
                    }
                }} label={<Label>Varselnavn</Label>}/>
                <Heading size={'small'}>Varselkanal</Heading>
                <Panel border>
                    <Switch disabled={editDisabled} checked={smsPreferertKanal}
                            onClick={() => {
                                setUnsavedChanges(true);
                                setSmsPreferertKanal(!smsPreferertKanal);
                            }}>
                        <Label>SMS som preferert kanal</Label>
                    </Switch>
                    <Textarea value={smsTekst} onChange={event => {
                        setUnsavedChanges(true);
                        setSmsTekst(event.target.value);
                    }} label={<Label>SMS</Label>}></Textarea>
                    <span>Antall tegn: {count}</span>
                </Panel>
                <Panel border>
                    <Switch disabled={editDisabled} checked={epostPreferertKanal}
                            onClick={() => {
                                setUnsavedChanges(true);
                                setEpostPreferertKanal(!epostPreferertKanal);
                            }}>
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
                    <Switch disabled={editDisabled} checked={navNoPreferertKanal}
                            onClick={() => {
                                setUnsavedChanges(true);
                                setNavNoPreferertKanal(!navNoPreferertKanal);
                            }}>
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
                {editDisabled ? '' :
                        (<>
                            <Button disabled={!unsavedChanges} variant={'secondary'}
                                    onClick={resetFormToCurrentSelectedVarsel}>Avbryt</Button>
                            <Button disabled={!unsavedChanges}
                                    onClick={saveForm}>{editingNew ? 'Opprett' : 'Oppdater'}</Button>
                            <Button variant={'danger'}
                                    onClick={() => setDeaktivert(!deaktivert)}>{deaktivert ? 'Aktiver' : 'Deaktiver'}</Button>
                        </>)
                }
            </div>
    )
};

export default Varseltype;
