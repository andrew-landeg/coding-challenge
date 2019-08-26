package uk.org.landeg.kalah;


import org.junit.Assert;
import org.junit.Test;

import uk.org.landeg.kalah.Constants.Player;

public class ContantsTest {
	@Test
	public void assertPlayerOpponent() {
		Assert.assertTrue(Player.SOUTH.getOpponent().equals(Player.NORTH));
		Assert.assertTrue(Player.NORTH.getOpponent().equals(Player.SOUTH));
	}
}
