import * as React from 'react';
import {Label, Select} from "@navikt/ds-react";
import VarselInfo from "../domain/VarselInfo";

interface VarselvelgerProps {
    disabled: boolean,
    onChooseVarsel: (a: string) => void,
    varselinfos: VarselInfo[],
    selectedVarseltypeId: string
}

const Varselvelger: React.FC<VarselvelgerProps> = ({disabled, onChooseVarsel, varselinfos, selectedVarseltypeId}) => {
    return (<div>
                <Select label={<Label>Varseltype</Label>} onChange={event => {
                    onChooseVarsel(event.target.value);
                }} value={selectedVarseltypeId}>
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
