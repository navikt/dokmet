package no.nav.dokkat.api.tkat020.v4;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import no.nav.dokkat.api.AbstractToObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat020.DokumentMottakInfoTo}
 */
@Deprecated
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"konverteringsBehandling", "arkivBehandling",
		"arkivSystem", "eksternDokumentTyper"})
public class DokumentMottakInfoToV4 extends AbstractToObject {
	
	private String konverteringsBehandling;
	private String arkivBehandling;
	
	private List<EksternDokumentTypeToV4> eksternDokumentTyper;
	
	public void setEksternDokumentTyper(List<EksternDokumentTypeToV4> eksternDokumentTyper) {
		if (eksternDokumentTyper == null) {
			this.eksternDokumentTyper = new ArrayList<>();
		} else {
			this.eksternDokumentTyper = new ArrayList<>(eksternDokumentTyper);
		}
	}
}
