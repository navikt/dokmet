import * as React from 'react';
import {Heading, Label, Switch, TextField, Textarea, Button, Panel} from "@navikt/ds-react";

interface VarselTypeProps {
    loggedOut: boolean
}

const Varseltype: React.FC<VarselTypeProps> = ({loggedOut}) => {
    const count = 10; // TODO: tell chars i feltet SMS
    return (<div className="">
                <TextField disabled={loggedOut} label={<Label>VarseltypeId</Label>}></TextField>
                <TextField disabled={loggedOut} label={<Label>Varselnavn</Label>}></TextField>
                <Heading size={'small'}>Varselkanal</Heading>
                <Panel border>
                    <Switch disabled={loggedOut}>
                        <Label>SMS som preferert kanal</Label>
                    </Switch>
                    <Textarea label={<Label>SMS</Label>}></Textarea>
                    <span>Antall tegn: {count}</span>
                </Panel>
                <Panel border>
                    <Switch disabled={loggedOut}>
                        <Label>Epost som preferert kanal</Label>
                    </Switch>
                    <TextField label={<Label>Emne</Label>}></TextField>
                    <Textarea label={<Label>Epost</Label>}></Textarea>
                </Panel>
                <Panel border>
                    <Switch disabled={loggedOut}>
                        <Label>DittNav som preferert kanal</Label>
                    </Switch>
                    <TextField label={<Label>URL</Label>}></TextField>
                    <Textarea label={<Label>DittNav</Label>}></Textarea>
                </Panel>
                {loggedOut ? '' :
                        (<>
                            <Button variant={'secondary'}>Avbryt</Button>
                            <Button>Opprett</Button>
                        </>)
                }
            </div>
    )
};

export default Varseltype;
