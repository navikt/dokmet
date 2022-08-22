package no.nav.dokmet.core.builders.builder;

import no.nav.dokmet.core.domain.entities.DistribusjonInfo;
import no.nav.dokmet.core.domain.entities.DistribusjonVarsel;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;
import no.nav.dokmet.core.domain.kode.KonvoluttvinduTypeCode;
import no.nav.dokmet.core.domain.kode.SentralPrintDokumentTypeCode;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DistribusjonInfoBuilder extends Builder<DistribusjonInfo> {

	private Long id;
	private DistribusjonKanalKode predefinertDistKanal;
	private String portoklasse;
	private Integer sikkerhetsnivaa;
	private Boolean tosidigPrint;
	private SentralPrintDokumentTypeCode sentralPrintDokumentType;
	private KonvoluttvinduTypeCode konvoluttvinduType;
	private Set<DistribusjonVarsel> distribusjonVarsels = new HashSet<>();

	public static DistribusjonInfoBuilder aDistribusjonInfo() {
		return new DistribusjonInfoBuilder();
	}

	public DistribusjonInfoBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public DistribusjonInfoBuilder predefinertDistKanal(DistribusjonKanalKode predefinertDistKanal) {
		this.predefinertDistKanal = predefinertDistKanal;
		return this;
	}

	public DistribusjonInfoBuilder portoklasse(String portoklasse) {
		this.portoklasse = portoklasse;
		return this;
	}

	public DistribusjonInfoBuilder sikkerhetsnivaa(Integer sikkerhetsnivaa) {
		this.sikkerhetsnivaa = sikkerhetsnivaa;
		return this;
	}

	public DistribusjonInfoBuilder tosidigPrint(Boolean tosidigPrint) {
		this.tosidigPrint = tosidigPrint;
		return this;
	}

	public DistribusjonInfoBuilder sentralPrintDokumentType(SentralPrintDokumentTypeCode sentralPrintDokumentType) {
		this.sentralPrintDokumentType = sentralPrintDokumentType;
		return this;
	}

	public DistribusjonInfoBuilder konvoluttvinduType(KonvoluttvinduTypeCode konvoluttvinduType) {
		this.konvoluttvinduType = konvoluttvinduType;
		return this;
	}

	public DistribusjonInfoBuilder distribusjonVarsel(DistribusjonVarsel... varsels) {
		this.distribusjonVarsels.addAll(Arrays.asList(varsels));
		return this;
	}

	public DistribusjonInfoBuilder distribusjonVarsel(Collection<DistribusjonVarsel> varsels) {
		this.distribusjonVarsels.addAll(varsels);
		return this;
	}

	@Override
	public DistribusjonInfo build() {
		DistribusjonInfo distribusjonInfo = new DistribusjonInfo(id, 1L);
		distribusjonInfo.setPredefinertDistKanal(predefinertDistKanal);
		distribusjonInfo.setPortoklasse(portoklasse);
		distribusjonInfo.setSikkerhetsnivaa(sikkerhetsnivaa);
		distribusjonInfo.setDistribusjonVarsels(distribusjonVarsels);
		distribusjonInfo.setTosidigPrint(tosidigPrint);
		distribusjonInfo.setSentralPrintDokumentType(sentralPrintDokumentType);
		distribusjonInfo.setKonvoluttvinduType(konvoluttvinduType);
		return distribusjonInfo;
	}

}
