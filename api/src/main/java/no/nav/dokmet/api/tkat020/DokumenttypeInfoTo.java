package no.nav.dokmet.api.tkat020;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"dokumenttypeId", "dokumentTittel", "dokumentType", "dokumentKategori", "sensitivt", "utledRegisterInfo", "tema", "arkivBehandling", "arkivSystem",
		"dokumentProduksjonsInfo"})
public class DokumenttypeInfoTo extends AbstractToObject {

	private String dokumenttypeId;
	private String dokumentTittel;
	private String dokumentType;
	private String dokumentKategori;
	private Boolean sensitivt;
	private boolean utledRegisterInfo;
	private String tema;
	private String arkivSystem;

	private DokumentProduksjonsInfoTo dokumentProduksjonsInfo;
}