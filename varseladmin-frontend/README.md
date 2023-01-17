# varseladmin-frontend

`varseladmin-frontend` er en enkel *single page app* i React og Typescript, som brukes for å se eller endre tekstene for
servicemeldinger som sendes ut til brukere. Appen bygges med npm og webpack, og hostes av dokmet.

npm-bygget kjøres av maven som en del av det vanlige bygget av dokmet, så du trenger ikke gjøre noe spesielt utover å ha
npm installert på maskinen din for å få det til å funke.

For lokal utvikling kan du gå inn i denne mappen og kjøre `npm ci -q && npm run start` for å installere dependencies og
starte opp frontenden uten resten av dokmet. Det er dessverre ikke mulig å gjøre noen api-kall når appen er startet på
den måten, fordi dokmet ikke kjører.

Fra dokumentasjonen React har generert for dette prosjektet:

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

The page will reload when you make changes.\
You may also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

