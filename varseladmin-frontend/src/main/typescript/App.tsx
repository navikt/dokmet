import * as React from 'react';
import {useEffect, useState} from 'react';
import MyRoutes from "./Routes";
import {getUserInfo} from "./Api";

const App: React.FC = () => {
    const [user, setUser] = useState<User>(null);

    useEffect(() => {
        getUserInfo().then(user => {
            if (user.NAVident) {
                setUser(user);
            }
        })
    }, []);

    return (
            <>
                <MyRoutes user={user} loginAction={(s: string) => setUser(null)}/>
            </>
    );
}

export default App;
