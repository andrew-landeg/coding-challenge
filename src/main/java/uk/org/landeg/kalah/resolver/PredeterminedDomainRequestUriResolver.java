package uk.org.landeg.kalah.resolver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Use a predefined hostname.
 * 
 * Typically this will be provided by the environment.
 *
 * @author Andrew Landeg
 *
 */
@Component
@ConditionalOnProperty(prefix="server.location", name="url", havingValue="", matchIfMissing=false)
public class PredeterminedDomainRequestUriResolver implements RequestUriResolver{
	@Value("${server.location.url}")
	private String domain;

	@Override
	public String resolve() {
		return domain;
	}
}
