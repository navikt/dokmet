package no.nav.dokmet.core.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.net.InetSocketAddress;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Data
@Validated
@ConfigurationProperties("dokmet")
public class DokmetProperties {

    private final Proxy proxy = new Proxy();
    private final Database database = new Database();
    private final Serviceuser serviceuser = new Serviceuser();

	@NotEmpty
	private String baseUrl;

	@NotEmpty
	private String scopesForBff;

	public String[] getScopesForBff() {
		return scopesForBff.split(",");
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
        /**
         * Statisk pool verdi for dokkat databasen.
         * <p>
         * Optimizing UCP behaviour https://docs.oracle.com/database/121/JJUCP/optimize.htm#JJUCP8143
         * About Optimizing Real-World Performance with Static Connection Pools
         * https://docs.oracle.com/en/database/oracle/oracle-database/19/jjucp/optimizing-real-world-performance.html
         * select STAT_NAME, to_char(VALUE) as VALUE, COMMENTS from v$osstat where stat_name IN ('NUM_CPUS','NUM_CPU_CORES','NUM_CPU_SOCKETS');
         * Dokmet har et tak på 500 tilkoblinger
         * Poolsize * max_pods må altså ikke overstige 500
         * Current er satt til max 60 * 4 = 240
         * @see no.nav.dokmet.core.repository.RepositoryConfig
         */
        @Positive
        private int poolsize = 60;
    }

    @Data
    @Validated
    public static class Proxy {
        private String host;
        private int port;

        public boolean isSet() {
            return isNotBlank(host);
        }

		public java.net.Proxy toJavaProxy() {
			return new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(host, port));
		}
    }
}
