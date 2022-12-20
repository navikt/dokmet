import React from 'react';
import ReactDOM from 'react-dom/client';
import "@navikt/ds-css";
import "@navikt/ds-css-internal";
import App from './App';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <App/>
    </React.StrictMode>
);