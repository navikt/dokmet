package no.nav.dokmet.core.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Setter;
import no.nav.dokmet.core.domain.AbstractDomainObject;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode;
import no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Boolean.TRUE;
import static java.lang.String.format;

@Entity
@Table(name = "DISTRIBUSJON_INFO")
public class DistribusjonInfo extends AbstractDomainObject {

	private static final long serialVersionUID = 8233694932600163904L;

	private static final String DISTRIBUSJON_INFO_SEQ = "DISTRIBUSJON_INFO_SEQ";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DISTRIBUSJON_INFO_SEQ)
	@SequenceGenerator(name = DISTRIBUSJON_INFO_SEQ, sequenceName = DISTRIBUSJON_INFO_SEQ, allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "k_predefinert_dist_kanal")
	private DistribusjonKanalKode predefinertDistKanal;

	@Column(name = "portoklasse", nullable = false)
	private String portoklasse;

	@Column(name = "sikkerhetsnivaa", nullable = false)
	private Integer sikkerhetsnivaa;

	@Setter
	@Column(name = "tosidig_print")
	private Boolean tosidigPrint;

	@Setter
	@Enumerated(EnumType.STRING)
	@Column(name = "k_sentral_print_dok_type")
	private SentralPrintDokumentTypeCode sentralPrintDokumentType;

	@Setter
	@Enumerated(EnumType.STRING)
	@Column(name = "k_konvoluttvindu_type")
	private KonvoluttvinduTypeCode konvoluttvinduType;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "fk_distribusjon_info_id", nullable = false)
	private Set<DistribusjonVarsel> distribusjonVarsels = new HashSet<>();

	public DistribusjonInfo(Long id, Long versjon) {
		this.id = id;
		setVersion(versjon);
	}

	public DistribusjonInfo() {
		//required by hibernate/jpa
	}

	public Long getId() {
		return id;
	}

	public DistribusjonKanalKode getPredefinertDistKanal() {
		return predefinertDistKanal;
	}

	public void setPredefinertDistKanal(DistribusjonKanalKode predefinertDistKanal) {
		this.predefinertDistKanal = predefinertDistKanal;
	}

	public String getPortoklasse() {
		return portoklasse;
	}

	public void setPortoklasse(String portoklasse) {
		this.portoklasse = portoklasse;
	}

	public Integer getSikkerhetsnivaa() {
		return sikkerhetsnivaa;
	}

	public void setSikkerhetsnivaa(Integer sikkerhetsnivaa) {
		this.sikkerhetsnivaa = sikkerhetsnivaa;
	}

	public boolean getTosidigPrint() {
		return tosidigPrint == null ? TRUE : tosidigPrint;
	}

	public SentralPrintDokumentTypeCode getSentralPrintDokumentType() {
		return this.sentralPrintDokumentType == null ? SentralPrintDokumentTypeCode.NAV_STANDARD : this.sentralPrintDokumentType;
	}

	public KonvoluttvinduTypeCode getKonvoluttvinduType() {
		return this.konvoluttvinduType == null ? KonvoluttvinduTypeCode.X : this.konvoluttvinduType;
	}

	public Set<DistribusjonVarsel> getDistribusjonVarsels() {
		return new HashSet<>(distribusjonVarsels);
	}

	public void setDistribusjonVarsels(Set<DistribusjonVarsel> distribusjonVarsels) {
		this.distribusjonVarsels = new HashSet<>(distribusjonVarsels);
	}

	public void addDistribusjonVarsel(DistribusjonVarsel distribusjonVarsel) {
		distribusjonVarsels.add(distribusjonVarsel);
	}

	@Override
	public String toString() {
		return format("id:%s, predefinertDistKanal:%s, portoklasse:%s, sikkerhetsnivaa:%s, distribusjonVarsels:%s",
						id, predefinertDistKanal, portoklasse, sikkerhetsnivaa, distribusjonVarsels);
	}
}
