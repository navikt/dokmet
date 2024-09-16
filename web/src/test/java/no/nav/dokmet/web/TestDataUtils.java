package no.nav.dokmet.web;

import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.EksternIdTypeKode;
import no.nav.dokmet.core.domain.kode.KanalKode;
import no.nav.dokmet.core.domain.kode.VarselKategoriKode;

public class TestDataUtils {
	
	public static final String SDP = "SDP";
	public static final String VARSELTYPE_ID = "varseltypeId";
	public static final String PORTO_KLASSE = "C5";
	public static final String DOKUMENT_TITTEL = "dokumentTittel";
	public static final String DOKUMENT_KATEGORI = "dokumentKategori";
	public static final String MAL_VERSJON = "1.0.0";
	public static final String MAL_LOGIKK_FIL = "ARENA";
	public static final String REDIGERBAR_MAL_ID = "redigerbarMalId";
	public static final String IKKE_REDIGERBAR_MAL_ID = "ikkeRedigerbarMalId";
	public static final String DOKUMENTTYPE_ID_INNGAAENDE = "010001";
	public static final String DOKUMENTTYPE_ID_UTGAAENDE = "000001";
	public static final String DOKUMENTTYPE_ID_NOTAT = "NOTAT";
	public static final String MAL_XSD_REFERANSE = DOKUMENTTYPE_ID_INNGAAENDE + ".xsd";
	public static final String EKSTERN_DOK_ID = "ID";
	public static final String EKSTERN_ID_TYPE = EksternIdTypeKode.SERVICE_CODE.toString();
	
	public static final String ENDRET_AV = "endretAv";
	public static final String OPPRETTET_AV = "opprettetAv";
	public static final String PORTO = "porto";
	public static final boolean SENSITIVT = false;
	public static final boolean UTLED_REGISTER_INFO = false;
	public static final String ARTIFAKT_ID = "artifakt_id";
	public static final String TEMA = "teema";
	public static final String BEHANDLINGSTEMA = "behandlingstema";
	public static final boolean VEDLEGG = true;
	public static final boolean EKSTERN_VEDLEGG = true;
	public static final String IKKE_REDIGERBAR_MALID = "ikkered_malid";
	public static final String REDIGERBAR_MALID = "ikkered_malid";
	public static final String DOKUMENTTYPE_ID = "000001";
	public static final int SIKKERHETSNIVAA = 4;
	public static final String SPRAAK_NO = "no";
	public static final String SPRAAK_EN = "en";
	public static final String DIST_KANAL_SDP = "SDP";
	public static final String VARSELTYPE_ID1 = "varseltypeId1";
	public static final String VARSELTYPE_ID2 = "varseltypeId2";
	public static final String DIST_KANAL_DITT_NAV = "DITT_NAV";
	public static final String DOKUMENT_TYPE_INNGAAENDE = "I";
	public static final String DOKUMENT_TYPE_UTGAAENDE = "U";
	public static final String EKSTERN_DOKUMENT_TYPE_ID_1 = "ID_1";
	public static final String EKSTERN_DOKUMENT_TYPE_ID_2 = "ID_2";
	public static final EksternIdTypeKode EKSTERN_ID_TYPE_KODE = EksternIdTypeKode.SERVICE_CODE;
	public static final String BREVPAKKE = "brevpakke";
	

	public static final VarselKategoriKode VARSEL_KATEGORI = VarselKategoriKode.DISTRIBUSJON;
	public static final DistribusjonKanalKode VARSEL_FOR_DISTRIBUSJON_KANAL = DistribusjonKanalKode.VED_DITT_NAV_OGSA_PRINT;
	public static final boolean INAKTIV = false;
	public static final int REVARSLING_INTERVALL = 5;
	public static final int ANTALL_REVARSLINGER = 2;
	public static final String TITTEL = "tittel";
	public static final KanalKode KANAL = KanalKode.EPOST;
	public static final String FOERSTEGANGSVARSEL_TEKST = "forestagang tekst";
	public static final String REVARSLING_TEKST = "revarseltekst";
	public static final String MAL_VERSION = "1.14.1";
	public static final String VARSEL_NAVN = "varselNavn";
	public static final String VARSEL_URL = "VARSEL_URL";
}
