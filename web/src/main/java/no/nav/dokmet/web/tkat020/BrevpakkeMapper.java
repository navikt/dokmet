package no.nav.dokmet.web.tkat020;

import no.nav.dokmet.core.domain.entities.XsdFil;
import no.nav.dokmet.web.tkat030.BrevpakkeRequest;

import java.util.List;

public class BrevpakkeMapper {

	public static List<XsdFil> map(BrevpakkeRequest request) {
		return request.xsdfiler().stream()
				.map(xsdFilTo -> map(request.brevpakke(), xsdFilTo))
				.toList();
	}

	private static XsdFil map(String brevpakke, BrevpakkeRequest.XsdFilTo xsdFilTo) {
		return XsdFil.builder()
				.brevpakke(brevpakke)
				.filsti(xsdFilTo.filsti())
				.filnavn(xsdFilTo.filnavn())
				.xsdfil(xsdFilTo.xsdfil())
				.build();
	}

}
