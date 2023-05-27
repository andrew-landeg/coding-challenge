package uk.org.landeg.kalah.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * REST Response Model object.
 * 
 * @author Andrew Landeg
 *
 */
@Data
@ToString
@NoArgsConstructor
public class CreateGameResponseModel {
	String id;
	String url;

	public CreateGameResponseModel withId(final Long gameId ) {
		this.setId(Long.toString(gameId));
		return this;
	}

	public CreateGameResponseModel withUri(final String url ) {
		this.setUrl(url);
		return this;
	}
}
