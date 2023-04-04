package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.core.builders.builder.VarselInfoBuilder;
import no.nav.dokmet.core.builders.builder.VarselMalBuilder;
import no.nav.dokmet.core.domain.entities.VarselInfo;
import no.nav.dokmet.core.domain.entities.VarselMal;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.KanalKode;
import no.nav.dokmet.core.domain.kode.VarselKategoriKode;
import no.nav.dokmet.core.exceptions.IllegalValueException;
import no.nav.dokmet.api.tkat021.VarselInfoTo;
import no.nav.dokmet.api.tkat021.VarselMalTo;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

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

	public VarselInfo map(VarselInfoTo varselInfoTo) {
		return VarselInfoBuilder.aVarselInfo()
				.varseltypeId(varselInfoTo.getVarseltypeId())
				.varselNavn(varselInfoTo.getVarselNavn())
				.varselKategori(stringToEnum(VarselKategoriKode.class, varselInfoTo.getVarselKategori()))
				.varselForDistribusjonKanal(stringToEnum(DistribusjonKanalKode.class, varselInfoTo.getVarselForDistribusjonKanal()))
				.inaktiv(varselInfoTo.getInaktiv())
				.revarslingIntervall(varselInfoTo.getRevarslingIntervall())
				.antallRevarslinger(varselInfoTo.getAntallRevarslinger())
				.varselURL(varselInfoTo.getVarselURL())
				.preferertKanal(mapPrefererteKanaler(varselInfoTo.getPreferertKanal()))
				.varselmals(mapMaler(varselInfoTo.getVarselmals()))
				.build();
	}

	private Set<KanalKode> mapPrefererteKanaler(Set<String> prefererteKanaler) {
		return prefererteKanaler.stream()
				.map(it -> stringToEnum(KanalKode.class, it))
				.collect(Collectors.toSet());
	}

	private Set<VarselMal> mapMaler(Set<VarselMalTo> varselMaler) {
		return varselMaler.stream()
				.map(this::mapMal)
				.collect(Collectors.toSet());
	}

	private VarselMal mapMal(VarselMalTo varselMalTo) {
		return VarselMalBuilder.aVarselMal()
				.kanal(stringToEnum(KanalKode.class, varselMalTo.getKanal()))
				.varselTittel(varselMalTo.getVarselTittel())
				.foerstegangsvarselTekst(varselMalTo.getFoerstegangsvarselTekst())
				.revarslingTekst(varselMalTo.getRevarslingTekst())
				.build();
	}

	public VarselInfo map(VarselInfoTo varselInfoTo, VarselInfo existing) {
		existing.setVarselNavn(varselInfoTo.getVarselNavn());
		existing.setVarselKategori(stringToEnum(VarselKategoriKode.class, varselInfoTo.getVarselKategori()));
		existing.setVarselForDistribusjonKanal(existing.getVarselForDistribusjonKanal() == null ? null :
				stringToEnum(DistribusjonKanalKode.class, varselInfoTo.getVarselForDistribusjonKanal()));
		existing.setInaktiv(varselInfoTo.getInaktiv());
		existing.setRevarslingIntervall(varselInfoTo.getRevarslingIntervall());
		existing.setAntallRevarslinger(varselInfoTo.getAntallRevarslinger());
		existing.setVarselURL(varselInfoTo.getVarselURL());
		existing.setPreferertKanal(mapPrefererteKanaler(varselInfoTo.getPreferertKanal()));
		existing.setVarselmals(mapMaler(varselInfoTo.getVarselmals()));

		return existing;
	}

	private static <E extends Enum<E>> E stringToEnum(Class<E> enumClass, String enumName) {
		try {
			return enumName == null ? null : Enum.valueOf(enumClass, enumName);
		} catch (IllegalArgumentException e) {
			throw new IllegalValueException(format("%s er ikke en gyldig kodeverdi for %s", enumName, enumClass.getSimpleName()));
		}
	}

}
