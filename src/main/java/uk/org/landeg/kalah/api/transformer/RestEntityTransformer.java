package uk.org.landeg.kalah.api.transformer;

public interface RestEntityTransformer<R, T> {

	/**
	 * Transform the specified entity into a suitable rest model type.
	 * 
	 * @param source source object
	 * @return instance of a REST model object. 
	 */
	R toRest(T source);

}
