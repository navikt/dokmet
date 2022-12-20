import React, {useState} from 'react';
import {BodyLong, Button, Heading, Label, Modal, TextField} from "@navikt/ds-react";
import Varselvelger from "./Varselvelger";
import VarselPreview from "./VarselPreview";
import AppHeader from "./AppHeader";
import VarselInfo from "../VarselInfo";
import {Navigate} from "react-router";

interface varseltestpageProps {
    username?: string,
    onLogoutAction: (x?: string) => void
}

const VarseltestPage: React.FC<varseltestpageProps> = ({username, onLogoutAction}) => {
    const [openPreviewModal, setOpenPreviewModal] = useState(false);
    const [varselInfos, ] = useState<VarselInfo[]>([])
    const [currentVarselId, setCurrentVarselId] = useState<string>("");
    const [showPageDeactivatedModal, setShowPageDeactivatedModal] = useState<boolean>(true);
    const closeModal = () => setShowPageDeactivatedModal(false);
    const loggedOut = false;

    if (showPageDeactivatedModal) {
        return (
                <div id={"__next"}>
                    <AppHeader username={username} onLogoutAction={onLogoutAction}/>
                    <div style={{maxWidth: '40em', margin: 'auto'}}>
                        <Heading size={'medium'}>Varseltest</Heading>
                        <Varselvelger loggedOut={!username} onChooseVarsel={setCurrentVarselId}
                                      varselinfos={varselInfos}/>
                        <div className={'actions-on-varsel'}>
                            {/* TODO: Den nåværende angular-appen parser ut en liste med parametre som må settes fra teksten
                            i varselmalen, og viser tekstbokser her for de parametrene */}
                            <Button disabled={currentVarselId === ""}
                                    onClick={() => setOpenPreviewModal(true)}>Forhåndsvisning</Button>
                            {!loggedOut && (<>
                                <TextField label={<Label>Fødselsnummer</Label>}></TextField>
                                <Button variant={'danger'}>Send varsel</Button>
                            </>)}
                        </div>
                        <VarselPreview open={openPreviewModal} onClose={() => setOpenPreviewModal(false)}
                                       varselinfos={varselInfos} currentVarselId={currentVarselId}
                                       mountpointId={'#__next'}/>
                    </div>
                    <Modal open={showPageDeactivatedModal} onClose={closeModal}>
                        <Modal.Content style={{maxWidth: '46em'}}>
                            <Heading size={'large'}>Forhåndsvisning er deaktivert</Heading>
                            <BodyLong>
                                Forhåndsvisning av varsler er ikke implementert i den nye versjonen av varseladmin, og
                                det er dessverre
                                derfor ikke mulig å forhåndsvise varslinger i denne løsningen. Om du har behov for å
                                forhåndsvise eller
                                teste utsending av varsler i varseladmin kan du kontakte Team Dokumentløsninger på Slack
                                i kanalen&nbsp;
                                <code>#team_dokumentløsninger</code>
                            </BodyLong>
                            <Button style={{margin: '1em auto 0.5em'}} onClick={closeModal}>Ok</Button>
                        </Modal.Content>
                    </Modal>
                </div>
        );
    } else {
        return (<Navigate to={'/'}/>)
    }
}

export default VarseltestPage;
