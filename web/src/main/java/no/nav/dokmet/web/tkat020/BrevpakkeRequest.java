package no.nav.dokmet.web.tkat020;

import java.util.List;

public record BrevpakkeRequest(String brevpakke,
							   List<XsdFilTo> xsdfiler) {

	public record XsdFilTo(String filsti,
						   String filnavn,
						   byte[] xsdfil) {
	}
}