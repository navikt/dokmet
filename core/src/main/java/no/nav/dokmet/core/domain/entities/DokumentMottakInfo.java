package no.nav.dokmet.core.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.dokmet.core.domain.AbstractDomainObject;
import no.nav.dokmet.core.domain.kode.ArkivBehandlingKode;
import no.nav.dokmet.core.domain.kode.KonverteringBehandlingKode;

/**
 * Domain object for DokumentMottakInfo
 * <p>
 * ID
 * PREDFINERT_AVSENDER
 * <p>
 * <p>
 * Følgende håndteres av {@link AbstractDomainObject}
 * VERSJON
 * OPPRETTET_AV
 * OPPRETTET_DATO
 * ENDRET_AV
 * ENDRET_DATO
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "DOKUMENT_MOTTAK_INFO")
@Builder
@AllArgsConstructor
public class DokumentMottakInfo extends AbstractDomainObject {

    private static final long serialVersionUID = -7282995086360679944L;

    @Id
    @NotNull
    @Column(name = "ID")
    private Long id;
	
    @MapsId
    @OneToOne(mappedBy = "dokumentMottakInfo")
    @JoinColumn(name = "id")
    private DokumenttypeInfo dokumenttypeInfo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "K_KONV_BEHANDLING")
    private KonverteringBehandlingKode konverteringBehandling;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "K_ARKIV_BEHANDLING")
    private ArkivBehandlingKode arkivBehandling;
	
	
	public DokumentMottakInfo(Long id, long version) {
        this.id = id;
        setVersion(version);
    }

}