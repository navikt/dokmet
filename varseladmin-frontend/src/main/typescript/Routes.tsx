import React, {Suspense} from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import VarseltypePage from './pages/VarseltypePage';
import VarseltestPage from './pages/VarseltestPage';
import LoginPage from './pages/LoginPage';
import {Navigate} from "react-router";

interface MyRoutesProps {
    loginAction: (name: string) => void,
    username?: string
}

const MyRoutes: React.FC<MyRoutesProps> = ({loginAction, username}) => (
        <Suspense fallback={<div/>}>
            <Router>
                <Routes>
                    <Route
                            path='/'
                            element={<Navigate to={'/dokmet/varseladmin/'}/>}
                    />
                    <Route
                            path='/dokmet/varseladmin/'
                            element={<VarseltypePage username={username} onLogoutAction={loginAction}/>}
                    />
                    <Route
                            path='/dokmet/varseladmin/varseltest'
                            element={<VarseltestPage username={username} onLogoutAction={loginAction}/>}
                    />
                    <Route
                            path='/dokmet/varseladmin/login'
                            element={<LoginPage loginAction={loginAction} loggedinUser={username}/>}
                    />
                </Routes>
            </Router>
        </Suspense>
);

export default MyRoutes;
