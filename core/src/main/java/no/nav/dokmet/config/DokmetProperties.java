package no.nav.dokmet.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Data
@ConfigurationProperties("dokmet")
@Validated
public class DokmetProperties {

    private final Endpoints endpoints = new Endpoints();
    private final Serviceuser serviceuser = new Serviceuser();
    private final Proxy proxy = new Proxy();


    @Data
    @Validated
    public static class Endpoints {

    }

    @Data
    @Validated
    public static class AzureEndpoint {

    }

    @Data
    @Validated
    public static class Serviceuser {

        @NotEmpty
        @ToString.Exclude
        private String username;

        @NotEmpty
        @ToString.Exclude
        private String password;
    }

    @Data
    @Validated
    public static class Proxy {
        private String host;
        private int port;

        public boolean isSet() {
            return isNotBlank(host);
        }
    }
}
