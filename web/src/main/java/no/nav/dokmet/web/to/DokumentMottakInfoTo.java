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
@JsonPropertyOrder({"konverteringsBehandling", "arkivBehandling",
		"arkivSystem", "eksternDokumentTyper"})
public class DokumentMottakInfoTo extends AbstractToObject {
	
	private String konverteringsBehandling;
	private String arkivBehandling;
	
	private List<EksternDokumentTypeTo> eksternDokumentTyper;
	
	public void setEksternDokumentTyper(List<EksternDokumentTypeTo> eksternDokumentTyper) {
		if (eksternDokumentTyper == null) {
			this.eksternDokumentTyper = new ArrayList<>();
		} else {
			this.eksternDokumentTyper = new ArrayList<>(eksternDokumentTyper);
		}
	}
}
