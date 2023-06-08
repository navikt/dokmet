package no.nav.dokmet.core.builders.builder;

import jakarta.persistence.EntityManager;
import org.slf4j.MDC;

/**
 * Base class for builders.
 *
 * @param <T> The type to build.
 */
public abstract class Builder<T> {

	private String userId = "builderUserId";

	public abstract T build();

	public Builder<T> userId(String userId) {
		this.userId = userId;
		return this;
	}

	public T buildAndPersist(EntityManager entityManager) {
		MDC.put("userId", userId);

		T objectToPersist = build();
		entityManager.persist(objectToPersist);
		entityManager.flush();
		return objectToPersist;
	}
}
