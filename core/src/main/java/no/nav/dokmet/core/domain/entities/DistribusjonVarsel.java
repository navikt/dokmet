package no.nav.dokmet.core.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import no.nav.dokmet.core.domain.AbstractDomainObject;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;

import static java.lang.String.format;

@Entity
@NoArgsConstructor
@Table(name = "DISTRIBUSJON_VARSEL")
public class DistribusjonVarsel extends AbstractDomainObject {

	private static final long serialVersionUID = 8183509044914875585L;

	private static final String DISTRIBUSJON_VARSEL_SEQ = "DISTRIBUSJON_VARSEL_SEQ";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DISTRIBUSJON_VARSEL_SEQ)
	@SequenceGenerator(name = DISTRIBUSJON_VARSEL_SEQ, sequenceName = DISTRIBUSJON_VARSEL_SEQ, allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "k_varsel_for_dist_kanal",  nullable = false)
	private DistribusjonKanalKode varselForDistribusjonKanal;

	@Column(name = "varseltype_id",  nullable = false)
	private String varseltypeId;

	public DistribusjonVarsel(Long id, Long versjon) {
		this.id = id;
		setVersion(versjon);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DistribusjonKanalKode getVarselForDistribusjonKanal() {
		return varselForDistribusjonKanal;
	}

	public void setVarselForDistribusjonKanal(DistribusjonKanalKode varselForDistribusjonKanal) {
		this.varselForDistribusjonKanal = varselForDistribusjonKanal;
	}

	public String getVarseltypeId() {
		return varseltypeId;
	}

	public void setVarseltypeId(String varseltypeId) {
		this.varseltypeId = varseltypeId;
	}

	@Override
	public String toString() {
		return format("id:%s, varselForDistribusjonKanal:%s, varseltypeId:%s",
						id, varselForDistribusjonKanal, varseltypeId);
	}
}
