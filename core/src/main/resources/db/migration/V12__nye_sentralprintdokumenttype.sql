INSERT INTO K_SENTRAL_PRINT_DOK_TYPE (k_sentral_print_dok_type,dekode,opprettet_dato,opprettet_av,endret_dato,endret_av)
SELECT
    'NAV_AARSOPPGAVE',
    'NAV Årsoppgave',
    timestamp '2022-05-18 15:00:00',
    'MMA-6152',
    NULL,
    NULL
FROM dual
WHERE NOT EXISTS (SELECT 1
                  FROM K_SENTRAL_PRINT_DOK_TYPE
                  WHERE k_sentral_print_dok_type = 'NAV_AARSOPPGAVE');

INSERT INTO K_SENTRAL_PRINT_DOK_TYPE (k_sentral_print_dok_type,dekode,opprettet_dato,opprettet_av,endret_dato,endret_av)
SELECT
    'NAV_UTBETALINGSMELDING',
    'NAV Utbetalingsmelding',
    timestamp '2022-05-18 15:00:00',
    'MMA-6152',
    NULL,
    NULL
FROM dual
WHERE NOT EXISTS (SELECT 1
                  FROM K_SENTRAL_PRINT_DOK_TYPE
                  WHERE k_sentral_print_dok_type = 'NAV_UTBETALINGSMELDING');
