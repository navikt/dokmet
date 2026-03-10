package no.nav.dokmet.api.tkat020;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@JsonPropertyOrder({"vedlegg", "eksternVedlegg", "ikkeRedigerbarMalId", "redigerbarMalId",
		"malLogikkFil", "malXsdReferanse", "spraakInfos", "distribusjonInfo"})
public class DokumentProduksjonsInfoTo extends AbstractToObject {

	private Boolean vedlegg;
	private Boolean eksternVedlegg;
	private String ikkeRedigerbarMalId;
	private String redigerbarMalId;
	private String malLogikkFil;
	private String malXsdReferanse;
	private List<SpraakInfoTo> spraakInfos;
	private DistribusjonInfoTo distribusjonInfo;

	public List<SpraakInfoTo> getSpraakInfos() {
		if (spraakInfos == null) {
			return Collections.emptyList();
		}
		return spraakInfos;
	}
}
