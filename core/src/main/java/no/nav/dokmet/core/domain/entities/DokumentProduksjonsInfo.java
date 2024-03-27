package no.nav.dokmet.core.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import no.nav.dokmet.core.domain.AbstractDomainObject;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "DOKUMENT_PRODUKSJON_INFO")
public class DokumentProduksjonsInfo extends AbstractDomainObject {
	private static final long serialVersionUID = -337016170078755496L;
	/**
	 * Settes på malLogikkFil/malXsdReferanse der det er fagsystemet selv som produserer dokumentet.
	 * Se https://jira.adeo.no/browse/MMA-3731
	 */
	public static final String PLACEHOLDER_IKKE_BRUKT = "IKKE_BRUKT";

	@Id
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "vedlegg", nullable = false)
	private Boolean vedlegg;
	
	@Column(name = "ekstern_vedlegg", nullable = false)
	private Boolean eksternVedlegg;
	
	@Column(name = "ikke_redigerbar_mal_id")
	private String ikkeRedigerbarMalId;
	
	@Column(name = "redigerbar_mal_id")
	private String redigerbarMalId;
	
	@Column(name = "mal_logikk_fil", nullable = false)
	private String malLogikkFil;
	
	@Column(name = "mal_xsd_referanse", nullable = false)
	private String malXsdReferanse;
	
	@MapsId
	@OneToOne(mappedBy = "dokumentProduksjonsInfo")
	@JoinColumn(name = "id")
	private DokumenttypeInfo dokumenttypeInfo;
	
	@OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
	@JoinColumn(name = "fk_distribusjon_info_id", unique = true)
	private DistribusjonInfo distribusjonInfo;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "fk_dokumentproduksjon_info", nullable = false)
	private Set<SpraakInfo> spraakInfos = new HashSet<>();

	public DokumentProduksjonsInfo(Long id, long version) {
		this.id = id;
		setVersion(version);
	}

	public DokumentProduksjonsInfo() {
		//required by hibernate/jpa
	}

	public void setVersion(long version){
		super.setVersion(version);
	}
	
	public Set<SpraakInfo> getSpraakInfos() {
		return new HashSet<>(spraakInfos);
	}
	
	public void setSpraakInfos(Set<SpraakInfo> spraakInfos) {
		this.spraakInfos = new HashSet<>(spraakInfos);
	}
	
	public void addSpraakInfo(SpraakInfo spraakInfo) {
		spraakInfos.add(spraakInfo);
	}
	
	@PreRemove
	private void removeDistribusjonInfo() {
		setDistribusjonInfo(null);
	}
	
}
