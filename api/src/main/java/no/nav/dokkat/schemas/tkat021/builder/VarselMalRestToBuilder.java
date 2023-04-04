package no.nav.dokkat.schemas.tkat021.builder;

import no.nav.dokkat.schemas.tkat021.VarselMalRestTo;

/**
 * Builder for {@link VarselMalRestTo}
 *
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat021.VarselMalTo} generert builder
 */
@Deprecated
public final class VarselMalRestToBuilder {
	private String kanal;
	private String varselTittel;
	private String foerstegangsvarselTekst;
	private String revarslingTekst;

	private VarselMalRestToBuilder() {
	}

	public static VarselMalRestToBuilder aVarselMalRestTo() {
		return new VarselMalRestToBuilder();
	}

	public VarselMalRestToBuilder kanal(String kanal) {
		this.kanal = kanal;
		return this;
	}

	public VarselMalRestToBuilder varselTittel(String varselTittel) {
		this.varselTittel = varselTittel;
		return this;
	}

	public VarselMalRestToBuilder foerstegangsvarselTekst(String foerstegangsvarselTekst) {
		this.foerstegangsvarselTekst = foerstegangsvarselTekst;
		return this;
	}

	public VarselMalRestToBuilder revarslingTekst(String revarslingTekst) {
		this.revarslingTekst = revarslingTekst;
		return this;
	}

	public VarselMalRestTo build() {
		VarselMalRestTo varselMalRestTo = new VarselMalRestTo();
		varselMalRestTo.setKanal(kanal);
		varselMalRestTo.setVarselTittel(varselTittel);
		varselMalRestTo.setFoerstegangsvarselTekst(foerstegangsvarselTekst);
		varselMalRestTo.setRevarslingTekst(revarslingTekst);
		return varselMalRestTo;
	}
}
