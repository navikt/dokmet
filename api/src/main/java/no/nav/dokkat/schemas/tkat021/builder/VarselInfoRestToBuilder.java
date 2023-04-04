package no.nav.dokkat.schemas.tkat021.builder;

import no.nav.dokkat.schemas.tkat021.VarselInfoRestTo;
import no.nav.dokkat.schemas.tkat021.VarselMalRestTo;

import java.util.HashSet;
import java.util.Set;

/**
 * Builder for {@link VarselInfoRestTo}
 *
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat021.VarselInfoTo} generert builder
 */
@Deprecated
public final class VarselInfoRestToBuilder {
	private String varseltypeId;
	private String varselNavn;
	private String varselKategori;
	private String varselForDistribusjonKanal;
	private Boolean inaktiv;
	private Integer revarslingIntervall;
	private Integer antallRevarslinger;
	private String varselURL;
	private Set<String> preferertKanal = new HashSet<>();
	private Set<VarselMalRestTo> varselmals = new HashSet<>();

	private VarselInfoRestToBuilder() {
	}

	public static VarselInfoRestToBuilder aVarselInfoRestTo() {
		return new VarselInfoRestToBuilder();
	}

	public VarselInfoRestToBuilder varseltypeId(String varseltypeId) {
		this.varseltypeId = varseltypeId;
		return this;
	}

	public VarselInfoRestToBuilder varselNavn(String varselNavn) {
		this.varselNavn = varselNavn;
		return this;
	}

	public VarselInfoRestToBuilder varselKategori(String varselKategori) {
		this.varselKategori = varselKategori;
		return this;
	}

	public VarselInfoRestToBuilder varselForDistribusjonKanal(String varselForDistribusjonKanal) {
		this.varselForDistribusjonKanal = varselForDistribusjonKanal;
		return this;
	}

	public VarselInfoRestToBuilder inaktiv(Boolean inaktiv) {
		this.inaktiv = inaktiv;
		return this;
	}

	public VarselInfoRestToBuilder revarslingIntervall(Integer revarslingIntervall) {
		this.revarslingIntervall = revarslingIntervall;
		return this;
	}

	public VarselInfoRestToBuilder antallRevarslinger(Integer antallRevarslinger) {
		this.antallRevarslinger = antallRevarslinger;
		return this;
	}

	public VarselInfoRestToBuilder varselURL(String varselURL) {
		this.varselURL = varselURL;
		return this;
	}

	public VarselInfoRestToBuilder preferertKanal(Set<String> preferertKanal) {
		this.preferertKanal = new HashSet<>(preferertKanal);
		return this;
	}

	public VarselInfoRestToBuilder varselmals(Set<VarselMalRestTo> varselmals) {
		this.varselmals = new HashSet<>(varselmals);
		return this;
	}

	public VarselInfoRestTo build() {
		VarselInfoRestTo varselInfoRestTo = new VarselInfoRestTo();
		varselInfoRestTo.setVarseltypeId(varseltypeId);
		varselInfoRestTo.setVarselNavn(varselNavn);
		varselInfoRestTo.setVarselKategori(varselKategori);
		varselInfoRestTo.setVarselForDistribusjonKanal(varselForDistribusjonKanal);
		varselInfoRestTo.setInaktiv(inaktiv);
		varselInfoRestTo.setRevarslingIntervall(revarslingIntervall);
		varselInfoRestTo.setAntallRevarslinger(antallRevarslinger);
		varselInfoRestTo.setVarselURL(varselURL);
		varselInfoRestTo.setPreferertKanal(preferertKanal);
		varselInfoRestTo.setVarselmals(varselmals);
		return varselInfoRestTo;
	}
}
