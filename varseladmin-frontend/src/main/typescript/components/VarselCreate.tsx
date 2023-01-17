import * as React from 'react';
import {Button} from "@navikt/ds-react";

interface VarselvelgerProps {
    disabled: boolean,
    performVarselCreate: () => void
}

const VarselCreate: React.FC<VarselvelgerProps> = ({disabled, performVarselCreate}) => {
    return (<div>
        <Button disabled={disabled} variant={'secondary'} onClick={performVarselCreate}>Legg til ny varseltype</Button>
    </div>);
}

export default VarselCreate;