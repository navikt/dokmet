package no.nav.dokmet.web.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class VarselInfoTo {
	private String varseltypeId;
	private String varselNavn;
	private String varselKategori;
	private String varselForDistribusjonKanal;
	private Boolean inaktiv;
	@Deprecated
	@JsonIgnore
	private String malVersion;
	private Integer revarslingIntervall;
	private Integer antallRevarslinger;
	private String varselURL;
	private Set<String> preferertKanal;
	private Set<VarselMalTo> varselmals;
}
