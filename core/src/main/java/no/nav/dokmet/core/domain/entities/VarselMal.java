package no.nav.dokmet.core.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import no.nav.dokmet.core.domain.AbstractDomainObject;
import no.nav.dokmet.core.domain.kode.KanalKode;
import no.nav.dokmet.core.validation.VarselTekst;
import org.springframework.validation.annotation.Validated;

import static java.lang.String.format;

@Entity
@Validated
@Table(name = "VARSEL_MAL")
public class VarselMal extends AbstractDomainObject {

	private static final long serialVersionUID = -11345441176451915L;

	private static final String VARSEL_MAL_SEQ = "VARSEL_MAL_SEQ";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = VARSEL_MAL_SEQ)
	@SequenceGenerator(name = VARSEL_MAL_SEQ, sequenceName = VARSEL_MAL_SEQ, allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "fk_varsel_info_id", nullable = false)
	private VarselInfo varselInfo;

	@Enumerated(EnumType.STRING)
	@Column(name = "k_kanal", nullable = false)
	private KanalKode kanal;

	@Column(name = "varsel_tittel")
	private String varselTittel;

	@VarselTekst
	@Column(name = "foerstegangsvarsel_tekst", nullable = false)
	private String foerstegangsvarselTekst;

	@VarselTekst
	@Column(name = "revarsling_tekst")
	private String revarslingTekst;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VarselInfo getVarselInfo() {
		return varselInfo;
	}

	public void setVarselInfo(VarselInfo varselInfo) {
		this.varselInfo = varselInfo;
	}

	public KanalKode getKanal() {
		return kanal;
	}

	public void setKanal(KanalKode kanal) {
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

	@Override
	public String toString() {
		return format("id:%s, kanal:%s, varselTittel:%s, foerstegangsvarselTekst:%s, revarslingTekst:%s ",
						id, kanal, varselTittel, foerstegangsvarselTekst, revarslingTekst);
	}
}
