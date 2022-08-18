package no.nav.dokmet.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

/**
 * Validator for varsel tekst, validerer fletteparameter navn og tidsformat
 *
 * @author Andreas Skomedal, Visma Consulting.
 */
public class VarselTekstValidator implements ConstraintValidator<VarselTekst, String> {

	private static final Pattern ANY_PARAMETER_PATTERN = compile("\\{(.+?)}", CASE_INSENSITIVE);

	private static final String PARAMETER_TEKST_REGEX = "[æøåA-Z0-9\\-_]+";

	private static final Pattern PARAMETER_PATTERN = compile("\\{" + PARAMETER_TEKST_REGEX + "}", CASE_INSENSITIVE);
	private static final Pattern TIDSPUNKT_PARAMETER_PATTERN = compile("\\{(" + PARAMETER_TEKST_REGEX + "):([^}]*)}", CASE_INSENSITIVE);

	public void initialize(VarselTekst constraint) {
		/* FOR TEST */
	}

	public boolean isValid(String tekst, ConstraintValidatorContext context) {
		if (tekst == null) {
			return true;
		}
		Matcher paramMatcher = ANY_PARAMETER_PATTERN.matcher(tekst);
		while (paramMatcher.find()) {
			String param = paramMatcher.group();
			if (!PARAMETER_PATTERN.matcher(param).matches()) {
				Matcher timeParamMatcher = TIDSPUNKT_PARAMETER_PATTERN.matcher(param);
				if (timeParamMatcher.matches()) {
					String pattern = timeParamMatcher.group(2);

					try {
						DateTimeFormatter.ofPattern(pattern);
					} catch (Exception e) {
						return fail(context, String.format("Parameter '%s' har ikke et gyldig datoformat", param));
					}
				} else {
					return fail(context, String.format("Parameter '%s' er ikke et gyldig parameternavn", param));
				}
			}
		}
		return true;
	}

	private boolean fail(ConstraintValidatorContext context, String format) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(format).addConstraintViolation();
		return false;
	}
}
