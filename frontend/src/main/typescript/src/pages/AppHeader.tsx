import {Dropdown, Header} from "@navikt/ds-react-internal";
import {Link} from "react-router-dom";
import React from "react";
import '../App.css';

interface AppHeaderProps {
    username?: string,
    onLogoutAction: (x?: string) => void
}

const AppHeader: React.FC<AppHeaderProps> = ({username, onLogoutAction}) => {
    const isLoggedIn = !!username;
    return (<Header>
        <Header.Title as="h1" href='/dokmet/varseladmin/'>Varseladmin</Header.Title>
        <Header.Button><Link className={'headerlink'} to={'/dokmet/varseladmin/'}>Varseltype</Link></Header.Button>
        <Header.Button><Link className={'headerlink'}
                             to={'/dokmet/varseladmin/varseltest'}>Varseltest</Link></Header.Button>
        {
            isLoggedIn ?
                    (<Dropdown>
                        <Header.UserButton
                                as={Dropdown.Toggle}
                                name={username}
                        />
                        <Dropdown.Menu>
                            <Dropdown.Menu.List>
                                <Dropdown.Menu.List.Item onClick={() => onLogoutAction(null)}>Logg
                                    ut</Dropdown.Menu.List.Item>
                            </Dropdown.Menu.List>
                        </Dropdown.Menu>
                    </Dropdown>) :
                    (<Header.Button><Link className={'headerlink'} to={'/dokmet/varseladmin/login'}>Logg
                        inn</Link></Header.Button>)
        }
    </Header>);
}

export default AppHeader;