package no.nav.dokmet.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.dokmet.core.domain.AbstractDomainObject;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain object for DokumenttypeInfo.
 *
 * @author Joakim Bjørnstad, Visma Consulting
 * @author Stig Strøm, Acando
 * @author Leo-Andreas Ervik, Visma Consulting
 */
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
	
	@Column(name = "behandlingstema")
	private String behandlingstema;
	
	@Column(name = "artifakt_id")
	private String artifaktId;

	@Enumerated(EnumType.STRING)
	@Column(name = "K_PREDEFINERT_ARKIV_SYSTEM", nullable = false)
	private ArkivSystemKode arkivSystem = ArkivSystemKode.JOARK;

	@OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
	@PrimaryKeyJoinColumn
	@JoinColumn(name = "id", nullable = false)
	private DokumentProduksjonsInfo dokumentProduksjonsInfo;
	
	@OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
	@PrimaryKeyJoinColumn
	private DokumentMottakInfo dokumentMottakInfo;

	@OneToMany(mappedBy = "dokumenttypeInfo", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EksternDokumentType> eksternDokumentType = new HashSet<>();

	public DokumenttypeInfo(Long id, long version) {
		this.id = id;
		setVersion(version);
	}

	/**
	 * Delete EksternDokumenType before setting the value
	 *
	 * @param eksternDokumentType Set of eksternDokumentType objects that belongs to DokumentTypeInfo
	 */
	public void setEksternDokumentType(Set<EksternDokumentType> eksternDokumentType) {
		this.eksternDokumentType.clear();
		if (eksternDokumentType != null) {
			this.eksternDokumentType.addAll(eksternDokumentType);
		}
	}
}
