import * as React from 'react';
import {Label, Button, Select} from "@navikt/ds-react";

interface VarselvelgerProps {
    loggedOut: boolean
}

const Varselvelger: React.FC<VarselvelgerProps> = ({loggedOut}) => {
    return (<div className="">
                <Select label={<Label>Varseltype</Label>}>
                    <option value={''}>Velg varseltype</option>
                    <option value={'pensjo1'}>1. gangvarsel Pensjon</option>
                    <option value={'pensjo2'}>2. gangvarsel Pensjon</option>
                </Select>
                <Button disabled={loggedOut} variant={'secondary'}>Legg til ny varseltype</Button>
            </div>
    )
};

export default Varselvelger;
