package no.nav.dokmet.api.tkat020;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"varseltypeId", "varselForDistribusjonKanal"})
public class DistribusjonVarselTo extends AbstractToObject {

	private String varselForDistribusjonKanal;
	private String varseltypeId;

}
