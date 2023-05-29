package uk.org.landeg.kalah.game;

import org.junit.jupiter.api.Test;
import uk.org.landeg.kalah.Constants.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Sanity checks, check the game board look-ups are set up correctly.
 *
 * @author Andrew Landeg
 *
 */
class KalaGameBoardTest {
	final KalahGameBoardStandard board = new KalahGameBoardStandard();

	@Test
	void assertSouthPlayerPits() {
		final List<Integer> expected = Arrays.asList(1,2,3,4,5,6);
		final List<Integer> actual = board.getPlayerPits().get(Player.SOUTH);
		assertEquals(expected, actual);
	}

	@Test
	void assertNorthPlayerPits() {
		final List<Integer> expected = Arrays.asList(8,9,10,11,12,13); 
		final List<Integer> actual = board.getPlayerPits().get(Player.NORTH);
		assertEquals(expected, actual);
	}

	@Test
	void assertPlayerKalah() {
		assertThat(7).isEqualTo(board.getPlayerKalah().get(Player.SOUTH).intValue());
		assertThat(14).isEqualTo(board.getPlayerKalah().get(Player.NORTH).intValue());
	}

	@Test
	void assertMoveSequences() {
		final List<Integer> expectedSouth = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13);
		final List<Integer> expectedNorth = Arrays.asList(1,2,3,4,5,6,8,9,10,11,12,13,14);
		assertEquals(expectedSouth, board.getMoveSequence().get(Player.SOUTH));
		assertEquals(expectedNorth, board.getMoveSequence().get(Player.NORTH));
	}

	@Test
	void assertOppisingPitsLookup() {
		final Map<Integer, Integer> opposingPits = board.getOpposingPits();
		assertThat(13).isEqualTo(opposingPits.get(1).intValue());
		assertThat(12).isEqualTo(opposingPits.get(2).intValue());
		assertThat(11).isEqualTo(opposingPits.get(3).intValue());
		assertThat(10).isEqualTo(opposingPits.get(4).intValue());
		assertThat(9).isEqualTo(opposingPits.get(5).intValue());
		assertThat(8).isEqualTo(opposingPits.get(6).intValue());

		assertThat(6).isEqualTo(opposingPits.get(8).intValue());
		assertThat(5).isEqualTo(opposingPits.get(9).intValue());
		assertThat(4).isEqualTo(opposingPits.get(10).intValue());
		assertThat(3).isEqualTo(opposingPits.get(11).intValue());
		assertThat(2).isEqualTo(opposingPits.get(12).intValue());
		assertThat(1).isEqualTo(opposingPits.get(13).intValue());
		
		// kalah 7,14 dont participate in captures, no opposing pit expected...
		assertNull(opposingPits.get(7));
		assertNull(opposingPits.get(14));
		
		final int totalPlayerPits = 6 * 2;
		assertThat(totalPlayerPits).isEqualTo(opposingPits.size());
	}
}
