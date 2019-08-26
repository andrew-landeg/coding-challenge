package uk.org.landeg.kalah.resolver;

/**
 * Provides a strategy for resolving the current hostname.
 * 
 * This may or may not be known in advance.
 * 
 * @author Andrew Landeg
 *
 */
public interface RequestUriResolver {
	String resolve();
}
