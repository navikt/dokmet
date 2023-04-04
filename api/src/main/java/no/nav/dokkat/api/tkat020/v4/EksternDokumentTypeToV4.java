package no.nav.dokkat.api.tkat020.v4;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import no.nav.dokkat.api.AbstractToObject;

/**
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat020.EksternDokumentTypeTo}
 */
@Deprecated
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"eksternDokumentTypeId", "eksternIdType"})
public class EksternDokumentTypeToV4 extends AbstractToObject {
	
	private String eksternDokumentTypeId;
	private String eksternIdType;
}
