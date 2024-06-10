update dokumenttype_info
set
    dokument_tittel = 'Attest A1',
    endret_dato = current_timestamp,
    endret_av = 'MMA-7497'
where
    dokumenttype_id = '000116';