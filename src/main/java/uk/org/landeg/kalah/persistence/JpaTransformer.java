package uk.org.landeg.kalah.persistence;

/**
 * Provide conversion to and from domain and business entities.
 *
 * @param <D> the type of domain object
 * @param <B> the type of business object
 */
public interface JpaTransformer<D, B> {
    /**
     * Maps the specified domain instance to the specified target instance.
     *
     * @param source the source domain object
     * @param target the target business object
     * @return the business object
     */
    B fromJpa(D source, B target);

    /**
     * Maps the specified business instance to the specified target domain instance.
     *
     * @param source the source business object
     * @param target the target domain object
     * @return the domain object
     */
    D toJpa(B source, D target);
}
