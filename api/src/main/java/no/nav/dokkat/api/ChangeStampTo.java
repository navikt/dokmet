package no.nav.dokkat.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * @deprecated Bruk {@link no.nav.dokmet.api.tkat020.ChangeStampTo}
 */
@Deprecated
public class ChangeStampTo {
    
    private String opprettetAv;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime opprettetDato;

    private String endretAv;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endretDato;

    public String getOpprettetAv() {
        return opprettetAv;
    }

    public void setOpprettetAv(String opprettetAv) {
        this.opprettetAv = opprettetAv;
    }

    public LocalDateTime getOpprettetDato() {
        return opprettetDato;
    }

    public void setOpprettetDato(LocalDateTime opprettetDato) {
        this.opprettetDato = opprettetDato;
    }

    public String getEndretAv() {
        return endretAv;
    }

    public void setEndretAv(String endretAv) {
        this.endretAv = endretAv;
    }

    public LocalDateTime getEndretDato() {
        return endretDato;
    }

    public void setEndretDato(LocalDateTime endretDato) {
        this.endretDato = endretDato;
    }


}
