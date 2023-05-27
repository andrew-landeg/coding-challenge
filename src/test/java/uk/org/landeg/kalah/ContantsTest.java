package uk.org.landeg.kalah;


import org.junit.jupiter.api.Test;

import uk.org.landeg.kalah.Constants.Player;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstantsTest {
	@Test
	void assertPlayerOpponent() {
		assertThat(Player.SOUTH.getOpponent()).isEqualTo(Player.NORTH);
		assertThat(Player.NORTH.getOpponent()).isEqualTo(Player.SOUTH);
	}
}
