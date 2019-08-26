package uk.org.landeg.kalah;

public class Constants {
	public enum Player {
		SOUTH, NORTH, NONE;

		public Player getOpponent() {
			return (this == SOUTH) ? NORTH : SOUTH;  
		}
	}
}
