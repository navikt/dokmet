CREATE TABLE DOKUMENTKATALOG
(
	dokumentkatalog_id   NUMBER(11) NOT NULL ,
	dokumenttype_id      VARCHAR2(20) NOT NULL ,
	mal_id               VARCHAR2(20) NULL ,
	mal_logikk_fil       VARCHAR2(50) NULL ,
	mal_version          VARCHAR2(40) NULL ,
	mal_xsd_referanse    VARCHAR2(200) NULL ,
	dokument_kategori    VARCHAR2(50) NOT NULL ,
	sensitivt            CHAR NOT NULL ,
	portoklasse          VARCHAR2(20) NULL ,
	opprettet_dato       TIMESTAMP NOT NULL ,
	opprettet_av         VARCHAR2(20) NOT NULL ,
	endret_dato          TIMESTAMP NULL ,
	endret_av            VARCHAR2(20) NULL ,
	versjon              NUMBER(9) NOT NULL ,
	dokument_tittel      VARCHAR2(200) NOT NULL,
	redigerbart          CHAR NULL,
	vedlegg              CHAR NULL
);

CREATE UNIQUE INDEX XPKDOKUMENTKATALOG ON DOKUMENTKATALOG
(dokumentkatalog_id   ASC);

ALTER TABLE DOKUMENTKATALOG
	ADD CONSTRAINT  XPKDOKUMENTKATALOG PRIMARY KEY (dokumentkatalog_id);

CREATE UNIQUE INDEX XAK1DOKUMENTKATALOG ON DOKUMENTKATALOG
(dokumenttype_id   ASC);

ALTER TABLE DOKUMENTKATALOG
ADD CONSTRAINT  XAK1DOKUMENTKATALOG UNIQUE (dokumenttype_id);

Create SEQUENCE DOKUMENTKATALOG_SEQ START WITH 1 INCREMENT BY 1 NOMAXVALUE NOCYCLE NOCACHE;
