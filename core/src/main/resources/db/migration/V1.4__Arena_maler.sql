UPDATE DOKUMENTKATALOG SET SENSITIVT = 'T', MAL_ID = 'MF_000001' , ENDRET_AV = 'R. Bjurstrøm', ENDRET_DATO = current_timestamp, VERSJON = VERSJON + 1 WHERE DOKUMENTTYPE_ID = '000001';
UPDATE DOKUMENTKATALOG SET MAL_ID = 'MF_000002', DOKUMENT_TITTEL='Individuell innkallingsbrev', SENSITIVT = 'T', ENDRET_AV = 'R. Bjurstrøm', ENDRET_DATO = current_timestamp, VERSJON = VERSJON + 1 WHERE DOKUMENTTYPE_ID = '000002';
UPDATE DOKUMENTKATALOG SET MAL_ID = 'MF_000006', SENSITIVT = 'T', MAL_XSD_REFERANSE='xsdfileshare/Arena/arena_000006.xsd', ENDRET_AV = 'R. Bjurstrøm', ENDRET_DATO = current_timestamp, VERSJON = VERSJON + 1 WHERE DOKUMENTTYPE_ID = '000006';
UPDATE DOKUMENTKATALOG SET MAL_ID = 'MF_000007', VEDLEGG='T', SENSITIVT = 'T', ENDRET_AV = 'R. Bjurstrøm', ENDRET_DATO = current_timestamp, VERSJON = VERSJON + 1 WHERE DOKUMENTTYPE_ID = '000007';
UPDATE DOKUMENTKATALOG SET MAL_ID = 'MF_000008', SENSITIVT = 'T', ENDRET_AV = 'R. Bjurstrøm', ENDRET_DATO = current_timestamp, VERSJON = VERSJON + 1 WHERE DOKUMENTTYPE_ID = '000008';
UPDATE DOKUMENTKATALOG SET MAL_ID = 'MF_000009', SENSITIVT = 'T', ENDRET_AV = 'R. Bjurstrøm', ENDRET_DATO = current_timestamp, VERSJON = VERSJON + 1 WHERE DOKUMENTTYPE_ID = '000009';
UPDATE DOKUMENTKATALOG SET MAL_ID = 'MF_000011', VEDLEGG= 'F', REDIGERBART='F', SENSITIVT = 'T', ENDRET_AV = 'R. Bjurstrøm', ENDRET_DATO = current_timestamp, VERSJON = VERSJON + 1 WHERE DOKUMENTTYPE_ID = '000011';
UPDATE DOKUMENTKATALOG SET MAL_ID = 'MF_000012', SENSITIVT = 'T', MAL_XSD_REFERANSE='xsdfileshare/Arena/arena_000012.xsd', PORTOKLASSE='B', ENDRET_AV = 'R. Bjurstrøm', ENDRET_DATO = current_timestamp, VERSJON = VERSJON + 1 WHERE DOKUMENTTYPE_ID = '000012';

INSERT INTO DOKUMENTKATALOG (dokumentkatalog_id, dokumenttype_id, mal_id, mal_logikk_fil, mal_version, mal_xsd_referanse, dokument_kategori, sensitivt, portoklasse, opprettet_dato, opprettet_av, endret_dato, endret_av, versjon, dokument_tittel, redigerbart, vedlegg)
VALUES (
  DOKUMENTKATALOG_SEQ.nextval, '000005', 'MF_000005', 'Arena', '1.0.0', 'xsdfileshare/Arena/Allow_Any_All.xsd', 'B', 'T', 'A',
  current_timestamp, 'R. Bjurstrøm',current_timestamp, 'R. Bjurstrøm', 1, 'Dokument for plan', 'F', 'F');

INSERT INTO DOKUMENTKATALOG (dokumentkatalog_id, dokumenttype_id, mal_id, mal_logikk_fil, mal_version, mal_xsd_referanse, dokument_kategori, sensitivt, portoklasse, opprettet_dato, opprettet_av, endret_dato, endret_av, versjon, dokument_tittel, redigerbart, vedlegg)
VALUES (
  DOKUMENTKATALOG_SEQ.nextval, '000013', 'MF_000013', 'Arena', '1.0.0', 'xsdfileshare/Arena/Allow_Any_All.xsd', 'B', 'T', 'B',
  current_timestamp, 'R. Bjurstrøm', current_timestamp, 'R. Bjurstrøm', 1, 'Melding om vedtak AAP', 'T', 'T');

INSERT INTO DOKUMENTKATALOG (dokumentkatalog_id, dokumenttype_id, mal_id, mal_logikk_fil, mal_version, mal_xsd_referanse, dokument_kategori, sensitivt, portoklasse, opprettet_dato, opprettet_av, endret_dato, endret_av, versjon, dokument_tittel, redigerbart, vedlegg)
VALUES (
  DOKUMENTKATALOG_SEQ.nextval, '000014', 'MF_000014', 'Arena', '1.0.0', 'xsdfileshare/Arena/Allow_Any_All.xsd', 'B', 'T', 'B',
  current_timestamp, 'R. Bjurstrøm', current_timestamp, 'R. Bjurstrøm', 1, 'Tvungen forvaltning', 'T', 'T');

