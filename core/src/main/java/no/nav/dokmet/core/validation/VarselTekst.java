package no.nav.dokmet.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validation for Varsel Parameters in a Varseltekst
 *
 * @author Andreas Skomedal, Visma Consulting.
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = VarselTekstValidator.class)
public @interface VarselTekst {
	String message() default "Ugyldig varseltekst";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
