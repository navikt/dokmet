import * as React from 'react';
import {Button, Label, Select} from "@navikt/ds-react";
import VarselInfo from "../VarselInfo";

interface VarselvelgerProps {
    loggedOut: boolean,
    onChooseVarsel: (a: string) => void,
    varselinfos: VarselInfo[]
}

const Varselvelger: React.FC<VarselvelgerProps> = ({loggedOut, onChooseVarsel, varselinfos}) => {
    return (<div>
                <Select label={<Label>Varseltype</Label>} onChange={event => {
                    onChooseVarsel(event.target.value);
                }}>
                    <option value={''}>Velg varseltype</option>
                    {varselinfos
                            .sort((a, b) => a.varselNavn.localeCompare(b.varselNavn))
                            .map(varselinfo =>
                                    (<option value={varselinfo.varseltypeId}
                                             key={varselinfo.varseltypeId}>{varselinfo.varselNavn}</option>)
                            )}
                </Select>
            </div>
    )
};

export default Varselvelger;
