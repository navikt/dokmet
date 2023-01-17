import * as React from 'react';
import {ReactNode, useEffect} from 'react';
import {Button, Modal} from "@navikt/ds-react";

interface VarselPreviewProps {
    open: boolean,
    cancelAction: () => void,
    confirmAction: () => void,
    mountpointId: string,
    children: ReactNode
}

const VarselPreview: React.FC<VarselPreviewProps> = ({open, cancelAction, confirmAction, mountpointId, children}) => {
    useEffect(() => Modal.setAppElement(mountpointId), [mountpointId]);

    return (<Modal open={open} closeButton={false} onClose={cancelAction}>
        <Modal.Content style={{maxWidth: '46em'}}>
            {children}
            <Button className={'varseltype-button'} variant={'secondary'}
                    onClick={cancelAction}>Avbryt</Button>
            <Button className={'varseltype-button'}
                    onClick={confirmAction}>{'Ok'}</Button>
        </Modal.Content>
    </Modal>);
};

export default VarselPreview;
