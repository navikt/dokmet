package no.nav.dokkat.api;

/**
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat020.AbstractToObject}
 */
@Deprecated
public abstract class AbstractToObject {
	private ChangeStampTo changeStamp;

	public ChangeStampTo getChangeStamp() {
		return changeStamp;
	}

	public void setChangeStamp(ChangeStampTo changeStamp) {
		this.changeStamp = changeStamp;
	}
}
