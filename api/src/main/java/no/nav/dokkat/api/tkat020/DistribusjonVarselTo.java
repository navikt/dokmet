package no.nav.dokkat.api.tkat020;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import no.nav.dokkat.api.AbstractToObject;

/**
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat020.DistribusjonVarselTo}
 */
@Deprecated
@JsonPropertyOrder({"varseltypeId", "varselForDistribusjonKanal"})
public class DistribusjonVarselTo extends AbstractToObject {

	private String varselForDistribusjonKanal;
	private String varseltypeId;

	public String getVarselForDistribusjonKanal() {
		return varselForDistribusjonKanal;
	}

	public void setVarselForDistribusjonKanal(String varselForDistribusjonKanal) {
		this.varselForDistribusjonKanal = varselForDistribusjonKanal;
	}

	public String getVarseltypeId() {
		return varseltypeId;
	}

	public void setVarseltypeId(String varseltypeId) {
		this.varseltypeId = varseltypeId;
	}
}
