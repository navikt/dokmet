import {Button, Heading, Label, Link, TextField} from '@navikt/ds-react';
import * as React from 'react';
import {useState} from 'react';
import AppHeader from "../components/AppHeader";
import {simulateLoginPromise} from "../ExampleData";

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
                    <TextField onChange={e => setUsername(e.target.value)} value={username}
                               label={<Label>Brukerident</Label>}></TextField>
                    <TextField onChange={e => setPassword(e.target.value)} value={password} type={'password'}
                               label={<Label>Passord</Label>}></TextField>
                    <Button onClick={doLogin}>Logg inn</Button><br/>
                    <Link href={'/'}>Fortsett med lesetilgang uten å logge inn</Link>
                </div>
            </>);
};

export default LoginPage;
