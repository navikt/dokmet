import {Dropdown, Header} from "@navikt/ds-react-internal";
import {Link} from "react-router-dom";
import React from "react";
import '../App.css';

interface AppHeaderProps {
    user?: User,
    onLogoutAction: (x?: string) => void
}

const AppHeader: React.FC<AppHeaderProps> = ({user, onLogoutAction}) => {
    const isLoggedIn = !!user;
    return (<Header>
        <Header.Title as="h1" href='/dokmet/varseladmin'>Varseladmin</Header.Title>
        <Header.Button><Link className={'headerlink'} to={'/dokmet/varseladmin'}>Varseltype</Link></Header.Button>
        <Header.Button><Link className={'headerlink'}
                             to={'/dokmet/varseladmin/varseltest'}>Varseltest</Link></Header.Button>
        {
            isLoggedIn ?
                    (<Dropdown>
                        <Header.UserButton
                                as={Dropdown.Toggle}
                                name={user.name}
                        />
                        <Dropdown.Menu>
                            <Dropdown.Menu.List>
                                <Dropdown.Menu.List.Item>
                                    <a href={'/rest/varseladmin/oauth/logout'}>Logg ut</a>
                                </Dropdown.Menu.List.Item>
                            </Dropdown.Menu.List>
                        </Dropdown.Menu>
                    </Dropdown>) :
                    (<Header.Button><a className={'headerlink'} href={'/rest/varseladmin/oauth/login'}>Logg
                        inn</a></Header.Button>)
        }
    </Header>);
}

export default AppHeader;