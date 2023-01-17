import * as React from 'react';
import {useEffect} from 'react';
import {BodyLong, Heading, Modal, Panel} from "@navikt/ds-react";
import VarselInfo from "../domain/VarselInfo";

interface VarselPreviewProps {
    open: boolean,
    onClose: () => void,
    varselinfos: VarselInfo[],
    currentVarselId: string,
    mountpointId: string
}

const VarselPreview: React.FC<VarselPreviewProps> = ({open, onClose, varselinfos, currentVarselId, mountpointId}) => {
    const currentVarsel = varselinfos.find((varselinfo) => varselinfo.varseltypeId?.toLowerCase() === currentVarselId.toLowerCase());

    const currentVarselSms = currentVarsel?.varselmals?.find((mal) => mal.kanal === 'SMS');
    const currentVarselEpost = currentVarsel?.varselmals?.find((mal) => mal.kanal === 'EPOST');
    const currentVarselNavNo = currentVarsel?.varselmals?.find((mal) => mal.kanal === 'DITT_NAV');

    useEffect(() => Modal.setAppElement('#__next'), []);

    return (<Modal open={open} onClose={onClose}>
        <Modal.Content style={{maxWidth: '46em'}}>
            <Heading size={'large'}>Forhåndsvisning</Heading>
            {currentVarselSms ?
                    <Panel border>
                        <Heading size={'small'}>SMS</Heading>
                        <BodyLong>
                            {currentVarselSms.foerstegangsvarselTekst}
                        </BodyLong>
                        <span>Antall tegn: {currentVarselSms.foerstegangsvarselTekst.length}</span>
                    </Panel>
                    : null}
            {currentVarselEpost ?
                    <Panel border>
                        <Heading size={'small'}>Epost</Heading>
                        <span>Emne: {currentVarselEpost.varselTittel}</span>
                        <BodyLong>
                            {currentVarselEpost.foerstegangsvarselTekst}
                        </BodyLong>
                    </Panel>
                    : null}
            {currentVarselNavNo ?
                    <Panel border>
                        <Heading size={'small'}>DittNav</Heading>
                        <BodyLong>
                            {currentVarselNavNo.foerstegangsvarselTekst}
                        </BodyLong>
                        <span>URL: {currentVarsel.varselURL}</span>
                    </Panel>
                    : null}
        </Modal.Content>
    </Modal>);
};

export default VarselPreview;