package no.nav.dokkat.api.tkat030;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"validert", "valideringsFeil"})
public class ValidateDokumenttypeResponse {
	private Boolean validert;
	private String valideringsFeil;
}
