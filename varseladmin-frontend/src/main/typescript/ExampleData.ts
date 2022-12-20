const userDataExample = {
    "displayName": "Zorro Friedrich Burkersen",
    "username": "Z123591",
    "dn": "CN=Z123591,OU=Users,OU=NAV,OU=BusinessUnits,DC=adeo,DC=no",
    "roles": ["ROLE_VARSELADMIN", "ROLE_PERSON"]
};

const simulateLoginPromise = () => new Promise(resolve => setTimeout(resolve, 1000)).then(() => userDataExample)

export {userDataExample, simulateLoginPromise};