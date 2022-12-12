import {Button, Heading, Label, TextField} from '@navikt/ds-react';
import * as React from 'react';
import AppHeader from "./AppHeader";
import {simulateLoginPromise} from "../ExampleData";
import {useState} from "react";

interface LoginPageProps {
    loggedinUser?: string,
    loginAction: (a: string) => void
}

const LoginPage: React.FC<LoginPageProps> = ({loggedinUser, loginAction}) => {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    const doLogin = () => {
        simulateLoginPromise().then(userdata => loginAction(userdata.displayName));
    }

    return (
            <>
                <AppHeader username={loggedinUser} onLogoutAction={loginAction}/>
                <div style={{maxWidth: '40em', margin: 'auto'}}>
                    <Heading size={'medium'}>Logg inn</Heading>
                    <TextField onChange={e => setUsername(e.target.value)} label={<Label>Brukerident</Label>}></TextField>
                    <TextField onChange={e => setPassword(e.target.value)} type={'password'} label={<Label>Passord</Label>}></TextField>
                    <Button onClick={doLogin}>Logg inn</Button>
                </div>
            </>);
};

export default LoginPage;
