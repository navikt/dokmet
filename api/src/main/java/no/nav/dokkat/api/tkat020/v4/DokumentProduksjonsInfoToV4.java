package no.nav.dokkat.api.tkat020.v4;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import no.nav.dokkat.api.AbstractToObject;
import no.nav.dokkat.api.tkat020.DistribusjonInfoTo;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat020.DokumentProduksjonsInfoTo}
 */
@Deprecated
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"vedlegg", "eksternVedlegg", "ikkeRedigerbarMalId", "redigerbarMalId",
		"malLogikkFil", "malXsdReferanse", "spraakInfos", "distribusjonInfo"})
public class DokumentProduksjonsInfoToV4 extends AbstractToObject {
	
	private Boolean vedlegg;
	private Boolean eksternVedlegg;
	private String ikkeRedigerbarMalId;
	private String redigerbarMalId;
	private String malLogikkFil;
	private String malXsdReferanse;
	private final List<SpraakInfoToV4> spraakInfos = new ArrayList<>();
	private DistribusjonInfoTo distribusjonInfo;
}
