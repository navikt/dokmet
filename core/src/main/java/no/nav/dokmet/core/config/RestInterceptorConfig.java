package no.nav.dokmet.core.config;

import no.nav.dokmet.core.interceptor.SporingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RestInterceptorConfig implements WebMvcConfigurer {

	private final BasicAuthRestInterceptor basicAuthRestInterceptor;
	private final SporingInterceptor sporingInterceptor;

	public RestInterceptorConfig(BasicAuthRestInterceptor basicAuthRestInterceptor,
								 SporingInterceptor sporingInterceptor) {
		this.basicAuthRestInterceptor = basicAuthRestInterceptor;
		this.sporingInterceptor = sporingInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sporingInterceptor)
				.addPathPatterns("/rest/**")
				.order(0);
		registry.addInterceptor(basicAuthRestInterceptor)
				.addPathPatterns("/rest/basicauth/**")
				.order(1);
	}
}