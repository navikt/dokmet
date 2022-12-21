import VarselInfo from "./domain/VarselInfo";

const getVarselInfos = (): Promise<VarselInfo[]> => {
    return fetch('/rest/varselinfo/').then(response => response.json());
}

const getSingleVarselInfo = (varselInfoId: string): Promise<VarselInfo> => {
    return fetch(`/rest/varseladmin/bff/rest/varselinfo/${varselInfoId}`).then(response => response.json());
}

const updateVarselInfo = (varselInfo: VarselInfo): Promise<string> => {
    return fetch(`/rest/varseladmin/bff/rest/varselinfo/${varselInfo.varseltypeId}`, {
        method: 'patch',
        body: JSON.stringify(varselInfo)
    }).then(response => response.text());
}

const createNewVarselInfo = (varselInfo: VarselInfo): Promise<string> => {
    return fetch('/rest/varseladmin/bff/rest/varselinfo/', {
        method: 'post',
        body: JSON.stringify(varselInfo)
    }).then(response => response.text());
}

export {getVarselInfos, getSingleVarselInfo, updateVarselInfo, createNewVarselInfo};