INSERT INTO DOKUMENTKATALOG (dokumentkatalog_id, dokumenttype_id, mal_id, mal_logikk_fil, mal_version, mal_xsd_referanse, dokument_kategori, sensitivt, portoklasse, opprettet_dato, opprettet_av, endret_dato, endret_av, versjon, dokument_tittel, redigerbart, vedlegg)
VALUES (
  DOKUMENTKATALOG_SEQ.nextval, '000015', 'MF_000015', 'Arena', '1.0.0', 'xsdfileshare/Arena/Allow_Any_All.xsd', 'B', 'T', 'B',
  current_timestamp, 'R. Bjurstrøm', current_timestamp, 'R. Bjurstrøm', 1, 'Innvilgelse fritak meldekort', 'T', 'T');

INSERT INTO DOKUMENTKATALOG (dokumentkatalog_id, dokumenttype_id, mal_id, mal_logikk_fil, mal_version, mal_xsd_referanse, dokument_kategori, sensitivt, portoklasse, opprettet_dato, opprettet_av, endret_dato, endret_av, versjon, dokument_tittel, redigerbart, vedlegg)
VALUES (
  DOKUMENTKATALOG_SEQ.nextval, '000016', 'MF_000016', 'Arena', '1.0.0', 'xsdfileshare/Arena/Allow_Any_All.xsd', 'B', 'T', 'B',
  current_timestamp, 'R. Bjurstrøm', current_timestamp, 'R. Bjurstrøm', 1, 'Avslag fritak meldekort', 'T', 'T');

INSERT INTO DOKUMENTKATALOG (dokumentkatalog_id, dokumenttype_id, mal_id, mal_logikk_fil, mal_version, mal_xsd_referanse, dokument_kategori, sensitivt, portoklasse, opprettet_dato, opprettet_av, endret_dato, endret_av, versjon, dokument_tittel, redigerbart, vedlegg)
VALUES (
  DOKUMENTKATALOG_SEQ.nextval, '000017', 'MF_000017', 'Arena', '1.0.0', 'xsdfileshare/Arena/Allow_Any_All.xsd', 'B', 'T', 'B',
  current_timestamp, 'R. Bjurstrøm', current_timestamp, 'R. Bjurstrøm', 1, 'Stans av arbeidsavklaringspenger ved dødsfall', 'T', 'F');

INSERT INTO DOKUMENTKATALOG (dokumentkatalog_id, dokumenttype_id, mal_id, mal_logikk_fil, mal_version, mal_xsd_referanse, dokument_kategori, sensitivt, portoklasse, opprettet_dato, opprettet_av, endret_dato, endret_av, versjon, dokument_tittel, redigerbart, vedlegg)
VALUES (
  DOKUMENTKATALOG_SEQ.nextval, '000018', 'MF_000018', 'Arena', '1.0.0', 'xsdfileshare/Arena/Allow_Any_All.xsd', 'B', 'T', 'B',
  current_timestamp, 'R. Bjurstrøm', current_timestamp, 'R. Bjurstrøm', 1, 'Tidsbegrenset bortfall av arbeidsavklaringspenger', 'T', 'T');

INSERT INTO DOKUMENTKATALOG (dokumentkatalog_id, dokumenttype_id, mal_id, mal_logikk_fil, mal_version, mal_xsd_referanse, dokument_kategori, sensitivt, portoklasse, opprettet_dato, opprettet_av, endret_dato, endret_av, versjon, dokument_tittel, redigerbart, vedlegg)
VALUES (
  DOKUMENTKATALOG_SEQ.nextval, '000019', 'MF_000019', 'Arena', '1.0.0', 'xsdfileshare/Arena/Allow_Any_All.xsd', 'B', 'T', 'B',
  current_timestamp, 'R. Bjurstrøm', current_timestamp, 'R. Bjurstrøm', 1, 'Innvilgelse ung uføre', 'T', 'T');

INSERT INTO DOKUMENTKATALOG (dokumentkatalog_id, dokumenttype_id, mal_id, mal_logikk_fil, mal_version, mal_xsd_referanse, dokument_kategori, sensitivt, portoklasse, opprettet_dato, opprettet_av, endret_dato, endret_av, versjon, dokument_tittel, redigerbart, vedlegg)
VALUES (
  DOKUMENTKATALOG_SEQ.nextval, '000020', 'MF_000020', 'Arena', '1.0.0', 'xsdfileshare/Arena/Allow_Any_All.xsd', 'B', 'T', 'B',
  current_timestamp, 'R. Bjurstrøm', current_timestamp, 'R. Bjurstrøm', 1, 'Avslag på ung ufør', 'T', 'T');
