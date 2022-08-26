package no.nav.dokmet.web.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
