package no.nav.dokmet.core.builders.builder;

import no.nav.dokmet.core.domain.entities.SpraakInfo;


public class SpraakInfoBuilder extends Builder<SpraakInfo> {

	private Long id;
	private String spraaklag;

	public static SpraakInfoBuilder aSoraakInfo() {
		return new SpraakInfoBuilder();
	}

	public SpraakInfoBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public SpraakInfoBuilder spraaklag(String spraaklag) {
		this.spraaklag = spraaklag;
		return this;
	}

	@Override
	public SpraakInfo build() {
		SpraakInfo spraakInfo = new SpraakInfo();
		spraakInfo.setId(id);
		spraakInfo.setSpraaklag(spraaklag);
		return spraakInfo;
	}
}
