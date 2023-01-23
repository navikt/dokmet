ALTER TABLE DOKUMENTKATALOG
ADD
(
varsling_sdp CHAR NULL,
varsling_alle_kanaler_sdp CHAR NULL,
varslingstekst_sdp VARCHAR2(1000 CHAR) NULL,
varsling_repetisjoner_sdp VARCHAR2(20 CHAR) NULL,
sikkerhetsnivaa_sdp NUMBER(2) NULL
);

UPDATE DOKUMENTKATALOG
SET
  varsling_sdp        = 'F',
  sikkerhetsnivaa_sdp = 4,
  ENDRET_AV           = 'R. Bjurstrøm',
  ENDRET_DATO         = current_timestamp;


ALTER TABLE DOKUMENTKATALOG MODIFY
(
varsling_sdp NOT NULL,
sikkerhetsnivaa_sdp NOT NULL
);