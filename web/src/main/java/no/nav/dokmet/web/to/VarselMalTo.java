package no.nav.dokmet.web.to;

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
