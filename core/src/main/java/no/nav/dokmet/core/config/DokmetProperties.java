package no.nav.dokmet.core.config;


import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Data
@Validated
@ConfigurationProperties("dokmet")
public class DokmetProperties {

    private final Proxy proxy = new Proxy();
    private final Database database = new Database();
    private final Endpoints endpoints = new Endpoints();
    private final Serviceuser serviceuser = new Serviceuser();


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
    public static class Database {

        @Positive
        private int poolsize = 20;
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
