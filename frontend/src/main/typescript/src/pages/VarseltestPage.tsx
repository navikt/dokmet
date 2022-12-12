import * as React from 'react';
import {Button, Heading, Label, TextField} from "@navikt/ds-react";
import Varselvelger from "./Varselvelger";
import VarselPreview from "./VarselPreview";
import {useState} from "react";
import AppHeader from "./AppHeader";
import {createRealisticVarselinfoPromise, varselinfoExamples} from "../ExampleData";
import VarselInfo from "../VarselInfo";

interface varseltestpageProps {
    username?: string,
    onLogoutAction: (x?: string) => void
}

const VarseltestPage: React.FC<varseltestpageProps> = ({username, onLogoutAction}) => {
    const [openPreviewModal, setOpenPreviewModal] = useState(false);
    const [varselInfos, setVarselInfos] = useState<VarselInfo[]>([])
    const [currentVarselId, setCurrentVarselId] = useState<string>("");

    createRealisticVarselinfoPromise()
            .then(varselinfos => varselinfos.filter(varselinfo => varselinfo.varselKategori === 'SERVICEMELDING'))
            .then(setVarselInfos);

    const loggedOut = !username;
    return (
            <div id={"__next"}>
                <AppHeader username={username} onLogoutAction={onLogoutAction}/>
                <div style={{maxWidth: '40em', margin: 'auto'}}>
                    <Heading size={'medium'}>Varseltest</Heading>
                    <Varselvelger loggedOut={!username} onChooseVarsel={setCurrentVarselId}
                                  varselinfos={varselInfos}/>
                    <div className={'actions-on-varsel'}>
                        {/* Den nåværende angular-appen parser ut en liste med parametre som må settes fra teksten
                            i varselmalen, og viser tekstbokser her for de parametrene */}
                        <Button disabled={currentVarselId === ""}
                                onClick={() => setOpenPreviewModal(true)}>Forhåndsvisning</Button>
                        {!loggedOut && (<>
                            {/* TODO: denne må ha validering */}
                            <TextField label={<Label>Fødselsnummer</Label>}></TextField>
                            <Button variant={'danger'}>Send varsel</Button>
                        </>)}
                    </div>
                    <VarselPreview open={openPreviewModal} onClose={() => setOpenPreviewModal(false)}
                                   varselinfos={varselInfos} currentVarselId={currentVarselId}
                                   mountpointId={'#__next'}/>
                </div>
            </div>
    );
}

export default VarseltestPage;
