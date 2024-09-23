package no.nav.dokmet.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RestInterceptorConfig implements WebMvcConfigurer {

	HandlerInterceptor basicAuthRestInterceptor;

	public RestInterceptorConfig(HandlerInterceptor basicAuthRestInterceptor) {
		this.basicAuthRestInterceptor = basicAuthRestInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(basicAuthRestInterceptor)
				.addPathPatterns("/rest/basicauth/**");
	}
}
