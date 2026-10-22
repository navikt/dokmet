package no.nav.dokmet.api.tkat021;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class VarselInfoTo {
	// Brukt av dokdistdpi-2
	private String varseltypeId;
	private Integer antallRevarslinger;
	private Integer revarslingIntervall;

	// Brukt av varsel2
	private Boolean inaktiv;
	private String varselURL;

	// Brukt av dokdistdpi-2 og varsel2
	private Set<String> preferertKanal;
	private Set<VarselMalTo> varselmals;

	// TODO: Ikke i bruk
	private String varselNavn;
	private String varselKategori;
	private String varselForDistribusjonKanal;
}
