package no.nav.dokmet.api.tkat020;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"predefinertDistKanal", "portoklasse", "sikkerhetsnivaa", "tosidigPrint", "sentralPrintDokumentType", "konvoluttvinduType", "distribusjonVarsels",})
public class DistribusjonInfoTo extends AbstractToObject {

	private String predefinertDistKanal;
	private String portoklasse;
	private Integer sikkerhetsnivaa;
	private Boolean tosidigPrint;
	private String sentralPrintDokumentType;
	private String konvoluttvinduType;
	private List<DistribusjonVarselTo> distribusjonVarsels;

	public void setDistribusjonVarsels(List<DistribusjonVarselTo> distribusjonVarsels) {
		if (distribusjonVarsels == null) {
			this.distribusjonVarsels = new ArrayList<>();
		} else {
			this.distribusjonVarsels = new ArrayList<>(distribusjonVarsels);
		}
	}

	public List<DistribusjonVarselTo> getDistribusjonVarsels() {
		if (distribusjonVarsels == null) {
			return Collections.emptyList();
		}
		return distribusjonVarsels;
	}
}
