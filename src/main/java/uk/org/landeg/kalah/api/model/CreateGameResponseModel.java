package uk.org.landeg.kalah.api.model;

/**
 * REST Response Model object.
 * 
 * @author Andrew Landeg
 *
 */
public class CreateGameResponseModel {
	String id;
	String url;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public CreateGameResponseModel withId(final Long gameId ) {
		this.setId(Long.toString(gameId));
		return this;
	}

	public CreateGameResponseModel withUri(final String url ) {
		this.setUrl(url);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreateGameResponseModel [id=");
		builder.append(id);
		builder.append(", url=");
		builder.append(url);
		builder.append("]");
		return builder.toString();
	}
}
