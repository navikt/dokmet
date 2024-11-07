package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.core.domain.entities.XsdFil;
import no.nav.dokmet.web.tkat020.BrevpakkeRequest.XsdFilTo;

import java.util.List;

public class BrevpakkeMapper {

	public static List<XsdFil> map(BrevpakkeRequest request) {
		return request.xsdfiler().stream()
				.map(xsdFilTo -> map(request.brevpakke(), xsdFilTo))
				.toList();
	}

	private static XsdFil map(String brevpakke, XsdFilTo xsdFilTo) {
		return XsdFil.builder()
				.brevpakke(brevpakke)
				.filsti(getFilsti(xsdFilTo))
				.filnavn(xsdFilTo.filnavn())
				.xsdfil(xsdFilTo.xsdfil())
				.build();
	}

	private static String getFilsti(XsdFilTo xsdFilTo) {
		return xsdFilTo.filsti();
	}

}