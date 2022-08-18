package no.nav.dokmet.core.builders.builder;

import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.domain.entities.VarselMal;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.KanalKode;
import no.nav.dokmet.core.domain.kode.VarselKategoriKode;

import java.util.HashSet;
import java.util.Set;

/**
 * Builder for {@link VarselInfo}
 *
 * @author Andreas Skomedal, Visma Consulting.
 */
public final class VarselInfoBuilder extends Builder<VarselInfo> {

	private Long id;
	private String varseltypeId;
	private String varselNavn;
	private VarselKategoriKode varselKategori;
	private DistribusjonKanalKode varselForDistribusjonKanal;
	private Boolean inaktiv;
	private Integer revarslingIntervall;
	private Integer antallRevarslinger;
	private String varselURL;
	private Set<KanalKode> preferertKanal = new HashSet<>();
	private Set<VarselMal> varselmals = new HashSet<>();

	private VarselInfoBuilder() {
	}

	public static VarselInfoBuilder aVarselInfo() {
		return new VarselInfoBuilder();
	}

	public VarselInfoBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public VarselInfoBuilder varseltypeId(String varseltypeId) {
		this.varseltypeId = varseltypeId;
		return this;
	}

	public VarselInfoBuilder varselNavn(String varselNavn) {
		this.varselNavn = varselNavn;
		return this;
	}

	public VarselInfoBuilder varselKategori(VarselKategoriKode varselKategori) {
		this.varselKategori = varselKategori;
		return this;
	}

	public VarselInfoBuilder varselForDistribusjonKanal(DistribusjonKanalKode varselForDistribusjonKanal) {
		this.varselForDistribusjonKanal = varselForDistribusjonKanal;
		return this;
	}

	public VarselInfoBuilder inaktiv(Boolean inaktiv) {
		this.inaktiv = inaktiv;
		return this;
	}

	public VarselInfoBuilder revarslingIntervall(Integer revarslingIntervall) {
		this.revarslingIntervall = revarslingIntervall;
		return this;
	}

	public VarselInfoBuilder antallRevarslinger(Integer antallRevarslinger) {
		this.antallRevarslinger = antallRevarslinger;
		return this;
	}

	public VarselInfoBuilder varselURL(String varselURL) {
		this.varselURL = varselURL;
		return this;
	}

	public VarselInfoBuilder preferertKanal(Set<KanalKode> preferertKanal) {
		this.preferertKanal = new HashSet<>(preferertKanal);
		return this;
	}

	public VarselInfoBuilder varselmals(Set<VarselMal> varselmals) {
		this.varselmals = new HashSet<>(varselmals);
		return this;
	}

	public VarselInfo build() {
		VarselInfo varselInfo = new VarselInfo();
		varselInfo.setId(id);
		varselInfo.setVarseltypeId(varseltypeId);
		varselInfo.setVarselNavn(varselNavn);
		varselInfo.setVarselKategori(varselKategori);
		varselInfo.setVarselForDistribusjonKanal(varselForDistribusjonKanal);
		varselInfo.setInaktiv(inaktiv);
		varselInfo.setRevarslingIntervall(revarslingIntervall);
		varselInfo.setAntallRevarslinger(antallRevarslinger);
		varselInfo.setVarselURL(varselURL);
		varselInfo.setPreferertKanal(preferertKanal);
		varselInfo.setVarselmals(varselmals);
		for (VarselMal varselmal : varselmals) {
			varselmal.setVarselInfo(varselInfo);
		}
		return varselInfo;
	}
}
