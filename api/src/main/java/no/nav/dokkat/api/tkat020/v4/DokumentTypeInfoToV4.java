package no.nav.dokkat.api.tkat020.v4;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import no.nav.dokkat.api.AbstractToObject;

/**
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat020.DokumenttypeInfoTo}
 */
@Deprecated
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"dokumenttypeId", "dokumentTittel", "dokumentType", "dokumentKategori", "sensitivt", "utledRegisterInfo", "tema", "behandlingstema", "arkivBehandling", "arkivSystem", "artifaktId",
		"dokumentProduksjonsInfo", "dokumentMottakInfo"})
public class DokumentTypeInfoToV4 extends AbstractToObject {

	private String dokumenttypeId;
	private String dokumentTittel;
	private String dokumentType;
	private String dokumentKategori;
	private Boolean sensitivt;
	private boolean utledRegisterInfo;
	private String tema;
	private String behandlingstema;
	private String arkivSystem;
	private String artifaktId;

	private DokumentProduksjonsInfoToV4 dokumentProduksjonsInfo;
	private DokumentMottakInfoToV4 dokumentMottakInfo;
}
