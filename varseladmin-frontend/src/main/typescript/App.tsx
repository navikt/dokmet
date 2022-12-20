import * as React from 'react';
import {useState} from 'react';
import MyRoutes from "./Routes";

const App: React.FC = () => {
    const [user, setUser] = useState<string>(null);

    return (
            <>
                <MyRoutes username={user} loginAction={setUser}/>
            </>
    );
}

export default App;
