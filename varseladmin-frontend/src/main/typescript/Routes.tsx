import React, {Suspense} from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import VarseltypePage from './pages/VarseltypePage';
import VarseltestPage from './pages/VarseltestPage';
import {Navigate} from "react-router";

interface MyRoutesProps {
    loginAction: (user: string) => void,
    user?: User
}

const MyRoutes: React.FC<MyRoutesProps> = ({loginAction, user}) => (
        <Suspense fallback={<div/>}>
            <Router>
                <Routes>
                    <Route
                            path='/'
                            element={<Navigate to={'/dokmet/varseladmin/'}/>}
                    />
                    <Route
                            path='/dokmet/varseladmin/'
                            element={<VarseltypePage user={user} onLogoutAction={loginAction}/>}
                    />
                    <Route
                            path='/dokmet/varseladmin/varseltest'
                            element={<VarseltestPage user={user} onLogoutAction={loginAction}/>}
                    />
                </Routes>
            </Router>
        </Suspense>
);

export default MyRoutes;
