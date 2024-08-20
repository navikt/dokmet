package no.nav.dokmet.web.tkat030;

public record ValiderBrevdataResponse(
    boolean gyldig,
    String valideringsfeil
) {
}
