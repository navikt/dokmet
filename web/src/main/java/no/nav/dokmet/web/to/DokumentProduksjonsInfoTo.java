package no.nav.dokmet.web.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"vedlegg", "eksternVedlegg", "ikkeRedigerbarMalId", "redigerbarMalId",
		"malLogikkFil", "malXsdReferanse", "spraakInfos", "distribusjonInfo"})
public class DokumentProduksjonsInfoTo extends AbstractToObject {
	
	private Boolean vedlegg;
	private Boolean eksternVedlegg;
	private String ikkeRedigerbarMalId;
	private String redigerbarMalId;
	private String malLogikkFil;
	private String malXsdReferanse;
	private final List<SpraakInfoTo> spraakInfos = new ArrayList<>();
	private DistribusjonInfoTo distribusjonInfo;
}
