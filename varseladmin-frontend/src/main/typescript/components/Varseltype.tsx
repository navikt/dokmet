import * as React from 'react';
import {useCallback, useEffect, useState} from 'react';
import {BodyShort, Button, Heading, Label, Panel, Switch, Textarea, TextField} from "@navikt/ds-react";
import VarselInfo from "../domain/VarselInfo";
import {createVarselInfo, getSingleVarselInfo, updateVarselInfo} from "../Api";
import {createNewVarselInfo, updateExistingVarselInfo} from "../domain/VarselInfoAntiCorruptionLayer";
import ConfirmModal from "./ConfirmModal";

interface VarselTypeProps {
    editDisabled: boolean,
    varselinfos: VarselInfo[],
    currentVarselTypeId: string,
    setCurrentVarselTypeId: (id: string) => void,
    editingNew: boolean,
    setEditingNew: (b: boolean) => void
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
    const [showConfirmModal, setConfirmModal] = useState<boolean>(false);
    const [doSaveForm, setSaveForm] = useState<boolean>(false);

    const resetFormToCurrentSelectedVarsel = useCallback(() => {
        if (editingNew && !currentVarselTypeId) {
            setCurrentVarselType(null);
            setVarselNavn("")
            setDeaktivert(true);
            setSmsPreferertKanal(false)
            setSmsTekst("");
            setEpostPreferertKanal(false)
            setEpostEmne("");
            setEpostTekst("");
            setNavNoPreferertKanal(false)
            setNavNoTekst("");
            setNavNoUrl("");
            setUnsavedChanges(false);
        } else if (!editingNew) {
            getSingleVarselInfo(currentVarselTypeId).then(currentVarsel => {
                setCurrentVarselType(currentVarsel);
                setVarselNavn(currentVarsel?.varselNavn || "")
                setDeaktivert(currentVarsel?.inaktiv)

                const currentVarselSms = currentVarsel?.varselmals?.find((mal) => mal.kanal === 'SMS');
                setSmsPreferertKanal(currentVarsel?.preferertKanal?.find(item => item === 'SMS') !== undefined)
                setSmsTekst(currentVarselSms?.foerstegangsvarselTekst || "");

                const currentVarselEpost = currentVarsel?.varselmals?.find((mal) => mal.kanal === 'EPOST');
                setEpostPreferertKanal(currentVarsel?.preferertKanal?.find(item => item === 'EPOST') !== undefined)
                setEpostEmne(currentVarselEpost?.varselTittel || "");
                setEpostTekst(currentVarselEpost?.foerstegangsvarselTekst || "");

                const currentVarselNavNo = currentVarsel?.varselmals?.find((mal) => mal.kanal === 'DITT_NAV');
                setNavNoPreferertKanal(currentVarsel?.preferertKanal?.find(item => item === 'DITT_NAV') !== undefined)
                setNavNoTekst(currentVarselNavNo?.foerstegangsvarselTekst || "");
                setNavNoUrl(currentVarsel?.varselURL || "");

                setUnsavedChanges(false);
            });
        }
    }, [editingNew, currentVarselTypeId]);

    const saveForm = useCallback(() => {
        if (!currentVarselTypeId) {
            return;
        }
        if (editingNew) {
            const newVarselInfo = createNewVarselInfo(currentVarselTypeId, varselNavn, smsPreferertKanal, smsTekst,
                    epostPreferertKanal, epostEmne, epostTekst, navNoPreferertKanal, navNoUrl, navNoTekst);
            createVarselInfo(newVarselInfo).then(() => setEditingNew(false)).then(() => resetFormToCurrentSelectedVarsel())
        } else {
            const updatedVarselInfo = updateExistingVarselInfo(currentVarselType, currentVarselTypeId, varselNavn, deaktivert,
                    smsPreferertKanal, smsTekst, epostPreferertKanal, epostEmne, epostTekst, navNoPreferertKanal, navNoUrl, navNoTekst);
            updateVarselInfo(updatedVarselInfo).then(() => resetFormToCurrentSelectedVarsel())
        }
    }, [editingNew, setEditingNew, resetFormToCurrentSelectedVarsel, currentVarselType, currentVarselTypeId, varselNavn, deaktivert,
        smsPreferertKanal, smsTekst, epostPreferertKanal, epostEmne, epostTekst, navNoPreferertKanal, navNoUrl, navNoTekst])

    if (doSaveForm) {
        setSaveForm(false);
        saveForm();
    }

    useEffect(resetFormToCurrentSelectedVarsel, [currentVarselTypeId, varselinfos, editingNew, resetFormToCurrentSelectedVarsel]);

    function toggleActivation() {
        setDeaktivert(!deaktivert);
        setConfirmModal(false);
        setSaveForm(true);
    }

    return (<div id={'__next'}>
                <ConfirmModal mountpointId={'#__next'} cancelAction={() => setConfirmModal(false)}
                              confirmAction={toggleActivation}
                              open={showConfirmModal}><BodyShort>Er du sikker på at du
                    vil {unsavedChanges ? 'lagre endringer og ' : ''} {deaktivert ? 'aktivere' : 'deaktivere'} varselet?</BodyShort></ConfirmModal>
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
                <Panel className={'varsel-section'} border>
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
                    <span>Antall tegn: {smsTekst.length}</span>
                </Panel>
                <Panel className={'varsel-section'} border>
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
                <Panel className={'varsel-section'} border>
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
                            <Button className={'varseltype-button'} disabled={!unsavedChanges} variant={'secondary'}
                                    onClick={resetFormToCurrentSelectedVarsel}>Avbryt</Button>
                            <Button className={'varseltype-button'} disabled={!unsavedChanges}
                                    onClick={() => setSaveForm(true)}>{editingNew ? 'Opprett' : 'Oppdater'}</Button>
                            <Button className={'varseltype-button'} variant={'danger'}
                                    disabled={editingNew || !currentVarselTypeId}
                                    onClick={() => setConfirmModal(true)}>{deaktivert ? 'Aktiver' : 'Deaktiver'}</Button>
                        </>)
                }
            </div>
    )
};

export default Varseltype;
