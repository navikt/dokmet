import * as React from 'react';
import Varseltype from "./Varseltype";
import {Heading} from "@navikt/ds-react";
import Varselvelger from "./Varselvelger";
import AppHeader from "./AppHeader";

const VarseltypePage: React.FC = () => (
        <>
            <AppHeader/>
            <div>
                <Heading size={'medium'}>Varseltype</Heading>
                <Varselvelger loggedOut={false}/>
                <Varseltype loggedOut={false}/>
            </div>
        </>);

export default VarseltypePage;
