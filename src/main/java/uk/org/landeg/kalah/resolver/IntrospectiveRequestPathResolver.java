package uk.org.landeg.kalah.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Infer the hostname by inspecting the requested URL of incoming requests.
 *
 * @author Andrew Landeg
 *
 */
@Component
@ConditionalOnProperty(prefix="server.location", name="url", havingValue="false", matchIfMissing=true)
public class IntrospectiveRequestPathResolver implements RequestUriResolver {

	@Override
	public String resolve() {
		HttpServletRequest request = 
		        ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes())
		                .getRequest();
		return request.getRequestURL().toString();
	}

}
