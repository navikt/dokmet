package no.nav.dokmet.api.tkat021;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class VarselMalTo {
	// Brukt av varsel2
	private String varselTittel;

	// Brukt av dokdistdpi-2 og varsel2
	private String kanal;
	private String foerstegangsvarselTekst;

	// TODO: Ikke i bruk
	private String revarslingTekst;
}