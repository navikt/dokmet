package no.nav.dokmet.core.domain.entities;

import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import no.nav.dokmet.core.domain.AbstractDomainObject;
import no.nav.dokmet.core.domain.kode.KanalKode;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.VarselKategoriKode;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

@Getter
@Setter
@Entity
@Table(name = "VARSEL_INFO", uniqueConstraints = @UniqueConstraint(columnNames = "varseltype_id"))
public class VarselInfo extends AbstractDomainObject {

	private static final long serialVersionUID = -7845969658524L;

	private static final String VARSEL_INFO_SEQ = "VARSEL_INFO_SEQ";

	public static final String VARSELTYPE_ID_REGEX = "[a-zA-Z0-9\\._]+";
	public static final String VARSELTYPE_ID_FEIL = "ugyldig varseltypeId, tillat: store og små engelske bokstaver, tall, understrek og punktum";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = VARSEL_INFO_SEQ)
	@SequenceGenerator(name = VARSEL_INFO_SEQ, sequenceName = VARSEL_INFO_SEQ, allocationSize = 1)
	@Column(name = "id", nullable = false)
	private Long id;

	@Pattern(regexp = VARSELTYPE_ID_REGEX, message = VARSELTYPE_ID_FEIL)
	@Column(name = "varseltype_id", nullable = false)
	private String varseltypeId;

	@Column(name = "varsel_navn", nullable = false)
	private String varselNavn;

	@Enumerated(EnumType.STRING)
	@Column(name = "k_varsel_kategori", nullable = false)
	private VarselKategoriKode varselKategori;

	@Enumerated(EnumType.STRING)
	@Column(name = "k_varsel_for_dist_kanal")
	private DistribusjonKanalKode varselForDistribusjonKanal;

	@Column(name = "inaktiv", nullable = false)
	private Boolean inaktiv;

	@Column(name = "revarsling_intervall")
	private Integer revarslingIntervall;

	@Column(name = "antall_revarslinger")
	private Integer antallRevarslinger;

	@Column(name = "varsel_url")
	private String varselURL;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "varsel_info_prefkanal", joinColumns = @JoinColumn(name = "fk_varsel_info_id"))
	@Column(name = "k_kanal")
	@Enumerated(EnumType.STRING)
	private Set<KanalKode> preferertKanal = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "varselInfo")
	private Set<VarselMal> varselmals = new HashSet<>();

	public Set<KanalKode> getPreferertKanal() {
		return new HashSet<>(preferertKanal);
	}

	public void setPreferertKanal(Set<KanalKode> preferertKanal) {
		this.preferertKanal = new HashSet<>(preferertKanal);
	}

	public Set<VarselMal> getVarselmals() {
		return new HashSet<>(varselmals);
	}

	public void setVarselmals(Set<VarselMal> varselmals) {
		this.varselmals.clear();
		this.varselmals.addAll(varselmals);
		for (VarselMal varselmal : varselmals) {
			varselmal.setVarselInfo(this);
		}
	}

	@Override
	public String toString() {
		return format("id:%s, varseltypeId:%s, varselNavn:%s, varselKategori:%s, varselForDistribusjonKanal:%s, inaktiv%s, revarslingIntervall:%s, antallRevarslinger:%s, preferertKanal:%s, varselURL:%s, varselmals:%s",
						id, varseltypeId, varselNavn, varselKategori, varselForDistribusjonKanal, inaktiv, revarslingIntervall, antallRevarslinger, preferertKanal, varselURL, varselmals);
	}
}
