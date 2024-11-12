package no.nav.dokmet.core.builders.builder;

import no.nav.dokmet.core.domain.entities.DokumentProduksjonsInfo;
import no.nav.dokmet.core.domain.entities.DokumenttypeInfo;
import no.nav.dokmet.core.domain.kode.ArkivSystemKode;
import no.nav.dokmet.core.domain.kode.DokumentTypeKode;

public final class DokumenttypeInfoBuilder extends Builder<DokumenttypeInfo> {
	
	private Long id;
	private String dokumenttypeId;
	private String dokumentTittel;
	private String dokumentKategori;
	private Boolean sensitivt;
	private boolean utledRegisterInfo;
	private String tema;
	private ArkivSystemKode arkivSystem = ArkivSystemKode.JOARK;
	private DokumentProduksjonsInfo dokumentProduksjonsInfo;
	private DokumentTypeKode dokumentType;

	private DokumenttypeInfoBuilder() {
	}
	
	public static DokumenttypeInfoBuilder builder() {
		return new DokumenttypeInfoBuilder();
	}
	
	@Override
	public DokumenttypeInfo build() {
		DokumenttypeInfo dokumenttypeInfo = new DokumenttypeInfo(id, 1L);
		dokumenttypeInfo.setDokumenttypeId(dokumenttypeId);
		dokumenttypeInfo.setDokumentTittel(dokumentTittel);
		dokumenttypeInfo.setDokumentKategori(dokumentKategori);
		dokumenttypeInfo.setSensitivt(sensitivt);
		dokumenttypeInfo.setUtledRegisterInfo(utledRegisterInfo);
		dokumenttypeInfo.setTema(tema);
		dokumenttypeInfo.setDokumentType(dokumentType);
		dokumenttypeInfo.setArkivSystem(arkivSystem);
		dokumenttypeInfo.setDokumentProduksjonsInfo(dokumentProduksjonsInfo);

		if (dokumentProduksjonsInfo != null) {
			dokumenttypeInfo.getDokumentProduksjonsInfo().setDokumenttypeInfo(dokumenttypeInfo);
		}
		
		return dokumenttypeInfo;
	}
	
	public DokumenttypeInfoBuilder id(Long id) {
		this.id = id;
		return this;
	}
	
	public DokumenttypeInfoBuilder dokumenttypeId(String dokumentTypeId) {
		this.dokumenttypeId = dokumentTypeId;
		return this;
	}
	
	public DokumenttypeInfoBuilder dokumentTittel(String dokumentTittel) {
		this.dokumentTittel = dokumentTittel;
		return this;
	}
	
	public DokumenttypeInfoBuilder dokumentKategori(String dokumentKategori) {
		this.dokumentKategori = dokumentKategori;
		return this;
	}
	
	public DokumenttypeInfoBuilder sensitivt(Boolean sensitivt) {
		this.sensitivt = sensitivt;
		return this;
	}

	public DokumenttypeInfoBuilder utledRegisterInfo(boolean utledRegisterInfo) {
		this.utledRegisterInfo = utledRegisterInfo;
		return this;
	}

	public DokumenttypeInfoBuilder tema(String tema) {
		this.tema = tema;
		return this;
	}
	
	public DokumenttypeInfoBuilder dokumentProduksjonsInfo(DokumentProduksjonsInfo dokumentProduksjonsInfo) {
		this.dokumentProduksjonsInfo = dokumentProduksjonsInfo;
		return this;
	}
	
	public DokumenttypeInfoBuilder dokumentType(DokumentTypeKode dokumentType) {
		this.dokumentType = dokumentType;
		return this;
	}
	
	public DokumenttypeInfoBuilder arkivSystem(ArkivSystemKode arkivSystem) {
		this.arkivSystem = arkivSystem;
		return this;
	}
	
}