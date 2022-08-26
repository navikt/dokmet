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
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"spraaklag"})
public class SpraakInfoTo extends AbstractToObject {
	private String spraaklag;
}
