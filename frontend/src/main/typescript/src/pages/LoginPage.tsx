import {Button, Heading, Label, TextField} from '@navikt/ds-react';
import * as React from 'react';
import AppHeader from "./AppHeader";

const LoginPage: React.FC = () => (
        <>
            <AppHeader/>
            <div className="App">
                <Heading size={'medium'}>Logg inn</Heading>
                <TextField label={<Label>Brukerident</Label>}></TextField>
                <TextField type={'password'} label={<Label>Passord</Label>}></TextField>
                <Button>Logg inn</Button>
            </div>
        </>);

export default LoginPage;
