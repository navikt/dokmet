import * as React from 'react';
import Varseltype from "./Varseltype";
import {Heading} from "@navikt/ds-react";
import Varselvelger from "./Varselvelger";
import AppHeader from "./AppHeader";
import {useEffect, useState} from "react";
import VarselInfo from "../VarselInfo";
import VarselCreate from "./VarselCreate";
import {getVarselInfos} from "../Api";

interface VarseltypePageProps {
    username?: string,
    onLogoutAction: (x?: string) => void
}

const VarseltypePage: React.FC<VarseltypePageProps> = ({username, onLogoutAction}) => {
    const [currentVarselId, setCurrentVarselId] = useState<string>("");
    const [varselInfos, setVarselInfos] = useState<VarselInfo[]>([])

    useEffect(() => {
        getVarselInfos()
                .then(varselinfos => varselinfos.filter(varselinfo => varselinfo.varselKategori === 'SERVICEMELDING'))
                .then(setVarselInfos);
    }, []);

    return (<>
        <AppHeader username={username} onLogoutAction={onLogoutAction}/>
        <div style={{maxWidth: '40em', margin: 'auto'}}>
            <Heading size={'medium'}>Varseltype</Heading>
            <Varselvelger loggedOut={!username} onChooseVarsel={setCurrentVarselId}
                          varselinfos={varselInfos}/>
            <VarselCreate loggedOut={!username}/>
            <Varseltype loggedOut={!username} currentVarselTypeId={currentVarselId}
                        varselinfos={varselInfos}/>
        </div>
    </>);
};

export default VarseltypePage;
