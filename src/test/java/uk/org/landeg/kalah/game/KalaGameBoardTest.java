package uk.org.landeg.kalah.game;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import uk.org.landeg.kalah.Constants.Player;

/**
 * Sanity checks, check the game board look-ups are set up correctly.
 *
 * @author Andrew Landeg
 *
 */
public class KalaGameBoardTest {
	final KalahGameBoardStandard board = new KalahGameBoardStandard();

	@Test
	public void assertSouthPlayerPits() {
		final List<Integer> expected = Arrays.asList(1,2,3,4,5,6);
		final List<Integer> actual = board.getPlayerPits().get(Player.SOUTH);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void assertNorthPlayerPits() {
		final List<Integer> expected = Arrays.asList(8,9,10,11,12,13); 
		final List<Integer> actual = board.getPlayerPits().get(Player.NORTH);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void assertPlayerKalah() {
		Assert.assertEquals(7, board.getPlayerKalah().get(Player.SOUTH).intValue());
		Assert.assertEquals(14, board.getPlayerKalah().get(Player.NORTH).intValue());
	}

	@Test
	public void assertMoveSequences() {
		final List<Integer> expectedSouth = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13);
		final List<Integer> expectedNorth = Arrays.asList(1,2,3,4,5,6,8,9,10,11,12,13,14);
		Assert.assertEquals(expectedSouth, board.getMoveSequence().get(Player.SOUTH));
		Assert.assertEquals(expectedNorth, board.getMoveSequence().get(Player.NORTH));
	}

	@Test
	public void assertOppisingPitsLookup() {
		final Map<Integer, Integer> opposingPits = board.getOpposingPits();
		Assert.assertEquals(13, opposingPits.get(1).intValue());
		Assert.assertEquals(12, opposingPits.get(2).intValue());
		Assert.assertEquals(11, opposingPits.get(3).intValue());
		Assert.assertEquals(10, opposingPits.get(4).intValue());
		Assert.assertEquals(9, opposingPits.get(5).intValue());
		Assert.assertEquals(8, opposingPits.get(6).intValue());

		Assert.assertEquals(6, opposingPits.get(8).intValue());
		Assert.assertEquals(5, opposingPits.get(9).intValue());
		Assert.assertEquals(4, opposingPits.get(10).intValue());
		Assert.assertEquals(3, opposingPits.get(11).intValue());
		Assert.assertEquals(2, opposingPits.get(12).intValue());
		Assert.assertEquals(1, opposingPits.get(13).intValue());
		
		// kalah 7,14 dont participate in captures, no opposing pit expected...
		Assert.assertNull(opposingPits.get(7));
		Assert.assertNull(opposingPits.get(14));
		
		final int totalPlayerPits = 6 * 2;
		Assert.assertEquals(totalPlayerPits, opposingPits.size());
	}
}
