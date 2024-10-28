package no.nav.dokmet.core.xml;

import lombok.extern.slf4j.Slf4j;
import no.nav.dokmet.core.exceptions.ValiderBrevdataTechnicalException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Map;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

@Slf4j
public class XmlValidator {

	private final Schema schema;

	public XmlValidator(Path hovedfilsti, Map<Path, byte[]> xsdFilMap) throws Exception {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);

		byte[] hovedfil = xsdFilMap.get(hovedfilsti);
		if (hovedfil == null) {
			throw new ValiderBrevdataTechnicalException("Fant ikke hovedfil med sti=%s i xsdFilMap".formatted(hovedfilsti));
		}

		schemaFactory.setResourceResolver(new InMemoryResourceResolver(xsdFilMap));
		this.schema = schemaFactory.newSchema(new StreamSource(new ByteArrayInputStream(hovedfil)));
	}

	public void validate(String brevdataXml) throws Exception {
		Validator validator = schema.newValidator();

		validator.validate(new StreamSource(new ByteArrayInputStream(brevdataXml.getBytes())));
	}

}