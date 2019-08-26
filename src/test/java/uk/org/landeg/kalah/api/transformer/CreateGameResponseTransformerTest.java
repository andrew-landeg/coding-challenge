package uk.org.landeg.kalah.api.transformer;

import org.junit.Assert;
import org.junit.Test;

import uk.org.landeg.kalah.api.model.CreateGameResponseModel;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.exception.KalahException;

public class CreateGameResponseTransformerTest {
	private CreateGameResponseTransformer transformer = new CreateGameResponseTransformer();
	
	@Test
	public void assertTransformToRestAsExpected() {
		KalahGameState state = new KalahGameState();
		state.setGameId(123456L);
		state.setUrl("http://dont-care-where");
		final CreateGameResponseModel response = transformer.toRest(state);
		Assert.assertEquals(state.getGameId().toString(), response.getId());
		Assert.assertEquals(state.getUrl().toString(), response.getUrl());
	}
	
	@Test(expected = KalahException.class)
	public void assertExceptionOnNullEntity() {
		transformer.toRest(null);
	}
}
