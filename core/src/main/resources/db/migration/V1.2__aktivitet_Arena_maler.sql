-- This is temporary until we can get automatic maldeploy up and running

INSERT INTO DOKUMENTKATALOG (dokumentkatalog_id, dokumenttype_id, mal_id, mal_logikk_fil, mal_version, mal_xsd_referanse, dokument_kategori, sensitivt, portoklasse, opprettet_dato, opprettet_av, endret_dato, endret_av, versjon, dokument_tittel, redigerbart, vedlegg)
VALUES (
  DOKUMENTKATALOG_SEQ.nextval, '000006', '000006', 'Arena', '1.0.0', 'xsdfileshare/Arena/Allow_Any_All.xsd', 'B', 'F', 'A',
  current_timestamp, 'A. Skomedal', NULL, NULL, 1, 'Avtale om aktivitet', 'F', 'F');
INSERT INTO DOKUMENTKATALOG (dokumentkatalog_id, dokumenttype_id, mal_id, mal_logikk_fil, mal_version, mal_xsd_referanse, dokument_kategori, sensitivt, portoklasse, opprettet_dato, opprettet_av, endret_dato, endret_av, versjon, dokument_tittel, redigerbart, vedlegg)
VALUES (
  DOKUMENTKATALOG_SEQ.nextval, '000011', '000011', 'Arena', '1.0.0', 'xsdfileshare/Arena/Allow_Any_All.xsd', 'B', 'F', 'A',
  current_timestamp, 'A. Skomedal', NULL, NULL, 1, 'Følgebrev', 'T', 'T');
INSERT INTO DOKUMENTKATALOG (dokumentkatalog_id, dokumenttype_id, mal_id, mal_logikk_fil, mal_version, mal_xsd_referanse, dokument_kategori, sensitivt, portoklasse, opprettet_dato, opprettet_av, endret_dato, endret_av, versjon, dokument_tittel, redigerbart, vedlegg)
VALUES (
  DOKUMENTKATALOG_SEQ.nextval, '000012', '000012', 'Arena', '1.0.0', 'xsdfileshare/Arena/Allow_Any_All.xsd', 'B', 'F', 'A',
  current_timestamp, 'A. Skomedal', NULL, NULL, 1, 'Vedtak om avslag §11-5', 'T', 'T');
