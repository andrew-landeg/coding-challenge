package uk.org.landeg.kalah.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Response Model object.
 * 
 * @author Andrew Landeg
 *
 */
@NoArgsConstructor
@ToString
public class PerformMoveResponseModel extends CreateGameResponseModel {
	@Getter
	private Map<String, String> status = new HashMap<>();

	public void setStatus(Map<String, String> status) {
		this.status = status;
	}
	public PerformMoveResponseModel withPitStatus(int pitId, int stones) {
		this.status.put(Integer.toString(pitId), Integer.toString(stones));
		return this;
	}

	@Override
	public PerformMoveResponseModel withId(final Long gameId ) {
		return (PerformMoveResponseModel) super.withId(gameId);
	}

	@Override
	public PerformMoveResponseModel withUri(final String uri ) {
		return (PerformMoveResponseModel) super.withUri(uri);
	}
}
