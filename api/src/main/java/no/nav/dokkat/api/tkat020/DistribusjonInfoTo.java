package no.nav.dokkat.api.tkat020;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import no.nav.dokkat.api.AbstractToObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat020.DistribusjonInfoTo}
 */
@Deprecated
@Getter
@Setter
@JsonPropertyOrder({"predefinertDistKanal", "portoklasse", "sikkerhetsnivaa", "tosidigPrint", "sentralPrintDokumentType", "konvoluttvinduType", "distribusjonVarsels",})
public class DistribusjonInfoTo extends AbstractToObject {

	private String predefinertDistKanal;
	private String portoklasse;
	private Integer sikkerhetsnivaa;
	private Boolean tosidigPrint;
	private String sentralPrintDokumentType;
	private String konvoluttvinduType;
	private List<DistribusjonVarselTo> distribusjonVarsels = new ArrayList<>();

	public void setDistribusjonVarsels(List<DistribusjonVarselTo> distribusjonVarsels) {
		if (distribusjonVarsels == null) {
			this.distribusjonVarsels = new ArrayList<>();
		} else {
			this.distribusjonVarsels = new ArrayList<>(distribusjonVarsels);
		}
	}

}
