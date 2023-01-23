DELETE FROM VARSEL_MAL where FK_VARSEL_INFO_ID in (
  SELECT ID FROM VARSEL_INFO where VARSELTYPE_ID in ('SVAR',
                                                'SPORSMAL',
                                                'Gruppeaktiviet',
                                                'IndividuellSamtale',
                                                'RettTil4UkerFerie',
                                                'RettTil4UkerFerieOppbrukt',
                                                'RettTil4UkerFerieKonvertInn',
                                                'PermitteringSnartOppbrukt',
                                                'SyfoTredjepart',
                                                'SyfoOppgave',
                                                'SyfoAktivitetskrav',
                                                '1.GangVarselBrevPensj',
                                                '2.GangVarselBrevPensj')
);


DELETE FROM VARSEL_INFO_PREFKANAL where FK_VARSEL_INFO_ID in (
    SELECT ID FROM VARSEL_INFO where VARSELTYPE_ID in ('SVAR',
                                                'SPORSMAL',
                                                'Gruppeaktiviet',
                                                'IndividuellSamtale',
                                                'RettTil4UkerFerie',
                                                'RettTil4UkerFerieOppbrukt',
                                                'RettTil4UkerFerieKonvertInn',
                                                'PermitteringSnartOppbrukt',
                                                'SyfoTredjepart',
                                                'SyfoOppgave',
                                                'SyfoAktivitetskrav',
                                                '1.GangVarselBrevPensj',
                                                '2.GangVarselBrevPensj')
);

DELETE FROM VARSEL_INFO where VARSELTYPE_ID in ('SVAR',
                                                'SPORSMAL',
                                                'Gruppeaktiviet',
                                                'IndividuellSamtale',
                                                'RettTil4UkerFerie',
                                                'RettTil4UkerFerieOppbrukt',
                                                'RettTil4UkerFerieKonvertInn',
                                                'PermitteringSnartOppbrukt',
                                                'SyfoTredjepart',
                                                'SyfoOppgave',
                                                'SyfoAktivitetskrav',
                                                '1.GangVarselBrevPensj',
                                                '2.GangVarselBrevPensj');

