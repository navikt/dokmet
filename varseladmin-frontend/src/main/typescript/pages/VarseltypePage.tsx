import * as React from 'react';
import {useEffect, useState} from 'react';
import Varseltype from "../components/Varseltype";
import {Heading} from "@navikt/ds-react";
import Varselvelger from "../components/Varselvelger";
import AppHeader from "../components/AppHeader";
import VarselInfo from "../domain/VarselInfo";
import VarselCreate from "../components/VarselCreate";
import {getVarselInfos} from "../Api";

interface VarseltypePageProps {
    user?: User,
    onLogoutAction: (x?: string) => void
}

const VarseltypePage: React.FC<VarseltypePageProps> = ({user, onLogoutAction}) => {
    const [currentVarselId, setCurrentVarselId] = useState<string>("");
    const [varselInfos, setVarselInfos] = useState<VarselInfo[]>([])
    const [editingNew, setEditingNew] = useState(false);

    useEffect(() => {
        getVarselInfos()
                .then(varselinfos => varselinfos.filter(varselinfo => varselinfo.varselKategori === 'SERVICEMELDING'))
                .then(setVarselInfos);
    }, []);

    const setupForVarselCreate = () => {
        setCurrentVarselId(null);
        setEditingNew(true);
    };

    const chooseExistingVarsel = (id: string) => {
        setCurrentVarselId(id);
        setEditingNew(false);
    };

    return (<>
        <AppHeader user={user} onLogoutAction={onLogoutAction}/>
        <div style={{maxWidth: '40em', margin: 'auto'}}>
            <Heading size={'medium'}>Varseltype</Heading>
            <Varselvelger disabled={!user} selectedVarseltypeId={currentVarselId} onChooseVarsel={chooseExistingVarsel}
                          varselinfos={varselInfos}/>
            <VarselCreate disabled={!user} performVarselCreate={setupForVarselCreate}/>
            <Varseltype editDisabled={!user} varselinfos={varselInfos}
                        currentVarselTypeId={currentVarselId} setCurrentVarselTypeId={setCurrentVarselId}
                        editingNew={editingNew} setEditingNew={setEditingNew}
            />
        </div>
    </>);
};

export default VarseltypePage;
