package no.nav.dokmet.core.xml;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.exceptions.XsdFilNotFoundException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Map;

@Slf4j
record InMemoryResourceResolver(Map<Path, byte[]> xsdFilMap) implements LSResourceResolver {

	@Override
	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
		Path filsti = Path.of(systemId);

		byte[] xsdFil = xsdFilMap.keySet()
				.stream()
				.filter(path -> path.getFileName().equals(filsti.getFileName()))
				.map(xsdFilMap::get)
				.findFirst()
				.orElse(null);

		if (xsdFil == null) {
			log.error("Fant ikke xsdFil for filsti={}", filsti);
			throw new XsdFilNotFoundException("Fant ikke xsdFil for filsti=%s".formatted(filsti));
		}

		return new InMemoryLSInput(systemId, new ByteArrayInputStream(xsdFil));
	}

}