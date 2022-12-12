import React, {Suspense} from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import VarseltypePage from './pages/VarseltypePage';
import VarseltestPage from './pages/VarseltestPage';
import LoginPage from './pages/LoginPage';

interface MyRoutesProps {
    loginAction: (name: string) => void,
    username?: string
}

const MyRoutes: React.FC<MyRoutesProps> = ({loginAction, username}) => (
    <Suspense fallback={<div/>}>
        <Router>
            <Routes>
                <Route
                    path="/"
                    element={<VarseltypePage username={username} onLogoutAction={loginAction}/>}
                />
                <Route
                    path="/varseltest"
                    element={<VarseltestPage username={username} onLogoutAction={loginAction}/>}
                />
                <Route
                    path="/login"
                    element={<LoginPage loginAction={loginAction}  loggedinUser={username}/>}
                />
            </Routes>
        </Router>
    </Suspense>
);

export default MyRoutes;
