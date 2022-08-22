package no.nav.dokmet.core.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.dokmet.core.domain.AbstractDomainObject;
import no.nav.dokmet.core.domain.kode.EksternIdTypeKode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@Table(name = "EKSTERN_DOKUMENT_TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class EksternDokumentType extends AbstractDomainObject {
	
	private static final long serialVersionUID = -3313979347435414631L;
	
	private static final String EKSTERN_DOKUMENT_TYPE_SEQ = "EKSTERN_DOKUMENT_TYPE_SEQ";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = EKSTERN_DOKUMENT_TYPE_SEQ)
	@SequenceGenerator(name = EKSTERN_DOKUMENT_TYPE_SEQ, sequenceName = EKSTERN_DOKUMENT_TYPE_SEQ, allocationSize = 1)
	@Column(name = "ID", nullable = false)
	protected Long id;
	
	@Column(name = "EKSTERN_DOKUMENTTYPE_ID", nullable = false)
	private String eksternDokumentTypeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_DOKUMENTTYPE_INFO", referencedColumnName = "id", nullable = false)
	private DokumenttypeInfo dokumenttypeInfo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "K_EKSTERN_ID_TYPE")
	private EksternIdTypeKode eksternIdType;
	
	public EksternDokumentType(Long id, long version) {
		this.id = 1L;
		setVersion(version);
	}
	
	public EksternDokumentType(Long id, String eksternDokumentTypeId, DokumenttypeInfo dokumenttypeInfo, EksternIdTypeKode eksternIdTypeKode) {
		this.id = id;
		this.eksternDokumentTypeId = eksternDokumentTypeId;
		this.dokumenttypeInfo = dokumenttypeInfo;
		this.eksternIdType = eksternIdTypeKode;
		setVersion(1L);
	}
}
