package no.nav.dokmet.core.builders.builder;


import no.nav.dokmet.core.domain.entities.DistribusjonInfo;
import no.nav.dokmet.core.domain.entities.DokumentProduksjonsInfo;
import no.nav.dokmet.core.domain.entities.SpraakInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class DokumentProduksjonInfoBuilder extends Builder<DokumentProduksjonsInfo> {

	private Long id;
	private Boolean vedlegg;
	private Boolean eksternVedlegg;
	private String ikkeRedigerbarMalId;
	private String redigerbarMalId;
	private String malLogikkFil;
	private String malXsdReferanse;
	private DistribusjonInfo distribusjonInfo;
	private final Set<SpraakInfo> spraakInfos = new HashSet<>();

	private DokumentProduksjonInfoBuilder() {
	}

	public static DokumentProduksjonInfoBuilder aDokumentProduksjonInfo() {
		return new DokumentProduksjonInfoBuilder();
	}

	@Override
	public DokumentProduksjonsInfo build() {
		DokumentProduksjonsInfo dokumentProduksjonsInfo = new DokumentProduksjonsInfo(id, 1L);
		dokumentProduksjonsInfo.setVedlegg(vedlegg);
		dokumentProduksjonsInfo.setEksternVedlegg(eksternVedlegg);
		dokumentProduksjonsInfo.setIkkeRedigerbarMalId(ikkeRedigerbarMalId);
		dokumentProduksjonsInfo.setRedigerbarMalId(redigerbarMalId);
		dokumentProduksjonsInfo.setMalLogikkFil(malLogikkFil);
		dokumentProduksjonsInfo.setMalXsdReferanse(malXsdReferanse);
		dokumentProduksjonsInfo.setDistribusjonInfo(distribusjonInfo);
		dokumentProduksjonsInfo.setSpraakInfos(spraakInfos);

		return dokumentProduksjonsInfo;
	}

	public DokumentProduksjonInfoBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public DokumentProduksjonInfoBuilder vedlegg(Boolean vedlegg) {
		this.vedlegg = vedlegg;
		return this;
	}

	public DokumentProduksjonInfoBuilder eksternVedlegg(Boolean eksternVedlegg) {
		this.eksternVedlegg = eksternVedlegg;
		return this;
	}

	public DokumentProduksjonInfoBuilder ikkeRedigerbarMalId(String ikkeRedigerbarMalId) {
		this.ikkeRedigerbarMalId = ikkeRedigerbarMalId;
		return this;
	}

	public DokumentProduksjonInfoBuilder redigerbarMalId(String redigerbarMalId) {
		this.redigerbarMalId = redigerbarMalId;
		return this;
	}

	public DokumentProduksjonInfoBuilder malLogikkFil(String malLogikkFil) {
		this.malLogikkFil = malLogikkFil;
		return this;
	}

	public DokumentProduksjonInfoBuilder malXsdReferanse(String malXsdReferanse) {
		this.malXsdReferanse = malXsdReferanse;
		return this;
	}

	public DokumentProduksjonInfoBuilder distribusjonInfo(DistribusjonInfo distribusjonInfo) {
		this.distribusjonInfo = distribusjonInfo;
		return this;
	}

	public DokumentProduksjonInfoBuilder spraakInfos (SpraakInfo... spraakInfos) {
		this.spraakInfos.addAll(Arrays.asList(spraakInfos));
		return this;
	}
}
