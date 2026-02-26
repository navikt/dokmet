package no.nav.dokmet.api.tkat021;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class VarselMalTo {
	private String kanal;
	private String varselTittel;
	private String foerstegangsvarselTekst;
	private String revarslingTekst;
}
