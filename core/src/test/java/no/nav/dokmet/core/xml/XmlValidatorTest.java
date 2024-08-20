package no.nav.dokmet.core.xml;

import no.nav.dokmet.core.exceptions.ValiderBrevdataTechnicalException;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class XmlValidatorTest {

	@Test
	void skalKasteValiderBrevdataTechnicalExceptionHvisHovedfilstiManglerIXsdFilMap() {
		var hovedfilsti = Path.of("v1.000067.xsd");
		Map<Path, byte[]> xsdFilMap = Map.of(
				Path.of("v1.000066.xsd"), "fil1".getBytes(),
				Path.of("felles/v1.SimpleTypes.xsd"), "fil2".getBytes());

		assertThatExceptionOfType(ValiderBrevdataTechnicalException.class)
				.isThrownBy(() -> new XmlValidator(hovedfilsti, xsdFilMap))
				.withMessageContaining("Fant ikke hovedfil med sti=%s i xsdFilMap", hovedfilsti);
	}

}