package uk.org.landeg.kalah.api.model;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Response Model object.
 * 
 * @author Andrew Landeg
 *
 */
public class PerformMoveResponseModel extends CreateGameResponseModel {
	private Map<String, String> status = new HashMap<>();

	public Map<String, String> getStatus() {
		return status;
	}
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
