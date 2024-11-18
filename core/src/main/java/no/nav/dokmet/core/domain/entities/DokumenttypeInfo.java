package no.nav.dokmet.core.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.dokmet.core.domain.AbstractDomainObject;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "DOKUMENTTYPE_INFO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DokumenttypeInfo extends AbstractDomainObject {
	
	private static final long serialVersionUID = -3313979347435414631L;
	
	private static final String DOKUMENTTYPE_INFO_SEQ = "DOKUMENTTYPE_INFO_SEQ";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DOKUMENTTYPE_INFO_SEQ)
	@SequenceGenerator(name = DOKUMENTTYPE_INFO_SEQ, sequenceName = DOKUMENTTYPE_INFO_SEQ, allocationSize = 1)
	@Column(name = "id", nullable = false)
	protected Long id;
	
	@Column(name = "dokumenttype_id", nullable = false, unique = true)
	private String dokumenttypeId;
	
	@Column(name = "dokument_tittel")
	private String dokumentTittel;
	
	@Column(name = "dokument_kategori", nullable = false)
	private String dokumentKategori;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "k_dokumenttype", nullable = false)
	private DokumentTypeKode dokumentType;
	
	@Column(name = "sensitivt")
	private Boolean sensitivt;

	@Column(name = "utled_register_info", nullable = false)
	private boolean utledRegisterInfo;

	@Column(name = "tema")
	private String tema;

	@Enumerated(EnumType.STRING)
	@Column(name = "K_PREDEFINERT_ARKIV_SYSTEM", nullable = false)
	private ArkivSystemKode arkivSystem = ArkivSystemKode.JOARK;

	@OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
	@PrimaryKeyJoinColumn
	@JoinColumn(name = "id", nullable = false)
	private DokumentProduksjonsInfo dokumentProduksjonsInfo;

	public DokumenttypeInfo(Long id, long version) {
		this.id = id;
		setVersion(version);
	}

}