UPDATE DOKUMENTKATALOG 
SET 
	VEDLEGG = 'T', 
	ENDRET_AV           = 'S. Strøm',
  	ENDRET_DATO         = current_timestamp 
WHERE DOKUMENTTYPE_ID in ('000005','000006');

UPDATE DOKUMENTKATALOG 
SET 
	VEDLEGG = 'F', 
	ENDRET_AV           = 'S. Strøm',
  	ENDRET_DATO         = current_timestamp 
WHERE DOKUMENTTYPE_ID in ('000007','000012','000013','000014','000015','000016','000018','000019','000020');