package no.nav.dokmet.core.xml;

import org.w3c.dom.ls.LSInput;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;

record InMemoryLSInput(String systemId, ByteArrayInputStream inputStream) implements LSInput {

	@Override
	public String getPublicId() {
		return null;
	}

	@Override
	public void setPublicId(String publicId) {
	}

	@Override
	public String getBaseURI() {
		return null;
	}

	@Override
	public void setBaseURI(String baseURI) {
	}

	@Override
	public String getEncoding() {
		return null;
	}

	@Override
	public void setEncoding(String encoding) {
	}

	@Override
	public boolean getCertifiedText() {
		return false;
	}

	@Override
	public void setCertifiedText(boolean certifiedText) {
	}

	@Override
	public String getSystemId() {
		return systemId;
	}

	@Override
	public void setSystemId(String systemId) {
	}

	@Override
	public Reader getCharacterStream() {
		return null;
	}

	@Override
	public void setCharacterStream(Reader characterStream) {
	}

	@Override
	public ByteArrayInputStream getByteStream() {
		return inputStream;
	}

	@Override
	public void setByteStream(InputStream byteStream) {
	}

	@Override
	public String getStringData() {
		return null;
	}

	@Override
	public void setStringData(String stringData) {
	}
}