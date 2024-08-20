package no.nav.dokmet.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.concurrent.TimeUnit.DAYS;

@Configuration
@EnableCaching
public class CacheConfig {

	public static final String BREVPAKKE_CACHE = "brevpakkeCache";

	@Bean
	CacheManager cacheManager() {
		CaffeineCacheManager manager = new CaffeineCacheManager();
		caffeineCaches().forEach(caffeineCache -> manager.registerCustomCache(caffeineCache.getName(), caffeineCache.getNativeCache()));
		return manager;
	}

	private List<CaffeineCache> caffeineCaches() {
		return List.of(
				new CaffeineCache(BREVPAKKE_CACHE, Caffeine.newBuilder()
						.expireAfterWrite(1, DAYS)
						.maximumSize(20)
						.build())
		);
	}
}
