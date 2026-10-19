package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.api.tkat021.VarselInfoTo;
import no.nav.dokmet.api.tkat021.VarselMalTo;
import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.domain.entities.VarselMal;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class VarselInfoMapper {

	public VarselInfoTo map(VarselInfo varselInfo) {
		return VarselInfoTo.builder()
				.varseltypeId(varselInfo.getVarseltypeId())
				.varselNavn(varselInfo.getVarselNavn())
				.varselKategori(varselInfo.getVarselKategori().name())
				.varselForDistribusjonKanal(mapVarselForDistribusjonKanal(varselInfo))
				.inaktiv(varselInfo.getInaktiv())
				.revarslingIntervall(varselInfo.getRevarslingIntervall())
				.antallRevarslinger(varselInfo.getAntallRevarslinger())
				.varselURL(varselInfo.getVarselURL())
				.preferertKanal(mapPreferertKanal(varselInfo))
				.varselmals(mapMalerTo(varselInfo.getVarselmals()))
				.build();
	}

	private static Set<String> mapPreferertKanal(VarselInfo varselInfo) {
		var preferertKanalSet = varselInfo.getPreferertKanal();

		return preferertKanalSet.stream()
				.map(Enum::name)
				.collect(Collectors.toSet());
	}

	private static String mapVarselForDistribusjonKanal(VarselInfo varselInfo) {
		var varselForDistribusjonsKanal = varselInfo.getVarselForDistribusjonKanal();

		return varselForDistribusjonsKanal == null ? null : varselForDistribusjonsKanal.name();
	}

	private Set<VarselMalTo> mapMalerTo(Set<VarselMal> varselMaler) {
		return varselMaler.stream()
				.map(this::mapMalTo)
				.collect(Collectors.toSet());
	}

	private VarselMalTo mapMalTo(VarselMal varselMal) {
		return VarselMalTo.builder()
				.kanal(varselMal.getKanal().name())
				.varselTittel(varselMal.getVarselTittel())
				.foerstegangsvarselTekst(varselMal.getFoerstegangsvarselTekst())
				.revarslingTekst(varselMal.getRevarslingTekst())
				.build();
	}

}