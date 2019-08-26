package uk.org.landeg.kalah.persistence;

/**
 * Provide conversion to and from domain and business entities.
 *
 * @param <DOMAIN> the type of domain object
 * @param <BUSINESS> the type of business object
 */
public interface JpaTransformer<DOMAIN, BUSINESS> {
    /**
     * Maps the specified domain instance to the specified target instance.
     *
     * @param source the source domain object
     * @param target the target business object
     * @return the business object
     */
    BUSINESS fromJpa(DOMAIN source, BUSINESS target);

    /**
     * Maps the specified business instance to the specified target domain instance.
     *
     * @param source the source business object
     * @param target the target domain object
     * @return the domain object
     */
    DOMAIN toJpa(BUSINESS source, DOMAIN target);
}
