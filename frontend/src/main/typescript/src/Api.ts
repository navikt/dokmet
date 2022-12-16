import VarselInfo from "./VarselInfo";

const getVarselInfos = (): Promise<VarselInfo[]> => {
    return fetch('/rest/varselinfo/').then(response => response.json());
}

export {getVarselInfos};