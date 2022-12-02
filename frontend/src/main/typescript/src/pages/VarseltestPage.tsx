import * as React from 'react';
import {Button, Heading, Label, TextField} from "@navikt/ds-react";
import Varselvelger from "./Varselvelger";
import VarselPreview from "./VarselPreview";
import {useState} from "react";
import AppHeader from "./AppHeader";

const VarseltestPage: React.FC = () => {
    const [openPreviewModal, setOpenPreviewModal] = useState(false);

    const loggedOut = false;
    return (
            <>
            <AppHeader/>
            <div>
                <Heading size={'medium'}>Varseltest</Heading>
                <Varselvelger loggedOut={false}/>
                <div className={'actions-on-varsel'}>
                    <Button onClick={() => setOpenPreviewModal(true)}>Forhåndsvisning</Button>
                    {!loggedOut && (<>
                        <TextField label={<Label>Fødselsnummer</Label>}></TextField>
                        {/* TODO: denne må ha validering */}
                        <Button variant={'danger'}>Send varsel</Button>
                    </>)}
                </div>
                <VarselPreview open={openPreviewModal} onClose={() => setOpenPreviewModal(false)}/>
            </div>
            </>
    );
}

export default VarseltestPage;
