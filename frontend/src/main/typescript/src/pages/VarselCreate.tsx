import * as React from 'react';
import {Button} from "@navikt/ds-react";

interface VarselvelgerProps {
    loggedOut: boolean
}

const VarselCreate: React.FC<VarselvelgerProps> = ({loggedOut}) => {
    return (<div>
        <Button disabled={loggedOut} variant={'secondary'}>Legg til ny varseltype</Button>
    </div>);
}

export default VarselCreate;