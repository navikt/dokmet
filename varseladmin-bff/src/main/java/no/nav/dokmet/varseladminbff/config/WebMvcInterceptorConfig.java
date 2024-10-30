package no.nav.dokmet.varseladminbff.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcInterceptorConfig implements WebMvcConfigurer {

	VarseladminBffInterceptor varseladminBffInterceptor;

	public WebMvcInterceptorConfig(VarseladminBffInterceptor varseladminBffInterceptor) {
		this.varseladminBffInterceptor = varseladminBffInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(varseladminBffInterceptor)
				.order(2);
	}
}
