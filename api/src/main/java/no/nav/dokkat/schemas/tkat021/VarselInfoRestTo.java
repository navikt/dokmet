package no.nav.dokkat.schemas.tkat021;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

/**
 * Rest object for VarselInfo
 *
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat021.VarselInfoTo}
 */
@Deprecated
public class VarselInfoRestTo {

	private String varseltypeId;

	private String varselNavn;

	private String varselKategori;

	private String varselForDistribusjonKanal;

	private Boolean inaktiv;

	@Deprecated
	@JsonIgnore
	private String malVersion;

	private Integer revarslingIntervall;

	private Integer antallRevarslinger;

	private String varselURL;

	private Set<String> preferertKanal = new HashSet<>();

	private Set<VarselMalRestTo> varselmals = new HashSet<>();

	public String getVarseltypeId() {
		return varseltypeId;
	}

	public void setVarseltypeId(String varseltypeId) {
		this.varseltypeId = varseltypeId;
	}

	public String getVarselNavn() {
		return varselNavn;
	}

	public void setVarselNavn(String varselNavn) {
		this.varselNavn = varselNavn;
	}

	public String getVarselKategori() {
		return varselKategori;
	}

	public void setVarselKategori(String varselKategori) {
		this.varselKategori = varselKategori;
	}

	public String getVarselForDistribusjonKanal() {
		return varselForDistribusjonKanal;
	}

	public void setVarselForDistribusjonKanal(String varselForDistribusjonKanal) {
		this.varselForDistribusjonKanal = varselForDistribusjonKanal;
	}

	@Deprecated
	public String getMalVersion() {
		return malVersion;
	}

	@Deprecated
	public void setMalVersion(String malVersion) {
		this.malVersion = malVersion;
	}

	public Boolean getInaktiv() {
		return inaktiv;
	}

	public void setInaktiv(Boolean inaktiv) {
		this.inaktiv = inaktiv;
	}

	public Integer getRevarslingIntervall() {
		return revarslingIntervall;
	}

	public void setRevarslingIntervall(Integer revarslingIntervall) {
		this.revarslingIntervall = revarslingIntervall;
	}

	public Integer getAntallRevarslinger() {
		return antallRevarslinger;
	}

	public void setAntallRevarslinger(Integer antallRevarslinger) {
		this.antallRevarslinger = antallRevarslinger;
	}

	public String getVarselURL() {
		return varselURL;
	}

	public void setVarselURL(String varselURL) {
		this.varselURL = varselURL;
	}

	public Set<String> getPreferertKanal() {
		return new HashSet<>(preferertKanal);
	}

	public void setPreferertKanal(Set<String> preferertKanal) {
		this.preferertKanal = new HashSet<>(preferertKanal);
	}

	public Set<VarselMalRestTo> getVarselmals() {
		return new HashSet<>(varselmals);
	}

	public void setVarselmals(Set<VarselMalRestTo> varselmals) {
		this.varselmals = new HashSet<>(varselmals);
	}


}