INSERT INTO VARSEL_INFO (ID, VARSELTYPE_ID, INAKTIV, MAL_VERSION, OPPRETTET_AV, OPPRETTET_DATO, VERSJON, VARSEL_NAVN, VARSEL_URL, K_VARSEL_KATEGORI) VALUES (1, 'RettTil4UkerFerie', 0, '1', 'Roar Bjurstrøm', current_timestamp,  0, 'RettTil4UkerFerie', 'https://www.nav.no/feriemedytelser', 'SERVICEMELDING');
INSERT INTO VARSEL_INFO (ID, VARSELTYPE_ID, INAKTIV, MAL_VERSION, OPPRETTET_AV, OPPRETTET_DATO, VERSJON, VARSEL_NAVN, VARSEL_URL, K_VARSEL_KATEGORI) VALUES (2, 'RettTil4UkerFerieOppbrukt',0, '1', 'Roar Bjurstrøm', current_timestamp, 0, 'RettTil4UkerFerieOppbrukt', 'https://www.nav.no/feriemedytelser', 'SERVICEMELDING');
INSERT INTO VARSEL_INFO (ID, VARSELTYPE_ID, INAKTIV, MAL_VERSION, OPPRETTET_AV, OPPRETTET_DATO, VERSJON, VARSEL_NAVN, VARSEL_URL, K_VARSEL_KATEGORI) VALUES (3, 'SVAR',  0, '1', 'Roar Bjurstrøm', current_timestamp,  0, 'SVAR', '{url}', 'SERVICEMELDING');
INSERT INTO VARSEL_INFO (ID, VARSELTYPE_ID, INAKTIV, MAL_VERSION, OPPRETTET_AV, OPPRETTET_DATO, VERSJON, VARSEL_NAVN, VARSEL_URL, K_VARSEL_KATEGORI) VALUES (4, 'Gruppeaktivitet', 0, '1', 'Roar Bjurstrøm', current_timestamp,  0, 'Gruppeaktivitet', null, 'SERVICEMELDING');
INSERT INTO VARSEL_INFO (ID, VARSELTYPE_ID, INAKTIV, MAL_VERSION, OPPRETTET_AV, OPPRETTET_DATO, VERSJON, VARSEL_NAVN, VARSEL_URL, K_VARSEL_KATEGORI) VALUES (5, 'IndividuellSamtale', 0, '1', 'Roar Bjurstrøm', current_timestamp,  0, 'IndividuellSamtale', null, 'SERVICEMELDING');
INSERT INTO VARSEL_INFO (ID, VARSELTYPE_ID, INAKTIV, MAL_VERSION, OPPRETTET_AV, OPPRETTET_DATO, VERSJON, VARSEL_NAVN, VARSEL_URL, K_VARSEL_KATEGORI) VALUES (6, '1.GangVarselBrevPensj', 0, '1', 'Roar Bjurstrøm', current_timestamp,  0, '1.GangVarselBrevPensj', 'https://www.nav.no/dinpensjon', 'SERVICEMELDING');
INSERT INTO VARSEL_INFO (ID, VARSELTYPE_ID, INAKTIV, MAL_VERSION, OPPRETTET_AV, OPPRETTET_DATO, VERSJON, VARSEL_NAVN, VARSEL_URL, K_VARSEL_KATEGORI) VALUES (7, '2.GangVarselBrevPensj', 0, '1', 'Roar Bjurstrøm', current_timestamp,  0, '2.GangVarselBrevPensj', 'https://www.nav.no/dinpensjon', 'SERVICEMELDING');
INSERT INTO VARSEL_INFO (ID, VARSELTYPE_ID, INAKTIV, MAL_VERSION, OPPRETTET_AV, OPPRETTET_DATO, VERSJON, VARSEL_NAVN, VARSEL_URL, K_VARSEL_KATEGORI) VALUES (8, 'SPORSMAL', 0, '1', 'Roar Bjurstrøm', current_timestamp,  0, 'SPORSMAL', '{url}', 'SERVICEMELDING');
INSERT INTO VARSEL_INFO (ID, VARSELTYPE_ID, INAKTIV, MAL_VERSION, OPPRETTET_AV, OPPRETTET_DATO, VERSJON, VARSEL_NAVN, VARSEL_URL, K_VARSEL_KATEGORI) VALUES (9, 'PermitteringSnartOppbrukt', 0, '1', 'Roar Bjurstrøm', current_timestamp,  0, 'PermitteringSnartOppbrukt', null, 'SERVICEMELDING');
INSERT INTO VARSEL_INFO (ID, VARSELTYPE_ID, INAKTIV, MAL_VERSION, OPPRETTET_AV, OPPRETTET_DATO, VERSJON, VARSEL_NAVN, VARSEL_URL, K_VARSEL_KATEGORI) VALUES (10, 'SyfoTredjepart', 0, '1', 'Roar Bjurstrøm', current_timestamp,  0, 'SyfoTredjepart', null, 'SERVICEMELDING');
INSERT INTO VARSEL_INFO (ID, VARSELTYPE_ID, INAKTIV, MAL_VERSION, OPPRETTET_AV, OPPRETTET_DATO, VERSJON, VARSEL_NAVN, VARSEL_URL, K_VARSEL_KATEGORI) VALUES (11, 'SyfoOppgave', 0, '1', 'Roar Bjurstrøm', current_timestamp,  0, 'SyfoOppgave', '{url}', 'SERVICEMELDING');
INSERT INTO VARSEL_INFO (ID, VARSELTYPE_ID, INAKTIV, MAL_VERSION, OPPRETTET_AV, OPPRETTET_DATO, VERSJON, VARSEL_NAVN, VARSEL_URL, K_VARSEL_KATEGORI) VALUES (12, 'SyfoAktivitetskrav', 0, '1', 'Roar Bjurstrøm', current_timestamp,  0, 'SyfoAktivitetskrav', '{url}', 'SERVICEMELDING');

INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (1, 3, 'EPOST', 'Du har fått et svar fra NAV', 'Hei! Du har fått et svar fra NAV på henvendelsen din. Du får se svaret ved å logge deg inn i Innboks på Ditt NAV eller følge denne lenken {url}.
Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (2, 3, 'SMS', null, 'Hei! Du har fått svar fra NAV. Se svaret på Ditt NAV: {url}.
Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (3, 3, 'DITT_NAV', null, 'Du har fått et svar fra NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (4, 8, 'EPOST', 'Du har fått et spørsmål fra NAV', 'Hei! Du har fått et spørsmål fra NAV. Du får se spørsmålet ved å logge deg inn i Innboks på Ditt NAV eller følge denne lenken {url}.
Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (5, 8, 'SMS', null, 'Hei! Du har fått et spørsmål fra NAV. Se spørsmålet på Ditt NAV: {url}.
Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (6, 8, 'DITT_NAV', null, 'Du har fått et spørsmål fra NAV  ', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (7, 4, 'SMS', null, 'Hei! Du har et møte i regi av NAV på {sted} {tid:dd.MM.yyyy} klokken {tid:HH:mm}. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (8, 4, 'EPOST', 'Påminnelse om møte', 'Hei! Dette er en beskjed om at du har et møte på {sted} {tid:dd.MM.yyyy} klokken {tid:HH:mm}. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (9, 4, 'DITT_NAV', null, 'Dette er en beskjed om at du har et møte på {sted} {tid:dd.MM.yyyy} klokken {tid:HH:mm}', 'Roar Bjurstrøm', current_timestamp,  0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (10, 5, 'DITT_NAV', null, 'Dette er en beskjed om at du har et møte på {sted} {tid:dd.MM.yyyy} klokken {tid:HH:mm}', 'Roar Bjurstrøm', current_timestamp,  0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (11, 5, 'SMS', null, 'Hei! Du har et møte i regi av NAV på {sted} {tid:dd.MM.yyyy} klokken {tid:HH:mm}. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (12, 5, 'EPOST', 'Påminnelse om møte', 'Hei! Dette er en beskjed om at du har et møte på {sted} {tid:dd.MM.yyyy} klokken {tid:HH:mm}. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (13, 1, 'SMS', null, 'Hei! Du har rett til å få dagpenger mens du tar ferie i inntil 4 uker.
Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (14, 1, 'EPOST', 'Rett til dagpenger under ferie', 'Hei! Vi vil informere deg om at du har rett til å få dagpenger mens du tar ferie i inntil 4 uker. Dersom du ønsker å ta ferie må du oppgi hvilke dager du tar ferie på dine meldekort. Du kan lese mer om lovendreng om ferie og dagpenger på https://www.nav.no/feriemedytelser. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (15, 1, 'DITT_NAV', null, 'Vi vil informere deg om at du har rett til å få dagpenger mens du tar ferie i inntil 4 uker. Dersom du ønsker å ta ferie må du oppgi hvilke dager du tar ferie på dine meldekort. Du kan lese mer om lovendring om ferie og dagpenger på nav.no. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (16, 2, 'DITT_NAV', null, 'Vi vil informere deg om at du ikke lenger har rett til å få utbetalt dagpenger mens du tar ferie. Du har nå tatt 4 uker med ferie mens du har hatt dagpenger. Dersom du ønsker å ta ferie må du oppgi dette på dine meldekort, og du vil få trekk i utbetalingen av dagpenger for feriedager. Du kan lese mer om ferie og dagpenger på nav.no. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (17, 2, 'EPOST', 'Opphør av dagpenger under ferie','Hei! Vi vil informere deg om at du ikke lenger har rett til å få utbetalt dagpenger mens du tar ferie. Du har nå tatt 4 uker med ferie mens du har hatt dagpenger. Dersom du ønsker å ta ferie må du oppgi dette på dine meldekort, og du vil få trekk i utbetalingen av dagpenger for feriedager. Du kan lese mer om ferie og dagpenger på https://www.nav.no/feriemedytelser. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (18, 2, 'SMS', null, 'Hei! Du har nå hatt fire ukers ferie med dagpenger og har ikke lenger rett til å få utbetalt dagpenger mens du tar ferie.
Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (22, 6, 'DITT_NAV', null, 'Du har mottatt et brev i Din Pensjon. Gå til Din pensjon og les brevet i innboksen', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (23, 6, 'EPOST', 'Brev i Din innboks', 'Hei! Vi har sendt deg et brev. Logg inn på nav.no/dinpensjon og les brevet i innboksen.
Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (24, 6, 'SMS', null, 'Hei! Vi har sendt deg et brev. Logg inn på nav.no/dinpensjon og les brevet i innboksen.
Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (25, 7, 'EPOST', 'Brev i Din innboks', 'Hei! Vi minner om at du har et ulest brev i Din pensjon. Brevet blir ikke sendt til deg i posten. Logg inn på nav.no/dinpensjon og les brevet i innboksen.
Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp,0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (26, 7, 'DITT_NAV', null, 'Vi minner om at du har et ulest brev i Din pensjon. Gå til Din pensjon og les brevet i innboksen. Brevet blir ikke sendt til deg i posten.', 'Roar Bjurstrøm', current_timestamp,  0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (27, 7, 'SMS', null, 'Hei! Vi minner om at du har et ulest brev i Din pensjon. Brevet blir ikke sendt til deg i posten. Logg inn på nav.no/dinpensjon og les brevet i innboksen.
Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp,  0);

INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (29, 9, 'SMS', null, 'Hei! Du kan motta dagpenger under permittering i inntil 49 uker i løpet av en periode på 18 måneder. Etter 30 uker med dagpenger, har arbeidsgiveren din en lønnspliktperiode på 5 arbeidsdager. Derfor stanses dagpengene dine etter 30 uker. Hvis du fortsatt er permittert når lønnspliktperioden på 5 arbeidsdager er over, må du søke om gjenopptak av dagpengene dine. Les mer på nav.no. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (28, 9, 'DITT_NAV', null, 'Du kan motta dagpenger under permittering i inntil 49 uker i løpet av en periode på 18 måneder. Etter 30 uker med dagpenger, har arbeidsgiveren din en lønnspliktperiode på 5 arbeidsdager. Derfor stanses dagpengene dine etter 30 uker. Hvis du fortsatt er permittert når lønnspliktperioden på 5 arbeidsdager er over, må du søke om gjenopptak av dagpengene dine. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp,  0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (30, 9, 'EPOST', 'Dagpenger under permittering', 'Hei! Du kan motta dagpenger under permittering i inntil 49 uker i løpet av en periode på 18 måneder. Etter 30 uker med dagpenger, har arbeidsgiveren din en lønnspliktperiode på 5 arbeidsdager. Derfor stanses dagpengene dine etter 30 uker. Hvis du fortsatt er permittert når lønnspliktperioden på 5 arbeidsdager er over, må du søke om gjenopptak av dagpengene dine. Les mer på nav.no. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);

INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (32, 10, 'SMS', null, 'Hei. En av dine ansatte er sykmeldt. For å se sykmeldingen, logg inn på: {url}. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (33, 10, 'EPOST', 'Sykmeldt arbeidstaker', 'Hei. En av dine ansatte er sykmeldt. For å se sykmeldingen, logg inn på: {url}. Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);

INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (34, 11, 'DITT_NAV', null, 'Du har mottatt en ny sykmelding.', 'Roar Bjurstrøm', current_timestamp,  0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (35, 11, 'SMS', null, 'Hei! Det ligger en melding til deg på nav.no. Du er velkommen til å være med og prøve en tjeneste som er under arbeid. Logg inn på Ditt NAV eller følg denne lenken {url} Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (36, 11, 'EPOST', 'Informasjon fra NAV', 'Hei! Det ligger en melding til deg på nav.no. Du er velkommen til å være med og prøve en tjeneste som er under arbeid. Logg inn på Ditt NAV eller følg denne lenken {url} Vennlig hilsen NAV', 'Roar Bjurstrøm', current_timestamp, 0);

INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (37, 12, 'DITT_NAV', null, 'NAV skal nå vurdere din aktivitetsplikt', 'Roar Bjurstrøm', current_timestamp,  0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (38, 12, 'SMS', null, 'Hei! Det ligger en viktig melding til deg på nav.no. Logg inn på Ditt NAV, eller følg denne lenken {url}. Vennlig hilsen NAV.', 'Roar Bjurstrøm', current_timestamp, 0);
INSERT INTO VARSEL_MAL (ID, FK_VARSEL_INFO_ID, K_KANAL, VARSEL_TITTEL, FOERSTEGANGSVARSEL_TEKST, OPPRETTET_AV, OPPRETTET_DATO, VERSJON) VALUES (39, 12, 'EPOST', 'Informasjon fra NAV', 'Hei! Det ligger en viktig melding til deg på nav.no. Logg inn på Ditt NAV, eller følg denne lenken {url}. Vennlig hilsen NAV.', 'Roar Bjurstrøm', current_timestamp, 0);

INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (1, 'DITT_NAV');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (1, 'EPOST');

INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (2, 'DITT_NAV');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (2, 'EPOST');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (2, 'SMS');

INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (3, 'DITT_NAV');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (3, 'EPOST');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (3, 'SMS');

INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (4, 'DITT_NAV');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (4, 'EPOST');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (4, 'SMS');

INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (5, 'DITT_NAV');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (5, 'EPOST');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (5, 'SMS');

INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (6, 'DITT_NAV');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (6, 'EPOST');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (6, 'SMS');

INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (7, 'DITT_NAV');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (7, 'EPOST');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (7, 'SMS');

INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (8, 'DITT_NAV');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (8, 'EPOST');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (8, 'SMS');

INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (9, 'DITT_NAV');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (9, 'EPOST');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (9, 'SMS');

INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (10, 'EPOST');

INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (11, 'DITT_NAV');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (11, 'EPOST');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (11, 'SMS');

INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (12, 'DITT_NAV');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (12, 'EPOST');
INSERT INTO VARSEL_INFO_PREFKANAL (FK_VARSEL_INFO_ID, K_KANAL) VALUES (12, 'SMS');