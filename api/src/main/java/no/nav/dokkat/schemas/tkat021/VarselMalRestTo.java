package no.nav.dokkat.schemas.tkat021;

/**
 * Rest object for VarselMal
 *
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat021.VarselMalTo}
 */
@Deprecated
public class VarselMalRestTo {

	private String kanal;

	private String varselTittel;

	private String foerstegangsvarselTekst;

	private String revarslingTekst;

	public String getKanal() {
		return kanal;
	}

	public void setKanal(String kanal) {
		this.kanal = kanal;
	}

	public String getVarselTittel() {
		return varselTittel;
	}

	public void setVarselTittel(String varselTittel) {
		this.varselTittel = varselTittel;
	}

	public String getFoerstegangsvarselTekst() {
		return foerstegangsvarselTekst;
	}

	public void setFoerstegangsvarselTekst(String foerstegangsvarselTekst) {
		this.foerstegangsvarselTekst = foerstegangsvarselTekst;
	}

	public String getRevarslingTekst() {
		return revarslingTekst;
	}

	public void setRevarslingTekst(String revarslingTekst) {
		this.revarslingTekst = revarslingTekst;
	}

}
