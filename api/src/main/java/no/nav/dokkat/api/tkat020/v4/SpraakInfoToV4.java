package no.nav.dokkat.api.tkat020.v4;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import no.nav.dokkat.api.AbstractToObject;

/**
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat020.SpraakInfoTo}
 */
@Deprecated
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"spraaklag"})
public class SpraakInfoToV4 extends AbstractToObject {
	private String spraaklag;
}
