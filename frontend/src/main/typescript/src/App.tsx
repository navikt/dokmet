import * as React from 'react';
import MyRoutes from "./Routes";
import {useState} from "react";

const App: React.FC = () => {
    const [user, setUser] = useState<string>(null);

    return (
            <>
                <MyRoutes username={user} loginAction={setUser} />
            </>
    );
}

export default App;
