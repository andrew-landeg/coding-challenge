package uk.org.landeg.kalah.api.transformer;

import org.junit.jupiter.api.Test;

import uk.org.landeg.kalah.api.model.CreateGameResponseModel;
import uk.org.landeg.kalah.components.KalahGameState;
import uk.org.landeg.kalah.exception.KalahException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateGameResponseTransformerTest {
	private CreateGameResponseTransformer transformer = new CreateGameResponseTransformer();
	
	@Test
	void assertTransformToRestAsExpected() {
		KalahGameState state = new KalahGameState();
		state.setGameId(123456L);
		state.setUrl("http://dont-care-where");
		final CreateGameResponseModel response = transformer.toRest(state);
		assertThat(state.getGameId()).hasToString(response.getId());
		assertThat(state.getUrl()).hasToString(response.getUrl());
	}
	
	@Test
	void assertExceptionOnNullEntity() {
		assertThrows(KalahException.class, () -> transformer.toRest(null));
	}
}
