package no.nav.dokmet.api.tkat021;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VarselMalTo {
	private String kanal;
	private String varselTittel;
	private String foerstegangsvarselTekst;
	private String revarslingTekst;
}
