INSERT INTO K_DIST_KANAL (k_dist_kanal, dekode, opprettet_dato, opprettet_av, endret_dato, endret_av)
SELECT 'DPVT', 'Digital Post til Virksomheter', TIMESTAMP '2023-06-20 15:35:00', 'MMA-6178', NULL, NULL
FROM DUAL
WHERE NOT EXISTS(SELECT 1 FROM K_DIST_KANAL WHERE k_dist_kanal = 'DPVT');