package no.nav.dokmet.core.builders.builder;

import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.domain.entities.VarselMal;
import no.nav.dokmet.core.domain.kode.KanalKode;

/**
 * Builder for {@link VarselMal}
 *
 * @author Andreas Skomedal, Visma Consulting.
 */
public final class VarselMalBuilder extends Builder<VarselMal> {

	private String revarslingTekst;
	private String foerstegangsvarselTekst;
	private String varselTittel;
	private KanalKode kanal;
	private VarselInfo varselInfo;
	private Long id;

	private VarselMalBuilder() {
	}

	public static VarselMalBuilder aVarselMal() {
		return new VarselMalBuilder();
	}

	public VarselMalBuilder revarslingTekst(String revarslingTekst) {
		this.revarslingTekst = revarslingTekst;
		return this;
	}

	public VarselMalBuilder foerstegangsvarselTekst(String foerstegangsvarselTekst) {
		this.foerstegangsvarselTekst = foerstegangsvarselTekst;
		return this;
	}

	public VarselMalBuilder varselTittel(String varselTittel) {
		this.varselTittel = varselTittel;
		return this;
	}

	public VarselMalBuilder kanal(KanalKode kanal) {
		this.kanal = kanal;
		return this;
	}

	public VarselMalBuilder varselInfo(VarselInfo varselInfo) {
		this.varselInfo = varselInfo;
		return this;
	}

	public VarselMalBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public VarselMal build() {
		VarselMal varselMal = new VarselMal();
		varselMal.setRevarslingTekst(revarslingTekst);
		varselMal.setFoerstegangsvarselTekst(foerstegangsvarselTekst);
		varselMal.setVarselTittel(varselTittel);
		varselMal.setKanal(kanal);
		varselMal.setVarselInfo(varselInfo);
		varselMal.setId(id);
		return varselMal;
	}
}
