CREATE TABLE K_KONVOLUTTVINDU_TYPE(
  K_KONVOLUTTVINDU_TYPE           VARCHAR2(50 CHAR)  NOT NULL,
  DEKODE                          VARCHAR2(200 CHAR) NOT NULL,
  OPPRETTET_DATO                  TIMESTAMP          NOT NULL,
  OPPRETTET_AV                    VARCHAR2(50 CHAR)  NOT NULL,
  ENDRET_DATO                     TIMESTAMP          NULL,
  ENDRET_AV                       VARCHAR2(50 CHAR)  NULL,
  CONSTRAINT XPK_KONVOLUTTVINDU_TYPE PRIMARY KEY (K_KONVOLUTTVINDU_TYPE)
);

CREATE TABLE K_SENTRAL_PRINT_DOK_TYPE (
  K_SENTRAL_PRINT_DOK_TYPE        VARCHAR2(50 CHAR)  NOT NULL,
  DEKODE                          VARCHAR2(200 CHAR) NOT NULL,
  OPPRETTET_DATO                  TIMESTAMP          NOT NULL,
  OPPRETTET_AV                    VARCHAR2(50 CHAR)  NOT NULL,
  ENDRET_DATO                     TIMESTAMP          NULL,
  ENDRET_AV                       VARCHAR2(50 CHAR)  NULL,
  CONSTRAINT XPK_SENTRAL_PRINT_DOK_TYPE PRIMARY KEY (K_SENTRAL_PRINT_DOK_TYPE)
);

INSERT INTO K_KONVOLUTTVINDU_TYPE (k_konvoluttvindu_type,dekode,opprettet_dato,opprettet_av,endret_dato,endret_av)
 SELECT
  'W',
  'Konvolutt uten adressevindu',
  timestamp '2019-02-08 10:00:00',
  'Sigurd Midttun',
  NULL,
  NULL
 FROM dual
  WHERE NOT EXISTS (SELECT 1
                   FROM K_KONVOLUTTVINDU_TYPE
                   WHERE k_konvoluttvindu_type = 'W');

INSERT INTO K_KONVOLUTTVINDU_TYPE (k_konvoluttvindu_type,dekode,opprettet_dato,opprettet_av,endret_dato,endret_av)
 SELECT
  'X',
  'Konvolutt med adressevindu',
  timestamp '2019-02-08 10:00:00',
  'Sigurd Midttun',
  NULL,
  NULL
 FROM dual
  WHERE NOT EXISTS (SELECT 1
                   FROM K_KONVOLUTTVINDU_TYPE
                   WHERE k_konvoluttvindu_type = 'X');

INSERT INTO K_SENTRAL_PRINT_DOK_TYPE (k_sentral_print_dok_type,dekode,opprettet_dato,opprettet_av,endret_dato,endret_av)
 SELECT
  'NAV_STANDARD',
  'Nav standard',
  timestamp '2019-02-08 10:00:00',
  'Sigurd Midttun',
  NULL,
  NULL
 FROM dual
  WHERE NOT EXISTS (SELECT 1
                   FROM K_SENTRAL_PRINT_DOK_TYPE
                   WHERE k_sentral_print_dok_type = 'NAV_STANDARD');

ALTER TABLE DISTRIBUSJON_INFO ADD (
  tosidig_print                 NUMBER(1, 0) NULL,
  k_sentral_print_dok_type      VARCHAR2(50) NULL,
  k_konvoluttvindu_type         VARCHAR2(50) NULL
);

ALTER TABLE DISTRIBUSJON_INFO
  ADD CONSTRAINT FK_SENTRAL_PRINT_DOK_TYPE
FOREIGN KEY (K_SENTRAL_PRINT_DOK_TYPE)
REFERENCES K_SENTRAL_PRINT_DOK_TYPE;

ALTER TABLE DISTRIBUSJON_INFO
  ADD CONSTRAINT FK_KONVOLUTTVINDU_TYPE
FOREIGN KEY (K_KONVOLUTTVINDU_TYPE)
REFERENCES K_KONVOLUTTVINDU_TYPE;