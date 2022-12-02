import React, {Suspense} from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import VarseltypePage from './pages/VarseltypePage';
import VarseltestPage from './pages/VarseltestPage';
import LoginPage from './pages/LoginPage';

const MyRoutes: React.FC = () => (
    <Suspense fallback={<div/>}>
        <Router>
            <Routes>
                <Route
                    path="/"
                    element={<VarseltypePage/>}
                />
                <Route
                    path="/varseltest"
                    element={<VarseltestPage/>}
                />
                <Route
                    path="/login"
                    element={<LoginPage/>}
                />
            </Routes>
        </Router>
    </Suspense>
);

export default MyRoutes;
