package no.nav.dokmet.core.builders.builder;

import no.nav.dokmet.core.domain.entities.DistribusjonVarsel;
import no.nav.dokmet.core.domain.kode.DistribusjonKanalKode;

public class DistribusjonVarselBuilder extends Builder<DistribusjonVarsel> {

	private Long id;
	private DistribusjonKanalKode varselForDistribusjonKanal;
	private String varseltypeId;

	public static DistribusjonVarselBuilder aDistribusjonVarsel() {
		return new DistribusjonVarselBuilder();
	}

	public DistribusjonVarselBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public DistribusjonVarselBuilder varselForDistribusjonKanal(DistribusjonKanalKode varselForDistribusjonKanal) {
		this.varselForDistribusjonKanal = varselForDistribusjonKanal;
		return this;
	}

	public DistribusjonVarselBuilder varseltypeId(String varseltypeId) {
		this.varseltypeId = varseltypeId;
		return this;
	}

	@Override
	public DistribusjonVarsel build() {
		DistribusjonVarsel distribusjonVarsel = new DistribusjonVarsel();
		distribusjonVarsel.setId(id);
		distribusjonVarsel.setVarselForDistribusjonKanal(varselForDistribusjonKanal);
		distribusjonVarsel.setVarseltypeId(varseltypeId);
		return distribusjonVarsel;
	}
}
