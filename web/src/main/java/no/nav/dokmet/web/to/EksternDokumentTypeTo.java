package no.nav.dokmet.web.to;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"eksternDokumentTypeId", "eksternIdType"})
public class EksternDokumentTypeTo extends AbstractToObject {
	
	private String eksternDokumentTypeId;
	private String eksternIdType;
}
