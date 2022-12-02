import {Dropdown, Header} from "@navikt/ds-react-internal";
import {Link} from "react-router-dom";
import React from "react";

const AppHeader: React.FC = () => {
    const isLoggedIn = false;
    const username = isLoggedIn ? "Saks Behandler" : "Logget ut";
    return (<Header>
        <Header.Title as="h1" href='/'>Varseladmin</Header.Title>
        <Header.Button><Link to={'/'}>Varseltekst</Link></Header.Button>
        <Header.Button><Link to={'/varseltest'}>Varseltest</Link></Header.Button>
        <Dropdown>
            <Header.UserButton as={Dropdown.Toggle} name={username} className={'ml-auto'}></Header.UserButton>
            <Dropdown.Menu>
                <Dropdown.Menu.List>
                    {
                        isLoggedIn ?
                                (<Dropdown.Menu.List.Item>Logg ut</Dropdown.Menu.List.Item>) :
                                (<Dropdown.Menu.List.Item><Link to={'/login'}>Logg inn</Link></Dropdown.Menu.List.Item>)
                    }
                </Dropdown.Menu.List>
            </Dropdown.Menu>
        </Dropdown>
    </Header>);
}

export default AppHeader;