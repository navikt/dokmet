import VarselInfo from "./VarselInfo";

const getVarselInfos = (): Promise<VarselInfo[]> => {
    return fetch('/varselinfo/v1/').then(response => response.json());
}